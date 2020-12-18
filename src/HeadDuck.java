import java.util.Timer;
import java.util.TimerTask;

public class HeadDuck extends Duck{
    private boolean whistling;
    private Timer whistlingTimer;
    private final int whitlingPeriod = 10000; // CONFIGURABLE

    public boolean isWhistling() {
        return whistling;
    }

    public void setWhistling(boolean whistling) {
        this.whistling = whistling;
    }

    public HeadDuck(int x, int y) {
        super(x, y);
        whistlingTimer = new Timer();
        move();
        vision(true);
        starve();
        whistle();
    }

    private void whistle(){
        whistlingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                setWhistling(true);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setWhistling(false);
            }
        }, 0, whitlingPeriod);
    }
}
