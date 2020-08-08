import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import models.Edge;
import models.Graph;
import models.Node;
import models.SmartGraph;

/**
 * This class includes implementations for base dijkstra problem, mast go and bypassing.
 */
public class DijkstraService {
  private final double MAX = Double.MAX_VALUE;
  private static double lowestCost;
  private static List<Node> shortestPath;

  /**
   * Base dijkstra algorithm implementation.
   *
   * @param graph the given graph
   * @param start the start node
   * @param end   the end node
   */
  public SmartGraph dijkstraBase(Graph graph, Node start, Node end) {
    if (!graph.getNodes().contains(start) || !graph.getNodes().contains(end)) {
      System.out.println("Graph doesn't contain start node or end node.");
      return new SmartGraph(graph, null);
    }

    Map<Node, Double> shortestPathMap = new HashMap<>();
    Map<Node, Node> changedAtMap = new HashMap<>();
    changedAtMap.put(start, null);
    List<Node> pathList = new ArrayList<>();

    for (Node node : graph.getNodes()) {
      if (node == start) {
        shortestPathMap.put(start, 0.0);
      } else {
        shortestPathMap.put(node, MAX);
      }
    }

    for (Edge edge : start.getEdges()) {
      shortestPathMap.put(edge.getDestNode(), edge.getWeight());
      changedAtMap.put(edge.getDestNode(), start);
    }

    start.setVisited(true);

    while (true) {
      Node currentNode = getClosedNode(shortestPathMap);

      if (currentNode == null) {
        System.out.println("\nNo available path between the start node and end node.");
        graph.resetNode();
        return new SmartGraph(graph, null);
      }

      if (currentNode == end) {
        Node child = end;

        pathList.add(end);
        while (true) {
          Node parent = changedAtMap.get(child);
          if (parent == null) {
            break;
          }
          pathList.add(parent);
          child = parent;
        }

        double cost = shortestPathMap.get(end);
        System.out.println("the path costs: " + cost);
        lowestCost = cost;

        System.out.print("the path is: ");
        Collections.reverse(pathList);
        for (Node node : pathList) {
          System.out.print(node.getName() + " ");
        }
        System.out.println();
        shortestPath = pathList;

        graph.resetNode();
        return new SmartGraph(graph, pathList);
      }

      currentNode.setVisited(true);

      for (Edge edge : currentNode.getEdges()) {
        if (edge.getDestNode().isVisited()) {
          continue;
        }

        if (shortestPathMap.get(currentNode) < edge.getWeight() + shortestPathMap.get(edge.getDestNode())) {
          shortestPathMap.put(edge.getDestNode(), edge.getWeight() + shortestPathMap.get(currentNode));
          changedAtMap.put(edge.getDestNode(), currentNode);
        }
      }
    }
  }

  private Node getClosedNode(Map<Node, Double> shortestPathMap) {
    double shortestDistance = MAX;
    Node closedNode = null;

    for (Node node : shortestPathMap.keySet()) {
      if (node.isVisited()) {
        continue;
      }

      double currDistance = shortestPathMap.get(node);
      if (currDistance < shortestDistance) {
        shortestDistance = currDistance;
        closedNode = node;
      }
    }

    return closedNode;
  }

  private static double getCost() {
    return lowestCost;
  }

  private static List<Node> getPath() {
    return shortestPath;
  }

  /**
   * To find the shortest path with bypassing certain node(s).
   *
   * @param graph    the given graph
   * @param start    the starting node
   * @param end      the destination node
   * @param bypasses a list of nodes to be bypassed
   */
  public SmartGraph dijkstraBypass(Graph graph, Node start, Node end, List<Node> bypasses) {

    // keep a copy of the original graph
    Graph originalGraph = new Graph(graph.getNodes(), graph.getOriginalEdges(), true);

    if (bypasses == null || bypasses.size() == 0) {
      return dijkstraBase(graph, start, end);
    }

    if (bypasses.contains(start) || bypasses.contains(end)) {
      System.out.println("We cannot remove the starting node or the destination node.");
      return new SmartGraph(graph, null);
    }

    for (Node bypass : bypasses) {
      if (graph.hasNode(bypass)) {
        graph.removeNode(bypass);
        graph.removeEdges(bypass);
      } else {
        System.out.println(String.format("The node <%s> is not in the graph.", bypass.getName()));
      }
    }

    SmartGraph sm =  dijkstraBase(graph, start, end);
    sm.setGraph(originalGraph);
    return sm;
  }

  public SmartGraph dijkstraMustGo(Graph graph, Node start, Node end, List<Node> mustGo) {

    if (mustGo.size() == 0 || mustGo == null) {
      return dijkstraBase(graph, start, end);
    }

    for (Node node : mustGo) {
      if (node.equals(start) || node.equals(end)) {
        System.out.println("Please try nodes except the start node and the end node.");
        return new SmartGraph(graph, null);
      }
    }

    mustGo.add(end);
    double total = 0.0;
    StringBuilder path = new StringBuilder();
    Node next;
    List<Node> temp;

    for (int i = 0; i < mustGo.size(); i++) {
      next = mustGo.get(i);
      dijkstraBase(graph, start, next);
      total += getCost();
      temp = getPath();
      for (Node node : temp) {
        path.append(node.getName() + " ");
      }
      start = next;
    }

    char[] p = path.toString().replaceAll(" ", "").toCharArray();
    List<Character> list = new ArrayList<>();
    for (char c : p) {
      list.add(c);
    }
    List<Character> res = list.stream().distinct().collect(Collectors.toList());

    System.out.println("The lowest cost is: " + total);
    System.out.println("The shortest path is: " + res);

    List<Node> pathList = new ArrayList<>();
    for(char c: res) {
      pathList.add(new Node(String.valueOf(c)));
    }

    return new SmartGraph(graph, pathList);
  }
}