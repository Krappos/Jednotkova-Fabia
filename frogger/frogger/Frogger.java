import java.util.*;
import javax.swing.Timer;

/**
 * Main Frogger game class
 */
public class Frogger {
    private int action;           // Last button pressed
    private boolean dead;         // True when game is over
    private int lives;            // Remaining lives
    private int level;            // Current level
    private int jumps;            // Current number of jumps
    private float timeLeft;       // Time remaining
    private int padsDone;         // Number of frogs safely on pads

    private Platno platno;
    private zaba frog;
    private List<Auto> cars;
    private List<Kamion> trucks;
    private List<Kmen> logs;
    private List<Korytnacka> riverTurtles;
    private List<Lekno> pads;

    private Timer gameTimer;
    private float elapsedTime;
    private Random rand = new Random();
    private int spawnProtection; // ticks of invulnerability after respawn

    private static final float GAME_SPEED = 0.1f; // seconds per tick accumulation

    public Frogger() {
        this.action = 0;
        this.dead = false;
        this.lives = 3;
        this.level = 1;
        this.jumps = 0;
        this.timeLeft = 300;
        this.padsDone = 0;

        this.cars = new ArrayList<>();
        this.trucks = new ArrayList<>();
        this.logs = new ArrayList<>();
        this.riverTurtles = new ArrayList<>();
        this.pads = new ArrayList<>();
        this.elapsedTime = 0;
        this.spawnProtection = 0;

        platno = new Platno(800, 600);
        platno.setGame(this);

        setup();

        gameTimer = new Timer(50, e -> { // ~20 FPS
            if (!dead) go();
        });
        gameTimer.start();
    }

    public void setup() {
        action = 0;
        dead = false;
        lives = platno.getStartLives();
        level = platno.getStartLevel();
        timeLeft = platno.getStartTime();
        padsDone = 0;
        jumps = 0;
        spawnProtection = 0;

        nextLevel();

    }

    private void nextLevel() {
        drawMap();
        switch (level) {
            case 1: level1(); break;
            case 2: level2(); break;
            case 3: level3(); break;
            case 4: level4(); break;
            case 5: level5(); break;
            default:
                System.out.println("All levels complete! Game Over!");
                dead = true;
        }
    }

    private void drawMap() {
        cars.clear();
        trucks.clear();
        logs.clear();
        riverTurtles.clear();
        pads.clear();

        // Create pads at bottom (finish row)
        for (int x = -6; x <= 6; x += 3) {
            pads.add(new Lekno(x * 40, 240));
        }

        resetFrog();
        padsDone = 0;
    }

    private void resetFrog() {
        frog = new zaba(0, -240);
        jumps = 0;
        timeLeft = platno.getStartTime();
        platno.setZaba(frog);
        spawnProtection = 20; // ~1s invulnerability
    }

    // Main loop
    public void go() {
        if (dead) return;
        move();
    }

    private void move() {
        moveFrog();

        // Move dynamic objects every tick (small increments)
        for (Kamion truck : trucks) {
            truck.posunSa();
            if (truck.getX() > 420) truck.setX(-420 - rand.nextInt(160));
            if (truck.getX() < -420) truck.setX(420 + rand.nextInt(160));
        }

        for (Auto car : cars) {
            car.posunSa();
            if (car.getX() > 420) car.setX(-420 - rand.nextInt(160));
            if (car.getX() < -420) car.setX(420 + rand.nextInt(160));
        }

        for (Kmen log : logs) {
            log.posunSa();
            if (log.getX() > 420) log.setX(-420 - rand.nextInt(160));
            if (log.getX() < -420) log.setX(420 + rand.nextInt(160));
        }

        for (Korytnacka t : riverTurtles) {
            t.posunSa();
            if (t.getX() > 420) t.setX(-420 - rand.nextInt(160));
            if (t.getX() < -420) t.setX(420 + rand.nextInt(160));
        }

        // Carry frog with logs/turtles
        if (frog != null) {
            for (Kmen log : logs) {
                if (isOnLog(frog, log)) {
                    frog.setX(frog.getX() + log.getRychlost());
                }
            }
            for (Korytnacka t : riverTurtles) {
                if (isOnTurtle(frog, t)) {
                    if (t.getSmer()) frog.setX(frog.getX() - t.getRychlost());
                    else frog.setX(frog.getX() + t.getRychlost());
                }
            }
        }

        // update time once per second
        elapsedTime += GAME_SPEED;
        if (elapsedTime >= 1.0f) {
            elapsedTime = 0;
            timeLeft -= 1;
        }

        if (spawnProtection > 0) spawnProtection--;

        checkFrog();
        platno.vykresli();
    }

    private void moveFrog() {
        if (action == 0) return;
        switch (action) {
            case 1: moveLeft(); break;
            case 2: moveRight(); break;
            case 3: moveUp(); break;
            case 4: moveDown(); break;
        }
        action = 0;
    }

    private void moveLeft() {
        if (frog.getX() > -360) {
            frog.setX(frog.getX() - 20);
            jumps++;
            checkFrog();
        }
    }

