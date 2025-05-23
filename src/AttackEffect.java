import java.awt.*;

public class AttackEffect {
    private GraphNode node;
    private long createdAt;
    private final long duration = 300; // ms

    public AttackEffect(GraphNode node) {
        this.node = node;
        this.createdAt = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - createdAt > duration;
    }

    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.white);
        int x = node.col * tileSize;
        int y = node.row * tileSize;
        g.drawOval(x + tileSize / 4, y + tileSize / 4, tileSize / 2, tileSize / 2);
    }
}
