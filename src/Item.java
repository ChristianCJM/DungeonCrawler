import java.awt.*;

public class Item {
    private GraphNode node;
    private String type; // "hp" or "mana"

    public Item(GraphNode node, String type) {
        this.node = node;
        this.type = type;
    }

    public GraphNode getNode() {
        return node;
    }

    public String getType() {
        return type;
    }

    public void draw(Graphics g, int tileSize) {
        switch (type) {
            case "hp" -> g.setColor(Color.pink);
            case "mana" -> g.setColor(Color.cyan);
        }
        g.fillOval(node.col * tileSize + tileSize/4, node.row * tileSize + tileSize/4, tileSize/2, tileSize/2);
    }
}
