public class DuckLifetime {
    private final static Board board = Board.getInstance();

    public void killDuck(Duck duck){
        board.removeDuck(duck);
    }

    public void promoteDuck(Duck duck){
        HeadDuck headDuck = new HeadDuck(duck.getX(), duck.getY());
        headDuck.setWeight(duck.getWeight());
        board.addHeadDuck(headDuck);
        board.removeDuck(duck);
    }
}
