import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class GraphNode {
    public int col, row;
    public String type;
    public List<GraphNode> neighbors;

    public GraphNode(int col, int row, String type) {
        this.col = col;
        this.row = row;
        this.type = type;
        this.neighbors = new ArrayList<>();
    }

    public void addNeighbor(GraphNode node) {
        if (!neighbors.contains(node)) {
            neighbors.add(node);
        }
    }

    public void draw(Graphics g, int tileSize) {
        switch (type) {
            case "hard" -> g.setColor(Color.gray);
            case "soft" -> g.setColor(Color.lightGray);
            default -> g.setColor(Color.darkGray);
        }
        g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
    }
}
