import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.sqrt
import kotlin.random.Random

// Data model for news nodes
data class NewsNode(val id: String, var position: Offset = Offset.Zero, var velocity: Offset = Offset.Zero)

class NewsGraphActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val sampleNews = listOf(
                    NewsNode("Tech"),
                    NewsNode("AI"),
                    NewsNode("Stocks"),
                    NewsNode("Policy"),
                    NewsNode("Health"),
                    NewsNode("Crypto")
                )

                NewsGraphScreen(sampleNews)
            }
        }
    }
}

@Composable
fun NewsGraphScreen(newsArticles: List<NewsNode>) {
    val nodes = remember {
        newsArticles.map { node ->
            node.copy(
                position = Offset(Random.nextFloat() * 800, Random.nextFloat() * 800),
                velocity = Offset(Random.nextFloat() * 2 - 1, Random.nextFloat() * 2 - 1)
            )
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            applyForces(nodes)
            delay(16L) // Approx. 60 FPS
        }
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawGraph(nodes)
        }
    }
}

fun applyForces(nodes: List<NewsNode>) {
    val repulsionStrength = 10000f
    val attractionStrength = 0.1f
    val damping = 0.9f

    for (i in nodes.indices) {
        var force = Offset.Zero
        for (j in nodes.indices) {
            if (i != j) {
                val delta = nodes[j].position - nodes[i].position
                val distance = maxOf(sqrt(delta.x * delta.x + delta.y * delta.y), 1f)
                val repulsion = delta / distance * (repulsionStrength / (distance * distance))
                force += repulsion
            }
        }

        nodes[i].velocity = (nodes[i].velocity + force * attractionStrength) * damping
        nodes[i].position += nodes[i].velocity
    }
}

fun DrawScope.drawGraph(nodes: List<NewsNode>) {
    // Draw nodes
    nodes.forEach { node ->
        drawCircle(
            color = Color.Blue,
            center = node.position,
            radius = 30f
        )

        drawContext.canvas.nativeCanvas.drawText(
            node.id,
            node.position.x - 20f,
            node.position.y + 5f,
            android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 30f
            }
        )
    }
}