    private void moveRight() {
        if (frog.getX() < 360) {
            frog.setX(frog.getX() + 20);
            jumps++;
            checkFrog();
        }
    }

    private void moveUp() {
        if (frog.getY() > -240) {
            frog.setY(frog.getY() - 20);
            jumps++;
            checkFrog();
        }
    }

    private void moveDown() {
        if (frog.getY() < 240) {
            frog.setY(frog.getY() + 20);
            jumps++;
            checkFrog();
        }
    }

    private void checkFrog() {
        if (frog == null) return;

        // Check pads (finish)
        for (Lekno pad : pads) {
            if (isNear(frog.getX(), frog.getY(), pad.getX(), pad.getY(), 30)) {
                padsDone++;
                resetFrog();
                if (padsDone >= 5) {
                    level++;
                    padsDone = 0;
                    System.out.println("Level " + level + " - All frogs safe!");
                    nextLevel();
                }
                return;
            }
        }

        // While spawn protection active, skip collisions
        if (spawnProtection > 0) return;

        // Collisions with vehicles
        for (Kamion t : trucks) if (isColliding(frog, t)) { killFrog(); return; }
        for (Auto c : cars) if (isColliding(frog, c)) { killFrog(); return; }

        // Water area check
        if (frog.getY() > 0 && frog.getY() < 200) {
            boolean onLog = false, onTurtle = false;
            for (Kmen log : logs) if (isOnLog(frog, log)) { onLog = true; break; }
            for (Korytnacka t : riverTurtles) if (isOnTurtle(frog, t)) { onTurtle = true; break; }
            if (!onLog && !onTurtle) { killFrog(); return; }
        }

        if (timeLeft <= 0) { killFrog(); return; }
    }

    private void killFrog() {
        lives--;
        System.out.println("Frog died! Lives remaining: " + lives);
        if (lives <= 0) {
            dead = true;
            System.out.println("GAME OVER!");
            if (gameTimer != null) gameTimer.stop();
        } else resetFrog();
    }

    // Helpers
    private boolean isColliding(zaba frog, Auto car) {
        return Math.abs(frog.getX() - car.getX()) < 30 && Math.abs(frog.getY() - car.getY()) < 25;
    }
    private boolean isColliding(zaba frog, Kamion truck) {
        return Math.abs(frog.getX() - truck.getX()) < 40 && Math.abs(frog.getY() - truck.getY()) < 25;
    }
    private boolean isOnLog(zaba frog, Kmen log) {
        return Math.abs(frog.getX() - log.getX()) < 30 && Math.abs(frog.getY() - log.getY()) < 15;
    }
    private boolean isOnTurtle(zaba frog, Korytnacka t) {
        return Math.abs(frog.getX() - t.getX()) < 30 && Math.abs(frog.getY() - t.getY()) < 15;
    }
    private boolean isNear(int x1,int y1,int x2,int y2,int d){return Math.abs(x1-x2)<d && Math.abs(y1-y2)<d;}

