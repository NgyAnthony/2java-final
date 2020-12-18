import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Duck extends Thread implements PositionNode {
    protected final static Board board = Board.getInstance();
    protected final static Collider collider = new Collider();
    protected final static DuckLifetime duckLifetime = new DuckLifetime();
    protected int x;
    protected int y;
    protected int weight;
    protected boolean eating;
    protected Timer starveTimer;
    protected Timer moveTimer;
    protected Timer hearingTimer;
    protected Timer resetLeaderTimer;
    private HeadDuck leader;
    private final int xGameSize = 600; // CONFIGURABLE: taille X du jeu/canvas
    private final int yGameSize = 600; // CONFIGURABLE: taille Y du jeu/canvas
    protected final int starvePeriod = 10000; // CONFIGURABLE: perte de "1" de poids toutes les X secondes
    protected final int resetLeaderPeriod = 2000; // CONFIGURABLE: se détache du leader après X secondes
    protected final int movePeriod = 100; // CONFIGURABLE: bouge toutes les X secondes
    protected final int hearPeriod = 100; // CONFIGURABLE: vérifie si un chef siffle toutes les X secondes
    protected final int initialWeight = 10; // CONFIGURABLE: Poids initial
    protected final int promoteWeight = 15; // CONFIGURABLE: Poids à atteindre pour devenir chef
    protected final int xHearingRange = 200; // CONFIGURABLE: Distance x à laquelle un canard peut entendre un sifflement
    protected final int yHearingRange = 200; // CONFIGURABLE: Distance y à laquelle un canard peut entendre un sifflement

    public Duck(int x, int y) {
        this.x = x;
        this.y = y;
        weight = initialWeight;
        // Bool flag to determine if the duck is eating, used by UI
        eating = false;

        // Timers are used to call functions every X seconds
        moveTimer = new Timer();
        starveTimer = new Timer();
        hearingTimer = new Timer();
        resetLeaderTimer = new Timer();

        // No leader yet
        leader = null;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        // Do not set value if there is an obstacle in the way or if it's beyond the game board.
        if (xGameSize > x & x > 0 & collider.pathIsClear(x, y)){
            this.x = x;
        }
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        // Do not set value if there is an obstacle in the way or if it's beyond the game board.
        if (yGameSize > y & y > 0 & collider.pathIsClear(x, y)){
            this.y = y;
        }
    }

    public HeadDuck getLeader() {
        return leader;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isEating() {
        return eating;
    }

    @Override
    public void run() {
        move();
        vision(false);
        starve();
        hearWhistle();
        resetLeader();
    }

    public void move() {
        moveTimer.schedule(
            new TimerTask() {
                @Override
                public void run() {
                    // There is no leader, just move randomly
                    if (leader == null){
                        Random random = new Random();

                        int stepx = random.nextInt(5) - random.nextInt(5);
                        int stepy = random.nextInt(5) - random.nextInt(5);

                        setX(getX() + stepx);
                        setY(getY() + stepy);
                    } else {
                        int stepx;
                        int stepy;

                        // Move in the direction of the leader
                        if (leader.getX() > getX()){
                            stepx = 3;
                        } else {
                            stepx = -3;
                        }

                        if (leader.getY() > getY()){
                            stepy = 3;
                        } else {
                            stepy = -3;
                        }

                        setX(getX() + stepx);
                        setY(getY() + stepy);
                    }
                }
            }, 0, movePeriod
        );
    }

    // Everytime the duck moves, he also uses his eyes to see if there is a water lily to eat.
    public void vision(boolean isHeadDuck){
        moveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    findAndEatWaterLily(isHeadDuck);
                    eating = false;
                } catch (InterruptedException ignored) {
                    System.out.println("Exception");
                }
            }
        }, 0, movePeriod);
    }

    public void addWeight(int additionalWeight){
        weight += additionalWeight;
    }

    public void promote(){
        // Promote the duck if he gets to the promotion weight
        if (weight >= promoteWeight){
            // Stop moving
            moveTimer.cancel();
            duckLifetime.promoteDuck(this);
        }
    }

    public void loseWeight(){
        // Losing one of eight during a single event
        weight -= 1;

        // If the duck has no weight, he dies.
        if (weight <= 0){
            // Stop moving
            moveTimer.cancel();
            duckLifetime.killDuck(this);
        }
    }

    // Starvation makes you lose weight
    public void starve(){
        moveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                loseWeight();
            }
        }, 0, starvePeriod);
    }

    // The duck needs to eat to avoid dying
    public void findAndEatWaterLily(boolean isHeadDuck) throws InterruptedException {
        if (isHeadDuck || weight < promoteWeight){
            // Find out if there is a water lily nearby
            if (collider.waterLilyIsNear(getX(), getY())){
                // If there are, eat the closest one
                WaterLily waterLily = collider.getClosestWaterLily(getX(), getY());
                // There is a water lily, start eating
                if (waterLily != null){
                    eating = true;

                    // Consume the water lily
                    int weightAdded = waterLily.eatWaterLily();
                    addWeight(weightAdded);

                    // Don't try to promote if the duck is a head
                    if (!isHeadDuck){
                        promote();
                    }
                }
            }
        }
    }

    // Our duck stops paying attention to the leader after a reset period
    public void resetLeader(){
        resetLeaderTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                leader = null;
            }
        }, 0, resetLeaderPeriod);
    }

    // Le canard à une connaissance de tous les chefs. Il regarde si le boolean "siffle" est actif
    public void hearWhistle() {
        hearingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Liste qui contient tous les chefs qui sifflent
                BlockingQueue<HeadDuckWithDelta> hearingHeads = new LinkedBlockingDeque<>();

                // Get all the head ducks on the board
                for (HeadDuck headDuck: board.getHeadDucks()){

                    // Figure out how far the head is from the duck
                    int xProximity = Math.abs(headDuck.getX() - getX());
                    int yProximity = Math.abs(headDuck.getY() - getY());

                    // The duck is whistling and is within hearing range
                    if (headDuck.isWhistling() &
                        xProximity <= xHearingRange & yProximity <= yHearingRange){

                        // Figure out its position relative to the current duck
                        int delta = collider.getDeltaToHeadDuck(headDuck, getX(), getY());
                        HeadDuckWithDelta headDuckWithDelta = new HeadDuckWithDelta(headDuck, delta);

                        // Add to the list of heads along with delta
                        hearingHeads.add(headDuckWithDelta);
                    }
                }

                // Determine from the list of whistling heads which one is the closest and make him the leader
                for (HeadDuckWithDelta headDuckWithDelta : hearingHeads){
                    if (leader == null){
                        leader = headDuckWithDelta.getHeadDuck();
                    } else {
                        if (headDuckWithDelta.getDelta() < collider.getDeltaToHeadDuck(leader, getX(), getY())){
                            leader = headDuckWithDelta.getHeadDuck();
                        }
                    }
                }
            }
        }, 0, hearPeriod);
    }
}

// Classe permettant de stocker un chef et le delta (distance entre le canard et le chef)
class HeadDuckWithDelta {
    private HeadDuck headDuck;
    private int delta;

    public HeadDuckWithDelta(HeadDuck headDuck, int delta) {
        this.headDuck = headDuck;
        this.delta = delta;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public HeadDuck getHeadDuck() {
        return headDuck;
    }

    public void setHeadDuck(HeadDuck headDuck) {
        this.headDuck = headDuck;
    }
}
