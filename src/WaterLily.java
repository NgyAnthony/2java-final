import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaterLily implements PositionNode{
    private int x;
    private int y;
    private final Lock verrou = new ReentrantLock();
    private int consumable;
    private final int oneBite = 1;
    private final int consumableQty = 5;
    private final static Board board = Board.getInstance();

    public int getConsumable() {
        return consumable;
    }

    public WaterLily(int x, int y) {
        this.x = x;
        this.y = y;
        consumable = consumableQty;
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
