import java.awt.*;

public class Enemy extends Entity {

    private GraphNode targetPlayerNode;

    public Enemy(GraphNode startNode) {
        super(startNode);
    }

    // This is required to implement the abstract method from Entity
    @Override
    public void update() {
        if (targetPlayerNode != null) {
            moveToward(targetPlayerNode);
        }
    }

    public void update(GraphNode playerNode) {
        this.targetPlayerNode = playerNode;
        update(); // triggers actual movement logic
    }

    private void moveToward(GraphNode playerNode) {
        GraphNode closest = null;
        double minDist = Double.MAX_VALUE;

        for (GraphNode neighbor : currentNode.neighbors) {
            double dist = distance(neighbor, playerNode);
            if (dist < minDist) {
                minDist = dist;
                closest = neighbor;
            }
        }

        if (closest != null) {
            currentNode = closest;
        }
    }

    private double distance(GraphNode a, GraphNode b) {
        int dx = a.col - b.col;
        int dy = a.row - b.row;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public void draw(Graphics g, int tileSize) {
        super.draw(g, tileSize, Color.red);
    }
}
