import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameManager extends JFrame {
    private final static Board board = new Board();
    private final Random random;

    public GameManager() throws HeadlessException {
        setSize(550,550);
        random = new Random();
        SpawnDucks();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GameManager manager = new GameManager();
            manager.add(board);
            manager.setVisible(true);
        });
    }

    private void SpawnDucks() {
        for (int i = 0; i <= 25; i += 1) {
            int xSpawn = Math.abs(random.nextInt(500) - random.nextInt(500));
            int ySpawn = Math.abs(random.nextInt(500) - random.nextInt(500));
            Duck duck = new Duck(xSpawn, ySpawn);
            board.addDuck(duck);
            duck.start();
        }
    }
}
