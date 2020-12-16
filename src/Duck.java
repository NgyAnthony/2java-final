import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Duck extends Thread implements PositionNode{
    private int x;
    private int y;
    private final int maxX = 500;
    private final int maxY = 500;

    public Duck(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        if (maxX > x & x > 0){
            this.x = x;
        }
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        if (maxY > y & y > 0){
            this.y = y;
        }
    }

    @Override
    public void run() {
        move();
    }

    public void move() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();

                int stepx = random.nextInt(5) - random.nextInt(5);
                int stepy = random.nextInt(5) - random.nextInt(5);

                setX(getX() + stepx);
                setY(getY() + stepy);
            }
        }, 0, 100);
    }

    // Make duck eat, manage thread access to waterlily
    // Make duck whistle
    // Make duck bump on obstacles


}
