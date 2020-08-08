import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import models.Edge;
import models.Graph;
import models.Node;
import models.SmartGraph;

/**
 * Three Dijkstra algorithm life applications.
 * - The Widest Path Between Two Hosts
 * - The Cheapest Flight Within X Stops
 * - Network Delay Time
 */
public class DijkstraAppsService {
  /**
   * Find the largest bottleneck of the given network and its path.
   * @param routers the given network of routers
   * @param bandwidths bandwidths between different routers
   * @param start the starting router
   * @param end the ending router
   */
  public SmartGraph widestPath(Set<Node> routers, List<Edge> bandwidths, Node start, Node end) {
    if (!routers.contains(start) || !routers.contains(end)) {
      System.out.println("Please designate a starting router and an ending router.");
      return null;
    }

    Graph graph = new Graph(routers, bandwidths, true);
    Map<List<Node>, Double> paths = new HashMap<>();
    Map<Double, List<Node>> sortedPaths = new LinkedHashMap<>();

    findWidestPath(start, end, Double.MAX_VALUE, new ArrayList<Node>(), paths);
    paths.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .forEachOrdered(x -> sortedPaths.put(x.getValue(), x.getKey()));

    for (Map.Entry<Double, List<Node>> entry: sortedPaths.entrySet()) {
      double cost = entry.getKey();
      List<Node> path = entry.getValue();
      path.add(0, start);

      System.out.println("The largest bandwidth is:" + cost);
      System.out.print("It is path is: ");
      for (Node node : path) {
        System.out.print(node.getName() + " ");
      }

      return new SmartGraph(graph, path);
    }
    return new SmartGraph(graph, null);
  }

  private static void findWidestPath(Node start, Node end, double bandwidth, List<Node> path, Map<List<Node>, Double> paths) {
    if (start.equals(end)) {
      paths.put(new ArrayList<>(path), bandwidth);
      return;
    }
    List<Edge> edges = start.getEdges();
    for (Edge edge: edges) {
      Node dest = edge.getDestNode();
      double weight = edge.getWeight();
      bandwidth = weight < bandwidth ? weight : bandwidth;
      path.add(dest);
      start = dest;
      findWidestPath(start, end, bandwidth, path, paths);
      path.remove(path.size() - 1);
      bandwidth = Double.MAX_VALUE;
    }
  }

  /**
   * Given a network of N nodes and signal travel times from one to another, we can evaluate the
   * efficiency of the network by calculating the minimum network delay time for all nodes to
   * receive a signal sent by a certain node.
   */
  public SmartGraph networkDelayTime(Set<Node> nodes, List<Edge> times, Node src) {

    int M = nodes.size();
    double[][] network = new double[M][M];
    for (double[] arr : network) {
      Arrays.fill(arr, Double.MAX_VALUE);
    }

    for (Edge e: times) {
      network[Integer.valueOf(e.getSourceNode().getName())][Integer.valueOf(e.getDestNode().getName())] = e.getWeight();
    }

    double[] dis = new double[M];
    Arrays.fill(dis, Double.MAX_VALUE);
    int start = Integer.valueOf(src.getName());
    dis[start] = 0;

    boolean[] visited = new boolean[M];
    PriorityQueue<Route> queue = new PriorityQueue<>();
    queue.offer(new Route(src, Arrays.asList(src), 0));

    List<Node> path = new ArrayList<>();
    double totalTime = 0;
    while (!queue.isEmpty()) {
      Route r = queue.poll();
      int u = Integer.valueOf(r.node.getName());
      if (!visited[u]) {
        visited[u] = true;
        for (int i = 0; i < M; i++) {
          if (network[u][i] < Double.MAX_VALUE) {
            if (!visited[i] && dis[u] < Double.MAX_VALUE && dis[i] > dis[u] + network[u][i]) {
              dis[i] = dis[u] + network[u][i];
              Node n = new Node(String.valueOf(i));
              List<Node> newPath = new ArrayList<>(r.path);
              newPath.add(n);
              queue.offer(new Route(n, newPath, dis[i]));
            }
          }
        }
      }
      if(queue.isEmpty()) {
        path = r.path;
        totalTime = r.cost;
        break;
      }
    }

    //generate graph from nodes and times for visualization
    Graph graph = new Graph(nodes, times, true);

    for (int i = 0; i < M; i++) {
      if (dis[i] == Double.MAX_VALUE) {
        System.out.println("Could not reach all nodes from the source node.");
        return new SmartGraph(graph, null);
      }
    }

    System.out.println("Longest Path in the Network:");
    for (Node n : path) {
      System.out.print(n.getName() + " ");
    }
    System.out.println();

    System.out.println("Total Delay Time: " + Math.round(totalTime));

    return new SmartGraph(graph, path);
  }

