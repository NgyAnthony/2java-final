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
            board.addHeadDuck(new HeadDuck(20, 20));
            board.addWaterLily(new WaterLily(10, 10));
            board.addRock(new Rock(60, 60));
            board.addDuck(new Duck(90, 90));
        });
    }
}
