package com.example.madcamp_wk3.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.sqrt
import kotlin.random.Random

const val SCREEN_WIDTH = 800f
const val SCREEN_HEIGHT = 800f
const val CONNECTION_THRESHOLD = 250f


data class GraphNode(
    val id: String,
    val title: String,
    val url: String,
    val category: String,
    var position: Offset,
    var velocity: Offset = Offset.Zero
)

@Composable
fun NewsGraphScreen(newsNodes: List<GraphNode>) {
    Log.d("NewsGraphScreen", "📡 Received ${newsNodes.size} nodes for graph visualization")

    var nodes by remember { mutableStateOf(newsNodes) }
    val links = remember {
        nodes.flatMapIndexed { index, nodeA ->
            nodes.drop(index + 1).mapNotNull { nodeB ->
                if (nodeA.category == nodeB.category) {
                    Log.d("NewsGraphScreen", "🔗 Creating link between: ${nodeA.title} ↔ ${nodeB.title}")
                    Pair(nodeA, nodeB)
                } else null
            }
        }
    }

    val context = LocalContext.current
    var selectedNode by remember { mutableStateOf<GraphNode?>(null) }

    LaunchedEffect(Unit) {
        while (true) {
            nodes = applyForceDirectedLayout(nodes)
            delay(16L) // 60 FPS
        }
    }

    Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
        detectTapGestures { tapOffset ->
            nodes.forEach { node ->
                if ((node.position - tapOffset).getDistance() <= 40f) {
                    selectedNode = node
                    Log.d("NewsGraphScreen", "🖱️ Node Clicked: ${node.title}")
                }
            }
        }
    }) {
        drawRect(color = Color.Black, size = size)
        drawGraph(nodes, links)
    }

    selectedNode?.let { node ->
        AlertDialog(
            onDismissRequest = { selectedNode = null },
            title = { Text(text = "Open News Article?") },
            text = { Text(text = node.title) },
            confirmButton = {
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(node.url))
                    context.startActivity(intent)
                    selectedNode = null
                }) {
                    Text("Open")
                }
            },
            dismissButton = {
                Button(onClick = { selectedNode = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun DrawScope.drawGraph(nodes: List<GraphNode>, links: List<Pair<GraphNode, GraphNode>>) {
    // Draw edges (connections between nodes)
    links.forEach { (nodeA, nodeB) ->
        drawLine(
            color = Color.Gray, // Edge color
            start = nodeA.position,
            end = nodeB.position,
            strokeWidth = 2f
        )
    }

    // Draw nodes
    nodes.forEach { node ->
        drawCircle(
            color = Color.White,
            center = node.position,
            radius = 30f
        )

        // ✅ Correct way to draw text inside the node
        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK  // Set text color properly
                textSize = 30f                        // Set text size properly
            }
            canvas.nativeCanvas.drawText(
                node.title.take(2),  // Show first two characters for brevity
                node.position.x - 15f,
                node.position.y + 10f,
                paint
            )
        }
    }
}

// Physics-based Force Layout Algorithm
fun applyForceDirectedLayout(nodes: List<GraphNode>): List<GraphNode> {
    val forceStrength = 0.1f
    val repulsionDistance = 150f

    return nodes.map { node ->
        var newVelocity = node.velocity

        nodes.forEach { other ->
            if (node != other) {
                val diff = node.position - other.position
                val distance = diff.getDistance().coerceAtLeast(1f)
                if (distance < repulsionDistance) {
                    newVelocity += diff / (distance * 5)
                }
            }
        }

        val centerForce = (Offset(500f, 500f) - node.position) * forceStrength
        newVelocity += centerForce
        newVelocity *= 0.9f

        val newPosition = node.position + newVelocity
        node.copy(position = newPosition, velocity = newVelocity)
    }
}

// Helper Functions
fun Offset.getDistance(): Float = sqrt(x * x + y * y)

fun randomPosition(): Offset = Offset(Random.nextFloat() * 800f, Random.nextFloat() * 800f)