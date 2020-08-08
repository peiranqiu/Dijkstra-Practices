package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Graph consists of nodes and edges. It can be undirected or directed.
 */
public class Graph {
  private Set<Node> nodes;
  private boolean directed;
  private List<Edge> originalEdges;

  public Graph(boolean dir) {
    directed = dir;
    nodes = new HashSet<>();
    originalEdges = new ArrayList<>();
  }

  public Graph(Set<Node> newNodes, List<Edge> newEdges, boolean dir) {

    nodes = new HashSet<>(newNodes);
    originalEdges = new ArrayList<>(newEdges);
    directed = dir;
  }

  public Set<Node> getNodes() {
    return nodes;
  }

  public List<Edge> getOriginalEdges() {
    return originalEdges;
  }

  public void addNodes(Node... n) {

    nodes.addAll(Arrays.asList(n));
  }

  public void addEdges(Edge... newEdges) {
    for (Edge edge: newEdges) {
      Node sourceNode = edge.getSourceNode();
      Node destNode = edge.getDestNode();

      nodes.add(sourceNode);
      nodes.add(destNode);
      originalEdges.add(edge);

      // go through all the edges and update edge weight if the edge is already added
      for(Edge e : sourceNode.getEdges()) {
        if (e.getSourceNode() == sourceNode && e.getDestNode() == destNode) {
          e.setWeight(edge.getWeight());
          return;
        }
      }
      sourceNode.getEdges().add(edge);
      destNode.getIntoEdges().add(edge);
    }
  }

  public void resetNode() {
    for(Node node: nodes){
      node.setVisited(false);
    }
  }
  public boolean hasNode(Node node) {
    return nodes.contains(node);
  }

  public void removeNode(Node node) {
    nodes.remove(node);
  }

  public void removeEdges(Node bypass) {
    for (Node node: nodes) {
      remove(node.getIntoEdges(), bypass);
      remove(node.getEdges(), bypass);
    }
  }

  private static void remove(List<Edge> edges, Node bypass) {
    if (edges == null || edges.size() == 0) {
      return;
    }

    // mutation!!!
    List<Edge> temp = new ArrayList<>(edges);
    for (Edge edge: temp) {
      if (containsBypass(edge, bypass)) {
        edges.remove(edge);
      }
    }
  }

  private static boolean containsBypass(Edge edge, Node bypass) {
    return edge.getSourceNode().getName().equals(bypass.getName())
            || edge.getDestNode().getName().equals(bypass.getName());
  }
}
