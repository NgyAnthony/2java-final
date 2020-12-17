import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Duck extends Thread implements PositionNode{
    private final static Board board = Board.getInstance();
    private final static Collider collider = new Collider();
    private int x;
    private int y;
    private final int maxX = 500;
    private final int maxY = 500;
    private int weight;
    private boolean eating;

    public boolean isEating() {
        return eating;
    }

    public Duck(int x, int y) {
        this.x = x;
        this.y = y;
        weight = 0;
        eating = false;
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

    private void move() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
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
                } catch (InterruptedException e) {
                    System.out.println("Water Lily has been eaten by another duck !");
                }
            }
        }, 0, 100);
    }

    private void addWeight(int additionalWeight){
        weight += additionalWeight;
    }

    private void findAndEatWaterLily() throws InterruptedException {
        if (collider.waterLilyIsNear(getX(), getY())){
            WaterLily waterLily = collider.getClosestWaterLily(getX(), getY());
            if (waterLily != null){
                eating = true;
                int weightAdded = waterLily.eatWaterLily();
                addWeight(weightAdded);
            }
        }
    }

    // Make duck whistle
}
