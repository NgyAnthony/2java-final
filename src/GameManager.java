import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameManager extends JFrame {
    private final static Board board = Board.getInstance();
    private final Random random;
    private final int ducksNumber = 25;
    private final int rocksNumber = 10;

    public GameManager() throws HeadlessException {
        setSize(550,550);
        random = new Random();
        SpawnDucks();
        SpawnRocks();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GameManager manager = new GameManager();
            manager.add(board);
            manager.setVisible(true);
        });
    }

    private int getRandomPositionInBoard(){
        return Math.abs(random.nextInt(500) - random.nextInt(500));
    }

    private void SpawnDucks() {
        for (int i = 0; i <= ducksNumber; i += 1) {
            int xSpawn = getRandomPositionInBoard();
            int ySpawn = getRandomPositionInBoard();
            Duck duck = new Duck(xSpawn, ySpawn);
            board.addDuck(duck);
            duck.start();
        }
    }

    private void SpawnRocks() {
        for (int i = 0; i <= rocksNumber; i += 1) {
            int xSpawn = getRandomPositionInBoard();
            int ySpawn = getRandomPositionInBoard();
            Rock rock = new Rock(xSpawn, ySpawn);
            board.addRock(rock);
        }
    }
}
