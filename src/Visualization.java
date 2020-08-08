import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Edge;
import models.Graph;
import models.Node;
import models.SmartGraph;

/**
 * This class illustrates visualization for dijkstra and dijkstra apps algorithm. Run this
 * application and enter the case option to visualize. The visualization makes use of Java FX Smart
 * Graph library.
 */
public class Visualization extends Application {

  /**
   * Start point of the application
   */
  @Override
  public void start(Stage ignored) {

    ServiceProvider serviceProvider = new ServiceProvider();
    ProblemType option = getUserInput();
    if (option != null) {
      SmartGraph smartGraph = serviceProvider.run(option);
      show(option.getValue(), smartGraph.getGraph(), smartGraph.getPath());
    }
  }

  /**
   * Get user input option for problem no.
   * @return problem no.
   */
  private ProblemType getUserInput() {
    ProblemType[] options = ProblemType.values();
    Scanner reader = new Scanner(System.in);
    for (int i = 0; i < options.length; i++) {
      System.out.println(i + ". " + options[i].getValue());
    }
    String str = "";
    while (str.length() != 1 || !Character.isDigit(str.charAt(0)) || str.charAt(0) - '0' >= options.length) {
      System.out.println("\nChoose from above options to visualize:");
      str = reader.nextLine();
    }
    reader.close();
    System.out.println();
    return options[str.charAt(0) - '0'];
  }

  /**
   * Open a window for visualization the graph and shortest path.
   * @param title the specific problem name
   * @param graph the graph
   * @param path the shortest path
   */
  @FXML
  private void show(String title, Graph graph, List<Node> path) {

    SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
    SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(generateDigraph(graph), strategy);

    //change color for shortest path
    if (path != null) {
      graphView.getStylableVertex(path.get(0).getName()).setStyleClass("myVertex");
      for (int i = 1; i < path.size(); i++) {
        String edgeName = path.get(i - 1).getName() + path.get(i).getName();
        graphView.getStylableEdge(edgeName).setStyleClass("myEdge");
        graphView.getStylableVertex(path.get(i).getName()).setStyleClass("myVertex");
      }
    }

    Scene scene = new Scene(graphView, 800, 600);
    Stage stage = new Stage(StageStyle.DECORATED);
    stage.setTitle(title);
    stage.setScene(scene);
    stage.show();
    graphView.init();

  }

  /**
   * Generate directed graph used in visualization.
   * @param graph the graph
   * @return the generated digraph
   */
  private Digraph<String, String> generateDigraph(Graph graph) {

    Digraph<String, String> g = new DigraphEdgeList<>();

    for (Node n : graph.getNodes()) {
      g.insertVertex(n.getName());
    }
    for (Edge e : graph.getOriginalEdges()) {
      String source = e.getSourceNode().getName();
      String dest = e.getDestNode().getName();
      g.insertEdge(source, dest, source + dest, (int) e.getWeight());
    }

    return g;
  }

  public static void main(String[] args) {
      launch(args);
    }
}
