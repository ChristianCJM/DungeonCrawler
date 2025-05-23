import java.awt.*;
import java.util.ArrayList;

public class Graph {
    private final int cols;
    private final int rows;
    private final GraphNode[][] nodes;

    public Graph(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        nodes = new GraphNode[cols][rows];
        generateGraph();
    }

    private void generateGraph() {
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                String type = (col == 0 || row == 0 || col == cols - 1 || row == rows - 1) ? "hard" : "empty";
                nodes[col][row] = new GraphNode(col, row, type);
            }
        }
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                GraphNode node = nodes[col][row];
                if (!node.type.equals("hard")) {
                    if (col > 0 && !nodes[col - 1][row].type.equals("hard")) node.addNeighbor(nodes[col - 1][row]);
                    if (col < cols - 1 && !nodes[col + 1][row].type.equals("hard")) node.addNeighbor(nodes[col + 1][row]);
                    if (row > 0 && !nodes[col][row - 1].type.equals("hard")) node.addNeighbor(nodes[col][row - 1]);
                    if (row < rows - 1 && !nodes[col][row + 1].type.equals("hard")) node.addNeighbor(nodes[col][row + 1]);
                }
            }
        }
    }

    public GraphNode getNode(int col, int row) {
        return nodes[col][row];
    }

    public void draw(Graphics g, int tileSize) {
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                nodes[col][row].draw(g, tileSize);
            }
        }
    }

    public ArrayList<GraphNode> getEmptyNodes() {
        ArrayList<GraphNode> result = new ArrayList<>();
        for (GraphNode[] row : nodes) {
            for (GraphNode node : row) {
                if (!node.type.equals("hard")) {
                    result.add(node);
                }
            }
        }
        return result;
    }

    public boolean areAdjacent(GraphNode a, GraphNode b) {
        return a.neighbors.contains(b);
    }

}
