package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Node represents a vertex in a graph.
 */
public class Node {
  private String name;
  private List<Edge> edges;
  private List<Edge> intoEdges;
  private boolean visited;

  public Node(String name) {
    this.name = name;
    edges = new ArrayList<>();
    intoEdges = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public List<Edge> getEdges() {
    return edges;
  }

  public List<Edge> getIntoEdges() {
    return intoEdges;
  }

  public void setEdges(List<Edge> edges) {
    this.edges = edges;
  }

  public boolean isVisited() {
    return visited;
  }

  public void setVisited(boolean visited) {
    this.visited = visited;
  }
}
