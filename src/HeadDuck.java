import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HeadDuck extends Duck{
    public HeadDuck(int x, int y) {
        super(x, y);
        move();
        vision(true);
        starve();
    }
}