  private class Route implements Comparable<Route> {

    double cost;
    Node node;
    List<Node> path;

    Route(Node node, List<Node> path, double cost) {
      this.node = node;
      this.path = path;
      this.cost = cost;
    }

    @Override
    public int compareTo(Route r) {
      return (int) (this.cost - r.cost);
    }

  }


  /**
   * Given a travel map with N cities connected by M flights. Each flight departs from one city and
   * arrives at another city with a price P. We can find the cheapest flight route from a departure
   * city to the destination city with up to X stops.
   */
  public SmartGraph findCheapestPrice(Set<Node> cities, List<Edge> flights, Node src, Node dst, int X) {

    //generate graph for visualization
    Graph graph = new Graph(cities, flights, true);

    int numOfCities = cities.size();
    double[][] srcToDst = new double[numOfCities][numOfCities];
    List<Node> path = new ArrayList<>();

    for (Edge edge : flights) {
      Node sourceNode = edge.getSourceNode();
      Node destNode = edge.getDestNode();
      srcToDst[Integer.valueOf(sourceNode.getName())][Integer.valueOf(destNode.getName())] = edge.getWeight();
    }

    PriorityQueue<City> minHeap = new PriorityQueue<>();
    minHeap.offer(new City(src, 0, 0));

    double[] cost = new double[numOfCities];
    Arrays.fill(cost, Double.MAX_VALUE);
    cost[Integer.valueOf(src.getName())] = 0;

    int[] stop = new int[numOfCities];
    Arrays.fill(stop, Integer.MAX_VALUE);
    stop[Integer.valueOf(src.getName())] = 0;

    while (!minHeap.isEmpty()) {
      City currCity = minHeap.poll();
      if (!path.contains(currCity.id)) {
        path.add(currCity.id);
      }

      if (currCity.id.getName().equals(dst.getName())) {
        System.out.println("The cheapest price is: " + currCity.costFromSrc);
        System.out.print("The path is: ");
        path.forEach(e -> System.out.print(e.getName() + " "));
        System.out.println();

        return new SmartGraph(graph, path);
      }

      if (currCity.disFromSrc == X + 1) {
        path.remove(currCity.id);
        continue;
      }

      double[] nexts = srcToDst[Integer.valueOf(currCity.id.getName())];
      for (int i = 0; i < numOfCities; i++) {
        if (nexts[i] != 0) {
          double newCost = currCity.costFromSrc + nexts[i];
          int newStop = currCity.disFromSrc + 1;
          if (newCost < cost[i]) {
            minHeap.offer(new City(new Node(String.valueOf(i)), newCost, newStop));
            cost[i] = newCost;
          } else if (newStop < stop[i]) {
            minHeap.offer(new City(new Node(String.valueOf(i)), newCost, newStop));
            stop[i] = newStop;
            path.remove(path.size() - 1);
          }
        }
      }
    }

    if (cost[Integer.valueOf(dst.getName())] == Double.MAX_VALUE) {
      System.out.println("Could not find cheapest price between source city and destination city.");
      return new SmartGraph(graph, null);
    }

    System.out.println("The cheapest price is: " + cost[Integer.valueOf(dst.getName())]);

    return new SmartGraph(graph, null);
  }

  private class City implements Comparable<City> {
    Node id;
    double costFromSrc;
    int disFromSrc;

    City(Node id, double costFromSrc, int disFromSrc) {
      this.id = id;
      this.costFromSrc = costFromSrc;
      this.disFromSrc = disFromSrc;
    }

    @Override
    public int compareTo(City o) {
      return (int) (this.costFromSrc - o.costFromSrc);
    }
  }
}
