import java.util.ArrayList;

public class Collider {
    private final static Board board = Board.getInstance();

    public boolean pathIsClear(int x, int y) {
        ArrayList<Rock> rocks = board.getRocks();
        boolean pathClear = true;

        for(Rock rock : rocks) {
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
        ArrayList<WaterLily> waterLilies = board.getWaterLilies();
        boolean isNear = false;

        for(WaterLily waterLily : waterLilies) {
            int xProximity = Math.abs(waterLily.getX() - x);
            int yProximity = Math.abs(waterLily.getY() - y);
            if (xProximity < 10 & yProximity < 10){
                isNear = true;
                break;
            }
        }

        return isNear;
    }

    public WaterLily getClosestWaterLily(int x, int y){
        ArrayList<WaterLily> waterLilies = board.getWaterLilies();
        WaterLily target = null;
        for(WaterLily waterLily : waterLilies) {
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
