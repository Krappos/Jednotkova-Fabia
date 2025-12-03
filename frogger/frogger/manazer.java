import java.util.*;

/**
 * Game Manager - handles game state, levels, and logic
 */
public class manazer {
    private int action;
    private boolean dead;
    private int lives;
    private int level;
    private int jumps;
    private float timeLeft;
    private int padsDone;

    private zaba frog;
    private List<Auto> cars;
    private List<Kamion> trucks;
    private List<Kmen> logs;
    private List<Korytnacka> riverTurtles;
    private List<Lekno> pads;

    private float elapsedTime;
    private Random rand = new Random();
    private int spawnProtection;

    private static final float GAME_SPEED = 0.1f;
    private static final int SPAWN_PROTECTION_TICKS = 20;

    public manazer() {
        this.cars = new ArrayList<>();
        this.trucks = new ArrayList<>();
        this.logs = new ArrayList<>();
        this.riverTurtles = new ArrayList<>();
        this.pads = new ArrayList<>();
        this.rand = new Random();
        this.elapsedTime = 0;
        reset();
    }

    public void reset() {
        this.action = 0;
        this.dead = false;
        this.lives = 3;
        this.level = 1;
        this.jumps = 0;
        this.timeLeft = 300;
        this.padsDone = 0;
        this.spawnProtection = 0;
    }

