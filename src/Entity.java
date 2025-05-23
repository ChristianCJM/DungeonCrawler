import java.awt.*;

public abstract class Entity {
    protected GraphNode currentNode;
    protected int hp = 100;

    public Entity(GraphNode startNode) {
        this.currentNode = startNode;
    }

    public GraphNode getCurrentNode() {
        return currentNode;
    }

    public abstract void update();

    public void draw(Graphics g, int tileSize, Color color) {
        g.setColor(color);
        g.fillRect(currentNode.col * tileSize, currentNode.row * tileSize, tileSize, tileSize);
    }

    public abstract void draw(Graphics g, int tileSize);
}