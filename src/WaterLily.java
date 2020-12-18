import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaterLily implements PositionNode{
    private final static Board board = Board.getInstance();
    private final Lock verrou = new ReentrantLock();
    private int x;
    private int y;
    private int consumable;

    private final int oneBite = 1; // CONFIGURABLE
    private final int consumableQty = 5; // CONFIGURABLE

    public WaterLily(int x, int y) {
        this.x = x;
        this.y = y;
        consumable = consumableQty;
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

    public int getConsumable() {
        return consumable;
    }

    public int eatWaterLily() throws InterruptedException {
        verrou.lock();
        try {
            Thread.sleep(1000);
            consumable -= oneBite;
            return oneBite;
        } finally {
            if (consumable <= 0){
                board.removeWaterLily(this);
            }
            verrou.unlock();
        }
    }
}