    public void nextLevel() {
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

    public void drawMap() {
        cars.clear();
        trucks.clear();
        logs.clear();
        riverTurtles.clear();
        pads.clear();

        for (int x = -6; x <= 6; x += 3) {
            pads.add(new Lekno(x * 40, 240));
        }

        resetFrog();
        padsDone = 0;
    }

    public void resetFrog() {
        frog = new zaba(0, -240);
        jumps = 0;
        timeLeft = 300;
        spawnProtection = SPAWN_PROTECTION_TICKS;
    }

    public void update() {
        moveFrog();

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

        elapsedTime += GAME_SPEED;
        if (elapsedTime >= 1.0f) {
            elapsedTime = 0;
            timeLeft -= 1;
        }

        if (spawnProtection > 0) spawnProtection--;

        checkFrog();
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
        if (frog.getX() > -380) {  // Extended boundary
            frog.setX(frog.getX() - 20);
            jumps++;
            checkFrog();
        }
    }

    private void moveRight() {
        if (frog.getX() < 380) {  // Extended boundary
            frog.setX(frog.getX() + 20);
            jumps++;
            checkFrog();
        }
    }

    private void moveUp() {
        if (frog.getY() > -280) {  // Extended to allow full movement up
            frog.setY(frog.getY() - 20);
            jumps++;
            checkFrog();
        }
    }

    private void moveDown() {
        if (frog.getY() < 280) {  // Extended to allow full movement down
            frog.setY(frog.getY() + 20);
            jumps++;
            checkFrog();
        }
    }

    private void checkFrog() {
        if (frog == null) return;

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

        if (spawnProtection > 0) return;

        // Collisions with vehicles only in road areas
        if (frog.getY() >= -100 && frog.getY() <= 100) {
            for (Kamion t : trucks) if (isColliding(frog, t)) { killFrog(); return; }
            for (Auto c : cars) if (isColliding(frog, c)) { killFrog(); return; }
        }

        // Water area check (must be on log or turtle)
        if (frog.getY() > 100 && frog.getY() < 200) {
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
        } else resetFrog();
    }

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

    private void level1(){
        // More trucks
        createTruck(5,-200,270,0.9f);
        createTruck(0,-200,270,0.9f);
        createTruck(-10,-200,270,0.9f);
        createTruck(-8,-160,90,0.9f);
        createTruck(-5,-160,90,0.9f);
        createTruck(2,-160,90,0.9f);
        createTruck(5,-160,90,0.9f);
        createTruck(-3,-120,270,0.8f);
        createTruck(6,-120,270,0.8f);
        createTruck(-10,-120,270,0.8f);
        // More cars
        createCar(0,-80,90,0.5f);
        createCar(-4,-80,90,0.5f);
        createCar(4,-80,90,0.5f);
        createCar(8,-80,90,0.5f);
        createCar(8,-40,270,0.3f);
        createCar(3,-40,270,0.3f);
        createCar(-2,-40,270,0.3f);
        createCar(-7,-40,270,0.3f);
        // More logs
        createLog(4,120,3,0.6f);
        createLog(-8,120,5,0.6f);
        createLog(0,120,4,0.6f);
        createLog(4,200,2,0.7f);
        createLog(-4,200,3,0.7f);
        createLog(-10,200,3,0.7f);
        createLog(1,280,4,0.3f);
        createLog(-6,280,4,0.3f);
        createLog(6,280,3,0.3f);
        // More turtles
        createRiverTurtle(2,160,2,0.4f);
        createRiverTurtle(-4,160,4,0.4f);
        createRiverTurtle(5,160,4,0.4f);
        createRiverTurtle(8,160,3,0.4f);
        createRiverTurtle(-3,240,4,0.5f);
        createRiverTurtle(7,240,3,0.5f);
        createRiverTurtle(-8,240,3,0.5f);
    }
    private void level2(){ createTruck(4,-200,270,0.8f); createTruck(-3,-200,270,0.8f); createTruck(0,-160,90,0.9f); createTruck(-4,-160,90,0.9f); createTruck(-1,-120,270,0.8f); createTruck(4,-120,270,0.8f); createTruck(-5,-120,270,0.8f); createCar(0,-80,90,0.2f); createCar(-4,-80,90,0.2f); createCar(8,-80,90,0.2f); createCar(6,-40,270,0.4f); createCar(2,-40,270,0.4f); createCar(-3,-40,270,0.4f); createCar(-6,-40,270,0.4f); createLog(6,120,3,0.6f); createLog(-4,120,4,0.6f); createLog(0,200,3,0.3f); createLog(-6,200,3,0.3f); createLog(1,280,4,0.5f); createLog(6,280,4,0.5f); createRiverTurtle(0,160,4,0.3f); createRiverTurtle(6,160,4,0.3f); createRiverTurtle(0,240,4,0.4f); createRiverTurtle(6,240,3,0.4f); }
    private void level3(){ createTruck(-8,-200,270,0.7f); createTruck(-4,-200,270,0.7f); createTruck(0,-200,270,0.7f); createTruck(-2,-160,90,0.7f); createTruck(2,-160,90,0.7f); createTruck(-6,-160,90,0.7f); createTruck(-4,-120,270,0.7f); createTruck(0,-120,270,0.7f); createTruck(4,-120,270,0.7f); createCar(-3,-80,90,0.2f); createCar(-5,-80,90,0.2f); createCar(5,-80,90,0.2f); createCar(1,-80,90,0.2f); createCar(0,-40,270,0.3f); createCar(5,-40,270,0.3f); createCar(-7,-40,270,0.3f); createCar(-3,-40,270,0.3f); createLog(-6,120,4,0.4f); createLog(-2,200,3,0.4f); createLog(5,200,3,0.4f); createLog(-4,280,2,0.2f); createLog(0,280,2,0.2f); createLog(4,280,2,0.2f); createRiverTurtle(-4,160,4,0.3f); createRiverTurtle(5,160,4,0.3f); createRiverTurtle(-1,240,3,0.4f); createRiverTurtle(-8,240,3,0.4f); }
    private void level4(){ createTruck(-8,-200,270,0.5f); createTruck(-2,-200,270,0.5f); createTruck(6,-200,270,0.5f); createTruck(4,-160,90,0.6f); createTruck(-1,-160,90,0.6f); createTruck(-6,-160,90,0.6f); createCar(-4,-120,270,0.3f); createCar(0,-120,270,0.3f); createCar(4,-120,270,0.3f); createCar(7,-120,270,0.3f); createCar(-3,-80,90,0.2f); createCar(-5,-80,90,0.2f); createCar(5,-80,90,0.2f); createCar(1,-80,90,0.2f); createCar(0,-40,270,0.3f); createCar(5,-40,270,0.3f); createCar(-7,-40,270,0.3f); createCar(-3,-40,270,0.3f); createLog(-3,120,3,0.3f); createLog(-3,200,3,0.3f); createLog(-3,280,3,0.3f); createRiverTurtle(-4,160,4,0.3f); createRiverTurtle(4,160,4,0.3f); createRiverTurtle(-7,160,1,0.3f); createRiverTurtle(-1,240,3,0.4f); createRiverTurtle(-8,240,3,0.4f); createRiverTurtle(3,240,2,0.4f); }
    private void level5(){ createCar(-4,-200,270,0.3f); createCar(0,-200,270,0.3f); createCar(4,-200,270,0.3f); createCar(7,-200,270,0.3f); createCar(-3,-160,90,0.2f); createCar(-5,-160,90,0.2f); createCar(5,-160,90,0.2f); createCar(1,-160,90,0.2f); createCar(8,-160,90,0.2f); createCar(-4,-120,270,0.3f); createCar(0,-120,270,0.3f); createCar(4,-120,270,0.3f); createCar(7,-120,270,0.3f); createCar(-3,-80,90,0.2f); createCar(-5,-80,90,0.2f); createCar(4,-80,90,0.2f); createCar(1,-80,90,0.2f); createCar(7,-80,90,0.2f); createCar(0,-40,270,0.3f); createCar(5,-40,270,0.3f); createCar(-7,-40,270,0.3f); createCar(-3,-40,270,0.3f); createLog(-5,120,2,0.2f); createLog(0,200,2,0.1f); createLog(-5,280,2,0.2f); createRiverTurtle(-4,160,2,0.3f); createRiverTurtle(4,160,3,0.3f); createRiverTurtle(-7,160,2,0.3f); createRiverTurtle(-1,240,2,0.3f); createRiverTurtle(-8,240,2,0.3f); createRiverTurtle(3,240,3,0.3f); }

    private void createTruck(int x,int y,int direction,float speed){ 
        int vel = Math.max(1, (int)(speed*3));
        trucks.add(new Kamion(x*40,y,vel, direction==90,50)); 
    }
    private void createCar(int x,int y,int direction,float speed){ 
        int vel = Math.max(1, (int)(speed*3));
        cars.add(new Auto(x*40,y,vel, direction==90,40)); 
    }
    private void createLog(int x,int y,int length,float speed){ 
        int vel = Math.max(1, (int)(speed*2));
        logs.add(new Kmen(x*40,y,length*40, vel)); 
    }
    private void createRiverTurtle(int x,int y,int length,float speed){ 
        int vel = Math.max(1, (int)(speed*2));
        riverTurtles.add(new Korytnacka(x*40,y,vel, length*40)); 
    }

    // Getters
    public zaba getFrog() { return frog; }
    public List<Auto> getCars() { return cars; }
    public List<Kamion> getTrucks() { return trucks; }
    public List<Kmen> getLogs() { return logs; }
    public List<Korytnacka> getRiverTurtles() { return riverTurtles; }
    public List<Lekno> getPads() { return pads; }
    public int getLives() { return lives; }
    public int getLevel() { return level; }
    public int getJumps() { return jumps; }
    public float getTimeLeft() { return timeLeft; }
    public boolean isDead() { return dead; }
    public int getPadsDone() { return padsDone; }

    // Setters
    public void setAction(int action) { this.action = action; }
}
