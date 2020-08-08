/**
 * This enum class includes available cases to run and visualize.
 */
public enum ProblemType {

  DIJKSTRA_BASE("Base Dijkstra Algorithm"),
  MUST_GO("Dijkstra with Must Go Certain Nodes"),
  BYPASSING("Dijkstra with Bypassing Certain Nodes"),
  CHEAPEST_FLIGHTS("Cheapest Flights Within K Stops"),
  NETWORK_DELAY("Network Delay Time"),
  WIDEST_PATH("The Widest Path Between Two Hosts");

  private String value;

  ProblemType(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}