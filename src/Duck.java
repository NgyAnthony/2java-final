import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Duck extends Thread implements PositionNode, ActionListener {
    private final static Board board = Board.getInstance();
    private final static Collider collider = new Collider();
    private final static DuckLifetime duckLifetime = new DuckLifetime();
    private final Timer moveTimer = new Timer();
    private int x;
    private int y;

    private int weight;
    private boolean eating;
    private final javax.swing.Timer timer;

    private final int maxX = 500;
    private final int maxY = 500;
    private final int starve = 10000;
    private final int initialWeight = 10;
    private final int promoteWeight = 15;

    public Duck(int x, int y) {
        this.x = x;
        this.y = y;
        weight = initialWeight;
        eating = false;

        // Remove 1 of weight every "starve" seconds
        timer = new javax.swing.Timer(starve, this);
        timer.start();
    }

    public int getWeight() {
        return weight;
    }

    public boolean isEating() {
        return eating;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        if (maxX > x & x > 0 & collider.pathIsClear(x, y)){
            this.x = x;
        }
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        if (maxY > y & y > 0 & collider.pathIsClear(x, y)){
            this.y = y;
        }
    }

    @Override
    public void run() {
        move();
    }

    void move() {
        moveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();

                int stepx = random.nextInt(5) - random.nextInt(5);
                int stepy = random.nextInt(5) - random.nextInt(5);

                setX(getX() + stepx);
                setY(getY() + stepy);

                try {
                    findAndEatWaterLily();
                    eating = false;
                } catch (InterruptedException ignored) {
                    System.out.println("Exception");
                }
            }
        }, 0, 100);
    }

    private void addWeight(int additionalWeight){
        weight += additionalWeight;
        if (weight >= promoteWeight){
            moveTimer.cancel();
            duckLifetime.promoteDuck(this);
        }
    }

    private void loseWeight(){
        weight -= 1;
        if (weight <= 0){
            moveTimer.cancel();
            duckLifetime.killDuck(this);
        }
    }

    private void findAndEatWaterLily() throws InterruptedException {
        if (collider.waterLilyIsNear(getX(), getY()) & weight < promoteWeight){
            WaterLily waterLily = collider.getClosestWaterLily(getX(), getY());
            if (waterLily != null){
                eating = true;
                int weightAdded = waterLily.eatWaterLily();
                addWeight(weightAdded);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==timer){
            loseWeight();
        }
    }

    // Make duck whistle
}
