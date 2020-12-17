import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaterLily implements PositionNode{
    private int x;
    private int y;
    private final Lock verrou = new ReentrantLock();
    private int consumable;
    private final int oneBite = 1;
    private final static Board board = Board.getInstance();

    public WaterLily(int x, int y) {
        this.x = x;
        this.y = y;
        consumable = 5;
    }

    public int eatWaterLily() throws InterruptedException {
        verrou.lock();
        try {
            Thread.sleep(1000);
            consumable -= oneBite;
            return oneBite;
        } finally {
            verrou.unlock();
            if (consumable <= 0){
                board.removeWaterLily(this);
            }
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }
}
