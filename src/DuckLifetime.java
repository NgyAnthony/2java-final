public class DuckLifetime {
    private final static Board board = Board.getInstance();

    public void killDuck(Duck duck){
        board.removeDuck(duck);
    }

    public void promoteDuck(Duck duck){
        board.addHeadDuck(new HeadDuck(duck.getX(), duck.getY()));
        board.removeDuck(duck);
    }
}
