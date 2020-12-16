import javax.swing.*;
import java.awt.*;

public class GameManager extends JFrame {
    public GameManager() throws HeadlessException {
        setSize(550,550);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GameManager manager = new GameManager();
            Board board = new Board();
            manager.add(board);
            manager.setVisible(true);

            // Test
            for (int i = 0; i <= 25; i += 1) {
                Duck duck = new Duck(250, 250);
                board.addDuck(duck);
                duck.start();
            }

            // board.addHeadDuck(new HeadDuck(20, 20));
            // board.addWaterLily(new WaterLily(10, 10));
            // board.addRock(new Rock(60, 60));
        });
    }
}
