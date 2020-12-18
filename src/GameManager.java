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
    private final int ducksNumber = 20; // CONFIGURABLE: Nombre de canards
    private final int rocksNumber = 10; // CONFIGURABLE: Nombre d'obstacles
    private final int waterLiliesNumber = 50; // CONFIGURABLE: Nombre de nénuphares
    private final int xWindowSize = 610; // CONFIGURABLE: taille X de la fenêtre
    private final int yWindowSize = 610; // CONFIGURABLE: taille Y de la fenêtre
    private final int xGameSize = 600; // CONFIGURABLE: taille X du jeu/canvas
    private final int yGameSize = 600; // CONFIGURABLE: taille X du jeu/canvas

    public GameManager() throws HeadlessException {
        // Set window size
        setSize(xWindowSize,yWindowSize);

        // Random is used for the spawn methods
        random = new Random();

        // Spawn objects before ducks
        SpawnRocks();
        SpawnWaterLilies();
        SpawnDucks();

        // Timer used to check the state of the board
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

    // Returns a random point in the game board
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

            // Duck is a thread, need to start it
            duck.start();
        }
    }

    private void SpawnWaterLilies() {
        // While loop makes sure there are always the correct number of water lilies on the board
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
            // Re-spawn water lilies if some have been eaten
            SpawnWaterLilies();
        }
    }
}
