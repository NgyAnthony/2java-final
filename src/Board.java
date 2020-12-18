import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Board extends JPanel implements ActionListener{
    private static final Board instance = new Board();
    private BlockingQueue<Duck> ducks;
    private BlockingQueue<HeadDuck> headDucks;
    private BlockingQueue<Rock> rocks;
    private BlockingQueue<WaterLily> waterLilies;
    private Timer timer;

    Board() {
        preparePool();
        // Timer for repaint
        timer = new Timer(50, this);
        timer.start();
    }

    public static Board getInstance()
    {
        return instance;
    }

    // Blocking queues for each element of our game
    // Using blocking queues to ensure we can access our lists with threads safely
    public BlockingQueue<Rock> getRocks() {
        return rocks;
    }

    public BlockingQueue<WaterLily> getWaterLilies() {
        return waterLilies;
    }

    public BlockingQueue<HeadDuck> getHeadDucks() {
        return headDucks;
    }

    // Add and remove items from Arrays
    public void addRock(Rock rock) {
        this.rocks.add(rock);
    }

    public void addWaterLily(WaterLily waterLily){
        this.waterLilies.add(waterLily);
    }

    public void removeWaterLily(WaterLily waterLily){
        this.waterLilies.remove(waterLily);
    }

    public void addHeadDuck(HeadDuck headDuck){
        this.headDucks.add(headDuck);
    }

    public void addDuck(Duck duck) {
        this.ducks.add(duck);
    }

    public void removeDuck(Duck duck){
        this.ducks.remove(duck);
    }

    // Init arrays
    private void preparePool() {
        ducks = new LinkedBlockingDeque<>();
        headDucks = new LinkedBlockingDeque<>();
        rocks = new LinkedBlockingDeque<>();
        waterLilies = new LinkedBlockingDeque<>();
    }

    // Drawing items
    public void drawHeadDucks(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        try {
            for(HeadDuck headDuck : headDucks) {
                int x = headDuck.getX();
                int y = headDuck.getY();
                Polygon poly = new Polygon(new int[] { x + 5, x + 10, x }, new int[] { y, y + 10, y + 10 }, 3);
                if (headDuck.isEating()){
                    g2d.setColor(new Color(231, 76, 60));
                } else if (headDuck.isWhistling()){
                    g2d.setColor(new Color(142, 68, 173));
                } else {
                    g2d.setColor(new Color(33, 97, 140));
                }

                if (headDuck.getWeight() > 0){
                    g2d.drawString(String.valueOf(headDuck.getWeight()), x , y);
                    g2d.fill(poly);
                }
            }
        } catch (NullPointerException e){
            System.out.println("Error: Head duck starved to death, can't draw it.");
        }
    }

    public void drawDucks(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        for(Duck duck : ducks) {
            int x = duck.getX();
            int y = duck.getY();

            Polygon poly = new Polygon(new int[] { x + 5, x + 10, x }, new int[] { y, y + 10, y + 10 }, 3);
            if (duck.isEating()){
                g2d.setColor(new Color(0, 255, 0));
            } else if (duck.getLeader() != null){
                g2d.setColor(new Color(210, 180, 222));
            } else {
                g2d.setColor(new Color(244, 208, 63));
            }

            g2d.drawString(String.valueOf(duck.getWeight()), x , y);
            g2d.fill(poly);
        }
    }

    public void drawRocks(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        for(Rock rock : rocks) {
            int x = rock.getX();
            int y = rock.getY();
            g2d.setColor(new Color(149, 165, 166));
            g2d.fillRect(x, y, 10, 10);
        }
    }

    public void drawWaterLilies(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        for(WaterLily waterLily : waterLilies) {
            int x = waterLily.getX();
            int y = waterLily.getY();
            g2d.setColor(new Color(34, 153, 84));
            g2d.drawString(String.valueOf(waterLily.getConsumable()), x , y);
            g2d.fillOval(x, y, 10, 10);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawDucks(g);
        drawHeadDucks(g);
        drawWaterLilies(g);
        drawRocks(g);
    }

    // Repaint every second
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==timer){
            repaint();
        }
    }
}
