import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameManager extends JFrame implements ActionListener {
    private final static Board board = Board.getInstance();
    private final static Collider collider = new Collider();
    private Timer timer;
    private final Random random;
    private final int ducksNumber = 20; // CONFIGURABLE
    private final int rocksNumber = 10; // CONFIGURABLE
    private final int waterLiliesNumber = 50; // CONFIGURABLE
    private final int xWindowSize = 610; // CONFIGURABLE
    private final int yWindowSize = 610; // CONFIGURABLE
    private final int xGameSize = 600; // CONFIGURABLE
    private final int yGameSize = 600; // CONFIGURABLE

    public GameManager() throws HeadlessException {
        setSize(xWindowSize,yWindowSize);
        random = new Random();
        SpawnRocks();
        SpawnWaterLilies();
        SpawnDucks();

        timer = new Timer(50, this);
        timer.start();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GameManager manager = new GameManager();
            manager.add(board);
            manager.setVisible(true);
        });
    }

    private int getRandomPositionInBoard(){
        return Math.abs(random.nextInt(xGameSize) - random.nextInt(yGameSize));
    }

    private void SpawnRocks() {
        for (int i = 0; i <= rocksNumber; i += 1) {
            int xSpawn = getRandomPositionInBoard();
            int ySpawn = getRandomPositionInBoard();
            Rock rock = new Rock(xSpawn, ySpawn);
            board.addRock(rock);
        }
    }

    private void SpawnDucks() {
        for (int i = 0; i <= ducksNumber; i += 1) {
            int xSpawn = getRandomPositionInBoard();
            int ySpawn = getRandomPositionInBoard();

            // Do not make ducks spawn on rocks
            while (!collider.pathIsClear(xSpawn, ySpawn)){
                xSpawn = getRandomPositionInBoard();
                ySpawn = getRandomPositionInBoard();
            }

            Duck duck = new Duck(xSpawn, ySpawn);
            board.addDuck(duck);
            duck.start();
        }
    }

    private void SpawnWaterLilies() {
        while(waterLiliesNumber != board.getWaterLilies().size()){
            int xSpawn = getRandomPositionInBoard();
            int ySpawn = getRandomPositionInBoard();

            // Do not make water lilies spawn on rocks
            while (!collider.pathIsClear(xSpawn, ySpawn)){
                xSpawn = getRandomPositionInBoard();
                ySpawn = getRandomPositionInBoard();
            }

            WaterLily waterLily = new WaterLily(xSpawn, ySpawn);
            board.addWaterLily(waterLily);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==timer){
            SpawnWaterLilies();
        }
    }
}
