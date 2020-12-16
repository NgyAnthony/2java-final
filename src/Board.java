import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener{
    private ArrayList<Duck> ducks;
    private ArrayList<HeadDuck> headDucks;
    private ArrayList<Rock> rocks;
    private ArrayList<WaterLily> waterLilies;
    private Timer timer;

    public void addRock(Rock rock) {
        this.rocks.add(rock);
    }

    public void addWaterLily(WaterLily waterLily){
        this.waterLilies.add(waterLily);
    }

    public void addHeadDuck(HeadDuck headDuck){
        this.headDucks.add(headDuck);
    }

    public void addDuck(Duck duck) {
        this.ducks.add(duck);
    }

    public void removeDucks(Duck duck){
        this.ducks.remove(duck);
    }

    private int poolWidth;
    private int poolHeight;

    private JLabel liliesAmountLabel;
    private JLabel ducksAmountLabel;
    private JLabel headDucksAmountLabel;

    Board() {
        preparePool();
        timer = new Timer(90, this);
        timer.start();
    }

    private void preparePool() {
        ducks = new ArrayList<>();
        headDucks = new ArrayList<>();
        rocks = new ArrayList<>();
        waterLilies = new ArrayList<>();
    }

    // Setters for the amount of each element
    public void setLiliesAmount(int liliesAmount) {
        this.liliesAmountLabel.setText(String.valueOf(liliesAmount));
    }

    public void setDucksAmount(int ducksAmount) {
        this.ducksAmountLabel.setText(String.valueOf(ducksAmount));
    }

    public void setHeadDucksAmount(int headDucksAmount) {
        this.headDucksAmountLabel.setText(String.valueOf(headDucksAmount));
    }

    // Drawing items
    public void drawHeadDucks(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        for(HeadDuck headDuck : headDucks) {
            int x = headDuck.getX();
            int y = headDuck.getY();
            Polygon poly = new Polygon(new int[] { x + 5, x + 10, x }, new int[] { y, y + 10, y + 10 }, 3);
            g2d.setColor(new Color(135, 54, 0  ));
            g2d.fill(poly);
        }
    }

    public void drawDucks(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        for(Duck duck : ducks) {
            int x = duck.getX();
            int y = duck.getY();

            Polygon poly = new Polygon(new int[] { x + 5, x + 10, x }, new int[] { y, y + 10, y + 10 }, 3);
            g2d.setColor(new Color(244, 208, 63));
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
