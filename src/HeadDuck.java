public class HeadDuck extends Duck{
    public HeadDuck(int x, int y) {
        super(x, y);
        move();
        vision(true);
        starve();
    }
}
