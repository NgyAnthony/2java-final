import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Collider {
    private final static Board board = Board.getInstance();
    private final Lock verrou = new ReentrantLock();
    private final Lock verrou2 = new ReentrantLock();

    public boolean pathIsClear(int x, int y) {
        boolean pathClear = true;

        for(Rock rock : board.getRocks()) {
            int xProximity = Math.abs(rock.getX() - x);
            int yProximity = Math.abs(rock.getY() - y);
            if (xProximity < 10 & yProximity < 10){
                pathClear = false;
                break;
            }
        }

        return pathClear;
    }

    public boolean waterLilyIsNear(int x, int y) {
        boolean isNear = false;

        for(WaterLily waterLily : board.getWaterLilies()) {
            if (waterLily != null){
                int xProximity = Math.abs(waterLily.getX() - x);
                int yProximity = Math.abs(waterLily.getY() - y);
                if (xProximity < 10 & yProximity < 10){
                    isNear = true;
                    break;
                }
            }
        }
        return isNear;
    }

    public WaterLily getClosestWaterLily(int x, int y){
        WaterLily target = null;
        for(WaterLily waterLily : board.getWaterLilies()) {
            int xProximity = Math.abs(waterLily.getX() - x);
            int yProximity = Math.abs(waterLily.getY() - y);
            if (xProximity < 10 & yProximity < 10){
                target = waterLily;
                break;
            }
        }
        return target;
    }
}
