import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Edge;
import models.Graph;
import models.Node;
import models.SmartGraph;

/**
 * Service Provider is the start point for invoking DijkstraService services.
 */
public class ServiceProvider {

  /**
   * Generate shortest paths for given problem.
   * @param option the problem no.
   * @return the smartgraph object consists of graph and path
   */
  public SmartGraph run(ProblemType option) {

    Node zero = new Node("0");
    Node one = new Node("1");
    Node two = new Node("2");
    Node three = new Node("3");
    Node four = new Node("4");
    Node five = new Node("5");
    Node six = new Node("6");

    Edge zero_one = new Edge(zero, one, 8);
    Edge zero_two = new Edge(zero, two, 20);
    Edge one_two = new Edge(one, two, 7);
    Edge one_three = new Edge(one, three, 3);
    Edge one_four = new Edge(one, four, 8);
    Edge two_four = new Edge(two, four, 9);
    Edge three_four = new Edge(three, four, 5);
    Edge three_five = new Edge(three, five, 2);
    Edge four_six = new Edge(four, six, 6);
    Edge five_four = new Edge(five, four, 1);
    Edge five_six = new Edge(five, six, 8);

    Graph graph = new Graph(true);
    graph.addNodes(zero, two, three, four, five, six);
    graph.addEdges(zero_one, zero_two, one_two, one_three, one_four, two_four, three_four, three_five, four_six, five_four, five_six);

    DijkstraService d = new DijkstraService();
    DijkstraAppsService appsService = new DijkstraAppsService();


    if(option == ProblemType.DIJKSTRA_BASE) {
      System.out.println("================== Base Dijkstra ===========================");

      // expected output:
      // the path costs: 20.0
      // the path is: 0 1 3 5 4 6
      return d.dijkstraBase(graph, zero, six);
    }

    else if(option == ProblemType.MUST_GO) {
      System.out.println("============== Dijkstra with Must Go Certain Nodes ==============");
      List<Node> mustGo = new ArrayList<>();
      mustGo.add(two);
      return d.dijkstraMustGo(graph, zero, six, mustGo);

    }
    else if(option == ProblemType.BYPASSING) {
      System.out.println("============== Dijkstra with Bypassing Certain Nodes ==============");
      List<Node> bypasses = new ArrayList<>();
      bypasses.add(one);
      return d.dijkstraBypass(graph, zero, six, bypasses);
    }

    else if(option == ProblemType.CHEAPEST_FLIGHTS) {

      System.out.println("================== Find Cheapest Price ======================");

      Set<Node> cities = new HashSet<>(Arrays.asList(zero, one, two, three, four));

      List<Edge> flights = new ArrayList<>();
      flights.add(new Edge(zero, one, 100));
      flights.add(new Edge(zero, two, 500));
      flights.add(new Edge(one, two, 250));
      flights.add(new Edge(one, three, 120));
      flights.add(new Edge(two, four, 50));
      flights.add(new Edge(three, zero, 120));
      flights.add(new Edge(three, two, 150));

      //expected path is: 0-1-2-4, price is 400
      return appsService.findCheapestPrice(cities, flights, zero, four, 2);
    }

    else if(option == ProblemType.NETWORK_DELAY) {

      System.out.println("================== Network Delay Time ======================");

      Set<Node> nodes = new HashSet<>(Arrays.asList(zero, one, two, three, four, five, six));
      List<Edge> times = new ArrayList<>();
      times.add(new Edge(zero, one, 1));
      times.add(new Edge(zero, two, 2));
      times.add(new Edge(one, three, 99));
      times.add(new Edge(three, four, 2));
      times.add(new Edge(four, five, 4));
      times.add(new Edge(four, six, 4));
      times.add(new Edge(six, three, 1));
      times.add(new Edge(two, four, 3));

      return appsService.networkDelayTime(nodes, times, zero);
    }

    else if(option == ProblemType.WIDEST_PATH) {

      System.out.println("======= The Widest Path Between Two Hosts ========");

      Set<Node> routers = new HashSet<>();
      routers.add(zero);
      routers.add(one);
      routers.add(two);
      routers.add(three);
      routers.add(four);

      List<Edge> bandwidths = new ArrayList<>();
      bandwidths.add(zero_one);
      bandwidths.add(zero_two);
      bandwidths.add(one_two);
      bandwidths.add(one_three);
      bandwidths.add(one_four);
      bandwidths.add(two_four);
      bandwidths.add(three_four);

      //Expected output:
      //The largest bandwidth is :9.0
      //It is path is: 0 2 4
      return appsService.widestPath(routers, bandwidths, zero, four);
    }

    return null;
  }

}
