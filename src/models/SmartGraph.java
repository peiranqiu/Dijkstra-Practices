package models;

import java.util.List;
import java.util.Set;

/**
 * Smartgraph represents a digraph with its shortest path for the specific problem.
 */
public class SmartGraph {
  private Graph graph;
  private List<Node> path;

  public SmartGraph(Graph graph, List<Node> path) {
    this.graph = graph;
    this.path = path;
  }

  public Graph getGraph() {
    return graph;
  }

  public void setGraph(Graph g) {
    this.graph = g;
  }

  public List<Node> getPath() {
    return path;
  }
}