    // Level setups (kept similar to previous)
    private void level1(){
        createTruck(5,-200,270,0.9f);
        createTruck(0,-200,270,0.9f);
        createTruck(-8,-160,90,0.9f);
        createTruck(-5,-160,90,0.9f);
        createTruck(2,-160,90,0.9f);
        createTruck(-3,-120,270,0.8f);
        createTruck(6,-120,270,0.8f);

        createCar(0,-80,90,0.4f);
        createCar(-4,-80,90,0.4f);
        createCar(8,-40,270,0.2f);
        createCar(3,-40,270,0.2f);

        createLog(4,120,3,0.6f);
        createLog(-8,120,5,0.6f);
        createLog(4,200,2,0.7f);
        createLog(-4,200,3,0.7f);
        createLog(1,280,4,0.3f);
        createLog(-6,280,4,0.3f);

        createRiverTurtle(2,160,2,0.4f);
        createRiverTurtle(-4,160,4,0.4f);
        createRiverTurtle(5,160,4,0.4f);
        createRiverTurtle(-3,240,4,0.5f);
        createRiverTurtle(7,240,3,0.5f);
    }
    private void level2(){ createTruck(4,-200,270,0.8f); createTruck(-3,-200,270,0.8f); createTruck(0,-160,90,0.9f); createTruck(-4,-160,90,0.9f); createTruck(-1,-120,270,0.8f); createTruck(4,-120,270,0.8f); createTruck(-5,-120,270,0.8f); createCar(0,-80,90,0.2f); createCar(-4,-80,90,0.2f); createCar(8,-80,90,0.2f); createCar(6,-40,270,0.4f); createCar(2,-40,270,0.4f); createCar(-3,-40,270,0.4f); createCar(-6,-40,270,0.4f); createLog(6,120,3,0.6f); createLog(-4,120,4,0.6f); createLog(0,200,3,0.3f); createLog(-6,200,3,0.3f); createLog(1,280,4,0.5f); createLog(6,280,4,0.5f); createRiverTurtle(0,160,4,0.3f); createRiverTurtle(6,160,4,0.3f); createRiverTurtle(0,240,4,0.4f); createRiverTurtle(6,240,3,0.4f); }
    private void level3(){ createTruck(-8,-200,270,0.7f); createTruck(-4,-200,270,0.7f); createTruck(0,-200,270,0.7f); createTruck(-2,-160,90,0.7f); createTruck(2,-160,90,0.7f); createTruck(-6,-160,90,0.7f); createTruck(-4,-120,270,0.7f); createTruck(0,-120,270,0.7f); createTruck(4,-120,270,0.7f); createCar(-3,-80,90,0.2f); createCar(-5,-80,90,0.2f); createCar(5,-80,90,0.2f); createCar(1,-80,90,0.2f); createCar(0,-40,270,0.3f); createCar(5,-40,270,0.3f); createCar(-7,-40,270,0.3f); createCar(-3,-40,270,0.3f); createLog(-6,120,4,0.4f); createLog(-2,200,3,0.4f); createLog(5,200,3,0.4f); createLog(-4,280,2,0.2f); createLog(0,280,2,0.2f); createLog(4,280,2,0.2f); createRiverTurtle(-4,160,4,0.3f); createRiverTurtle(5,160,4,0.3f); createRiverTurtle(-1,240,3,0.4f); createRiverTurtle(-8,240,3,0.4f); }
    private void level4(){ createTruck(-8,-200,270,0.5f); createTruck(-2,-200,270,0.5f); createTruck(6,-200,270,0.5f); createTruck(4,-160,90,0.6f); createTruck(-1,-160,90,0.6f); createTruck(-6,-160,90,0.6f); createCar(-4,-120,270,0.3f); createCar(0,-120,270,0.3f); createCar(4,-120,270,0.3f); createCar(7,-120,270,0.3f); createCar(-3,-80,90,0.2f); createCar(-5,-80,90,0.2f); createCar(5,-80,90,0.2f); createCar(1,-80,90,0.2f); createCar(0,-40,270,0.3f); createCar(5,-40,270,0.3f); createCar(-7,-40,270,0.3f); createCar(-3,-40,270,0.3f); createLog(-3,120,3,0.3f); createLog(-3,200,3,0.3f); createLog(-3,280,3,0.3f); createRiverTurtle(-4,160,4,0.3f); createRiverTurtle(4,160,4,0.3f); createRiverTurtle(-7,160,1,0.3f); createRiverTurtle(-1,240,3,0.4f); createRiverTurtle(-8,240,3,0.4f); createRiverTurtle(3,240,2,0.4f); }
    private void level5(){ createCar(-4,-200,270,0.3f); createCar(0,-200,270,0.3f); createCar(4,-200,270,0.3f); createCar(7,-200,270,0.3f); createCar(-3,-160,90,0.2f); createCar(-5,-160,90,0.2f); createCar(5,-160,90,0.2f); createCar(1,-160,90,0.2f); createCar(8,-160,90,0.2f); createCar(-4,-120,270,0.3f); createCar(0,-120,270,0.3f); createCar(4,-120,270,0.3f); createCar(7,-120,270,0.3f); createCar(-3,-80,90,0.2f); createCar(-5,-80,90,0.2f); createCar(4,-80,90,0.2f); createCar(1,-80,90,0.2f); createCar(7,-80,90,0.2f); createCar(0,-40,270,0.3f); createCar(5,-40,270,0.3f); createCar(-7,-40,270,0.3f); createCar(-3,-40,270,0.3f); createLog(-5,120,2,0.2f); createLog(0,200,2,0.1f); createLog(-5,280,2,0.2f); createRiverTurtle(-4,160,2,0.3f); createRiverTurtle(4,160,3,0.3f); createRiverTurtle(-7,160,2,0.3f); createRiverTurtle(-1,240,2,0.3f); createRiverTurtle(-8,240,2,0.3f); createRiverTurtle(3,240,3,0.3f); }
    
    // Object creation helpers
    private void createTruck(int x,int y,int direction,float speed){ trucks.add(new Kamion(x*40,y,(int)(speed*3), direction==90,50)); }
    private void createCar(int x,int y,int direction,float speed){ cars.add(new Auto(x*40,y,(int)(speed*3), direction==90,40)); }
    private void createLog(int x,int y,int length,float speed){ logs.add(new Kmen(x*40,y,length*40, Math.max(1,(int)(speed*2)))); }
    private void createRiverTurtle(int x,int y,int length,float speed){ riverTurtles.add(new Korytnacka(x*40,y,(int)(speed*2), length*40)); }
    
    // Getters for rendering
    public List<Auto> getCars(){ return cars; }
    public List<Kamion> getTrucks(){ return trucks; }
    public List<Kmen> getLogs(){ return logs; }
    public List<Korytnacka> getRiverTurtles(){ return riverTurtles; }
    public List<Lekno> getPads(){ return pads; }
    public zaba getFrog(){ return frog; }
    public int getLives(){ return lives; }
    public int getLevel(){ return level; }
    public int getJumps(){ return jumps; }
    public float getTimeLeft(){ return timeLeft; }
    public boolean isDead(){ return dead; }
    
    public void setAction(int action){ this.action = action; }
    
    public static void main(String[] args){ Frogger g = new Frogger(); g.setup(); }
}