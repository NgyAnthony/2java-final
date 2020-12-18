import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Duck extends Thread implements PositionNode {
    protected final static Board board = Board.getInstance();
    protected final static Collider collider = new Collider();
    protected final static DuckLifetime duckLifetime = new DuckLifetime();
    protected int x;
    protected int y;

    protected int weight;
    protected boolean eating;
    protected Timer starveTimer;
    protected Timer moveTimer;

    protected final int maxX = 500;
    protected final int maxY = 500;
    protected final int starve = 10000;
    protected final int move = 100;
    protected final int initialWeight = 10;
    protected final int promoteWeight = 15;

    public Duck(int x, int y) {
        this.x = x;
        this.y = y;
        weight = initialWeight;
        eating = false;

        moveTimer = new Timer();
        starveTimer = new Timer();
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isEating() {
        return eating;
    }

    @Override
    public void run() {
        move();
        vision(false);
        starve();
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

    public void move() {
        moveTimer.schedule(
            new TimerTask() {
                @Override
                public void run() {
                    Random random = new Random();

                    int stepx = random.nextInt(5) - random.nextInt(5);
                    int stepy = random.nextInt(5) - random.nextInt(5);

                    setX(getX() + stepx);
                    setY(getY() + stepy);
                }
            }, 0, move
        );
    }

    public void vision(boolean isHeadDuck){
        moveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    findAndEatWaterLily(isHeadDuck);
                    eating = false;
                } catch (InterruptedException ignored) {
                    System.out.println("Exception");
                }
            }
        }, 0, move);
    }

    public void addWeight(int additionalWeight){
        weight += additionalWeight;
    }

    public void promote(){
        if (weight >= promoteWeight){
            moveTimer.cancel();
            duckLifetime.promoteDuck(this);
        }
    }

    public void loseWeight(){
        weight -= 1;

        if (weight <= 0){
            moveTimer.cancel();
            duckLifetime.killDuck(this);
        }
    }

    public void starve(){
        moveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                loseWeight();
            }
        }, 0, starve);
    }

    public void findAndEatWaterLily(boolean isHeadDuck) throws InterruptedException {
        if (isHeadDuck || weight < promoteWeight){
            if (collider.waterLilyIsNear(getX(), getY())){
                WaterLily waterLily = collider.getClosestWaterLily(getX(), getY());
                if (waterLily != null){
                    eating = true;
                    int weightAdded = waterLily.eatWaterLily();
                    addWeight(weightAdded);
                    if (!isHeadDuck){
                        promote();
                    }
                }
            }
        }
    }
}
