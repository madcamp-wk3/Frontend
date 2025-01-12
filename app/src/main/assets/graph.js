const nodes = [];
const links = [];

document.addEventListener("DOMContentLoaded", function () {

console.log("document addEventListener 실행")
const svg = d3.select("body").append("svg")
    .attr("width", window.innerWidth)
    .attr("height", window.innerHeight);

const simulation = d3.forceSimulation(nodes)
    .force("link", d3.forceLink(links).id(d => d.id).distance(100))
    .force("charge", d3.forceManyBody().strength(-200))
    .force("center", d3.forceCenter(window.innerWidth / 2, window.innerHeight / 2));

const link = svg.append("g").selectAll("line");
const node = svg.append("g").selectAll("circle");


window.updateGraph = function (newNodes, newLinks) {
    nodes.splice(0, nodes.length, ...newNodes);
    links.splice(0, links.length, ...newLinks);


    const linkUpdate = link.data(links);
    linkUpdate.exit().remove();
    linkUpdate.enter().append("line")
        .attr("stroke", "#aaa")
        .attr("stroke-width", 1)
        .merge(link);


    const nodeUpdate = node.data(nodes);
    nodeUpdate.exit().remove();
    nodeUpdate.enter().append("circle")
        .attr("r", d => d.size || 5)
        .attr("fill", "#69b3a2")
        .merge(node);


    simulation.nodes(nodes);
    simulation.force("link").links(links);
    simulation.alpha(1).restart();
};
});