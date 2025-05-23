import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Entity {
    private boolean up, down, left, right;
    private int hp = 5;
    private int mana = 3;

    public Player(GraphNode startNode) {
        super(startNode);
    }

    public int getHp() {
        return hp;
    }

    public int getMana() {
        return mana;
    }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp < 0) hp = 0;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public void useMana(int amount) {
        mana -= amount;
        if (mana < 0) mana = 0;
    }

    public void restoreHp(int amount) {
        hp += amount;
    }

    public void restoreMana(int amount) {
        mana += amount;
    }

    @Override
    public void update() {
        if (up && hasNeighbor(currentNode.col, currentNode.row - 1)) {
            currentNode = getNeighbor(currentNode.col, currentNode.row - 1);
            up = false;
        } else if (down && hasNeighbor(currentNode.col, currentNode.row + 1)) {
            currentNode = getNeighbor(currentNode.col, currentNode.row + 1);
            down = false;
        } else if (left && hasNeighbor(currentNode.col - 1, currentNode.row)) {
            currentNode = getNeighbor(currentNode.col - 1, currentNode.row);
            left = false;
        } else if (right && hasNeighbor(currentNode.col + 1, currentNode.row)) {
            currentNode = getNeighbor(currentNode.col + 1, currentNode.row);
            right = false;
        }
    }

    private boolean hasNeighbor(int col, int row) {
        for (GraphNode n : currentNode.neighbors)
            if (n.col == col && n.row == row) return true;
        return false;
    }

    private GraphNode getNeighbor(int col, int row) {
        for (GraphNode n : currentNode.neighbors)
            if (n.col == col && n.row == row) return n;
        return currentNode;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_D -> right = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = false;
            case KeyEvent.VK_S -> down = false;
            case KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_D -> right = false;
        }
    }

    public void draw(Graphics g, int tileSize) {
        super.draw(g, tileSize, Color.blue);
    }
}
