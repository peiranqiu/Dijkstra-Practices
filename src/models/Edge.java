package models;

/**
 * Edge represents an edge connecting two nodes in a graph.
 */
public class Edge implements Comparable<Edge>{
  private Node source;
  private Node dest;
  private double weight;

  public Edge(Node src, Node dest, double weight) {
    this.source = src;
    this.dest = dest;
    this.weight = weight;
  }

  public Node getSourceNode() {
    return source;
  }

  public void setSourceNode(Node source) {
    this.source = source;
  }

  public Node getDestNode() {
    return dest;
  }

  public void setDestNode(Node dest) {
    this.dest = dest;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  @Override
  public int compareTo(Edge that) {
    if (this.weight > that.weight) {
      return 1;
    }
    return -1;
  }
}
