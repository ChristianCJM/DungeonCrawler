import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    final int tileSize = 32;
    final int maxCol = 30;
    final int maxRow = 20;
    final int screenWidth = tileSize * maxCol;
    final int screenHeight = tileSize * maxRow + 20;

    Thread gameThread;
    Player player;
    Graph dungeonGraph;
    ArrayList<Enemy> enemies = new ArrayList<>();

    long lastEnemySpawn = 0;
    long enemySpawnInterval = 5000;
    boolean attackPressed = false;
    private boolean gameOver = false;

    private ArrayList<Item> items = new ArrayList<>();
    private long lastItemSpawnTime = System.currentTimeMillis();
    private long itemSpawnInterval = 6000;

    private final Set<Integer> pressedKeys = new HashSet<>();
    private ArrayList<AttackEffect> attackEffects = new ArrayList<>();

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);
        dungeonGraph = new Graph(maxCol, maxRow);
        player = new Player(dungeonGraph.getNode(5, 5));
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (!gameOver) {
            update();
            repaint();
            try {
                Thread.sleep(250); // control speed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("GAME OVER");
    }


    private void update() {
        player.update();

        for (Enemy e : enemies) {
            e.update(player.getCurrentNode());
            if (e.getCurrentNode() == player.getCurrentNode()) {
                player.takeDamage(1);
                break;
            }
        }

        long now = System.currentTimeMillis();
        if (now - lastEnemySpawn > enemySpawnInterval) {
            spawnEnemy();
            lastEnemySpawn = now;
            if (enemySpawnInterval > 2000) {
                enemySpawnInterval -= 300;
            }
        }

        if (attackPressed) {
            Iterator<Enemy> iter = enemies.iterator();
            GraphNode playerNode = player.getCurrentNode();
            while (iter.hasNext()) {
                Enemy enemy = iter.next();
                if (isNeighbor(playerNode, enemy.getCurrentNode())) {
                    iter.remove();
                }
            }
            attackPressed = false;
        }

        if (System.currentTimeMillis() - lastItemSpawnTime >= itemSpawnInterval) {
            spawnItem();
            lastItemSpawnTime = System.currentTimeMillis();
            if (itemSpawnInterval > 3000) itemSpawnInterval += 300;
        }

        Iterator<Item> itemIterator = items.iterator();
        while (itemIterator.hasNext()) {
            Item item = itemIterator.next();
            if (item.getNode() == player.getCurrentNode()) {
                switch (item.getType()) {
                    case "hp" -> player.restoreHp(1);
                    case "mana" -> player.restoreMana(1);
                }
                itemIterator.remove();
            }
        }

        attackEffects.removeIf(AttackEffect::isExpired);

        if (player.isDead()) {
            gameOver = true;
        }

    }

    private boolean isNeighbor(GraphNode a, GraphNode b) {
        return a.neighbors.contains(b);
    }

    private void spawnEnemy() {
        GraphNode node;
        do {
            node = dungeonGraph.getNode((int)(Math.random() * maxCol), (int)(Math.random() * maxRow));
        } while (node.type.equals("hard"));
        enemies.add(new Enemy(node));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        dungeonGraph.draw(g, tileSize);
        player.draw(g, tileSize);
        drawHUD(g);
        for (Enemy e : enemies) {
            e.draw(g, tileSize);
        }
        for (Item item : items) {
            item.draw(g, tileSize);
        }
        for (AttackEffect effect : attackEffects) {
            effect.draw(g, tileSize);
        }

        g.dispose();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            eliminateNearbyEnemies();
        }
    }

    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void spawnItem() {
        ArrayList<GraphNode> available = dungeonGraph.getEmptyNodes();
        if (available.isEmpty()) return;

        GraphNode node = available.get((int) (Math.random() * available.size()));
        String type = Math.random() < 0.5 ? "hp" : "mana";
        items.add(new Item(node, type));
    }

    private void eliminateNearbyEnemies() {
        if (player.getMana() <= 0) return;

        boolean attacked = false;
        Iterator<Enemy> iter = enemies.iterator();
        while (iter.hasNext()) {
            Enemy e = iter.next();
            if (dungeonGraph.areAdjacent(player.getCurrentNode(), e.getCurrentNode())) {
                attackEffects.add(new AttackEffect(e.getCurrentNode()));
                iter.remove();
                attacked = true;
            }
        }

        if (attacked) {
            player.useMana(1);
        }
    }

    private void drawHUD(Graphics g) {
        int x = 10;
        int y = getHeight() - 40;
        int boxSize = 20;

        // Draw HP
        g.setColor(Color.white);
        g.drawString("HP:", x, y - 5);
        for (int i = 0; i < player.getHp(); i++) {
            g.setColor(Color.green);
            g.fillRect(x + 30 + i * (boxSize + 5), y - 15, boxSize, boxSize);
        }

        // Draw Mana
        g.setColor(Color.white);
        g.drawString("Mana:", x, y + 25);
        for (int i = 0; i < player.getMana(); i++) {
            g.setColor(Color.cyan);
            g.fillRect(x + 50 + i * (boxSize + 5), y + 10, boxSize, boxSize);
        }
    }

}
