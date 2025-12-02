
import java.util.*;
import java.awt.event.*;
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
    private static final int START_LIVES = 3;
    private static final int START_TIME = 300;
    private static final int START_LEVEL = 1;
    private static final float GAME_SPEED = 0.1f;
    
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
        
        // Create GUI
        platno = new Platno(800, 600);
        platno.setGame(this);
        
        // Start game timer
        gameTimer = new Timer(50, e -> {
            if (!dead) {
                go();
            }
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
        
        nextLevel();
    }
        drawMap();
        
        switch(level) {
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
        
        // Create pads at top
        for (int x = -6; x <= 6; x += 3) {
            pads.add(new Lekno(x * 40, 240));
        }
        
        resetFrog();
        padsDone = 0;
    }
    
    private void resetFrog() {
        if (frog != null) {
            frog = null;
        }
        frog = new zaba(0, -240);
        jumps = 0;
        timeLeft = platno.getStartTime();
        platno.setZaba(frog);
    }
    
    // ==================== MAIN GAME LOOP ====================
    
    public void go() {
        if (dead) return;
        
        move();
    }
    
    private void move() {
        moveFrog();
        elapsedTime += GAME_SPEED;
        
        if (elapsedTime >= 1.0f) {
            elapsedTime = 0;
            
            // Decrement time
            timeLeft -= 1;
            
            // Move trucks
            for (Kamion truck : trucks) {
                truck.posunSa();
                // Wrap around screen
                if (truck.getX() > 400) {
                    truck.setX(-400);
                } else if (truck.getX() < -400) {
                    truck.setX(400);
                }
            }
            
            // Move cars
            for (Auto car : cars) {
                car.posunSa();
                // Wrap around screen
                if (car.getX() > 400) {
                    car.setX(-400);
                } else if (car.getX() < -400) {
                    car.setX(400);
                }
            }
            
            // Move logs
            for (Kmen log : logs) {
                // Move log and carry frog
                int newX = log.getX() + 1;
                if (newX > 400) {
                    newX = -400;
                }
                log.setX(newX);
                
                // If frog is on log, move frog too
                if (isOnLog(frog, log)) {
                    frog.setX(frog.getX() + 1);
                }
            }
            
            // Move river turtles
            for (Korytnacka turtle : riverTurtles) {
                turtle.posunSa();
                // Wrap around screen
                if (turtle.getX() > 400) {
                    turtle.setX(-400);
                } else if (turtle.getX() < -400) {
                    turtle.setX(400);
                }
                
                // If frog is on turtle, move frog opposite direction
                if (isOnTurtle(frog, turtle)) {
                    frog.setX(frog.getX() - 1);
                }
            }
            
            checkFrog();
        }
        
        platno.vykresli();
    }
    
    private void moveFrog() {
        if (action != 0) {
            switch(action) {
                case 1: moveLeft(); break;
                case 2: moveRight(); break;
                case 3: moveUp(); break;
                case 4: moveDown(); break;
            }
            action = 0;
        }
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
        if (frog.getY() < 240) {
            frog.setY(frog.getY() + 20);
            jumps++;
            checkFrog();
        }
    }
    
    private void moveDown() {
        if (frog.getY() > -240) {
            frog.setY(frog.getY() - 20);
            jumps++;
            checkFrog();
        }
    }
    
    private void checkFrog() {
        // Check if on pad
        for (Lekno pad : pads) {
            if (isNear(frog.getX(), frog.getY(), pad.getX(), pad.getY(), 30)) {
                padsDone++;
                resetFrog();
                
                if (padsDone == 5) {
                    level++;
                    padsDone = 0;
                    System.out.println("Level " + level + " - All frogs safe!");
                    nextLevel();
                }
                return;
            }
        }
        
        // Check if hit by truck or car
        for (Kamion truck : trucks) {
            if (isColliding(frog, truck)) {
                killFrog();
                return;
            }
        }
        
        for (Auto car : cars) {
            if (isColliding(frog, car)) {
                killFrog();
                return;
            }
        }
        
        // Check if in water without safety
        if (frog.getY() > 0 && frog.getY() < 200) {
            boolean onLog = false;
            boolean onTurtle = false;
            
            for (Kmen log : logs) {
                if (isOnLog(frog, log)) {
                    onLog = true;
                    break;
                }
            }
            
            for (Korytnacka turtle : riverTurtles) {
                if (isOnTurtle(frog, turtle)) {
                    onTurtle = true;
                    break;
                }
            }
            
            if (!onLog && !onTurtle) {
                killFrog();
                return;
            }
        }
        
        // Check if time ran out
        if (timeLeft <= 0) {
            killFrog();
            return;
        }
    }
    
    private void killFrog() {
        lives--;
        System.out.println("Frog died! Lives remaining: " + lives);
        
        if (lives <= 0) {
            dead = true;
            System.out.println("GAME OVER!");
            if (gameTimer != null) {
                gameTimer.stop();
            }
        } else {
            resetFrog();
        }
    }
    
    // ==================== HELPER METHODS ====================
    
    private boolean isColliding(zaba frog, Auto car) {
        int frogX = frog.getX();
        int frogY = frog.getY();
        int carX = car.getX();
        int carY = car.getY();
        
        return Math.abs(frogX - carX) < 30 && Math.abs(frogY - carY) < 25;
    }
    
    private boolean isColliding(zaba frog, Kamion truck) {
        int frogX = frog.getX();
        int frogY = frog.getY();
        int truckX = truck.getX();
        int truckY = truck.getY();
        
        return Math.abs(frogX - truckX) < 40 && Math.abs(frogY - truckY) < 25;
    }
    
    private boolean isOnLog(zaba frog, Kmen log) {
        return Math.abs(frog.getX() - log.getX()) < 30 && Math.abs(frog.getY() - log.getY()) < 15;
    }
    
    private boolean isOnTurtle(zaba frog, Korytnacka turtle) {
        return Math.abs(frog.getX() - turtle.getX()) < 30 && Math.abs(frog.getY() - turtle.getY()) < 15;
    }
    
    private boolean isNear(int x1, int y1, int x2, int y2, int distance) {
        return Math.abs(x1 - x2) < distance && Math.abs(y1 - y2) < distance;
    }
    
    // ==================== LEVEL DESIGNS ====================
    
    private void level1() {
        createTruck(5, -200, 270, 0.9f);
        createTruck(0, -200, 270, 0.9f);
        createTruck(-8, -160, 90, 0.9f);
        createTruck(-5, -160, 90, 0.9f);
        createTruck(2, -160, 90, 0.9f);
        createTruck(-3, -120, 270, 0.8f);
        createTruck(6, -120, 270, 0.8f);
        
        createCar(0, -80, 90, 0.4f);
        createCar(-4, -80, 90, 0.4f);
        createCar(8, -40, 270, 0.2f);
        createCar(3, -40, 270, 0.2f);
        
        createLog(4, 120, 3, 0.6f);
        createLog(-8, 120, 5, 0.6f);
        createLog(4, 200, 2, 0.7f);
        createLog(-4, 200, 3, 0.7f);
        createLog(1, 280, 4, 0.3f);
        createLog(-6, 280, 4, 0.3f);
        
        createRiverTurtle(2, 160, 2, 0.4f);
        createRiverTurtle(-4, 160, 4, 0.4f);
        createRiverTurtle(5, 160, 4, 0.4f);
        createRiverTurtle(-3, 240, 4, 0.5f);
        createRiverTurtle(7, 240, 3, 0.5f);
    }
    
    private void level2() {
        createTruck(4, -200, 270, 0.8f);
        createTruck(-3, -200, 270, 0.8f);
        createTruck(0, -160, 90, 0.9f);
        createTruck(-4, -160, 90, 0.9f);
        createTruck(-1, -120, 270, 0.8f);
        createTruck(4, -120, 270, 0.8f);
        createTruck(-5, -120, 270, 0.8f);
        
        createCar(0, -80, 90, 0.2f);
        createCar(-4, -80, 90, 0.2f);
        createCar(8, -80, 90, 0.2f);
        createCar(6, -40, 270, 0.4f);
        createCar(2, -40, 270, 0.4f);
        createCar(-3, -40, 270, 0.4f);
        createCar(-6, -40, 270, 0.4f);
        
        createLog(6, 120, 3, 0.6f);
        createLog(-4, 120, 4, 0.6f);
        createLog(0, 200, 3, 0.3f);
        createLog(-6, 200, 3, 0.3f);
        createLog(1, 280, 4, 0.5f);
        createLog(6, 280, 4, 0.5f);
        
        createRiverTurtle(0, 160, 4, 0.3f);
        createRiverTurtle(6, 160, 4, 0.3f);
        createRiverTurtle(0, 240, 4, 0.4f);
        createRiverTurtle(6, 240, 3, 0.4f);
    }
    
    private void level3() {
        createTruck(-8, -200, 270, 0.7f);
        createTruck(-4, -200, 270, 0.7f);
        createTruck(0, -200, 270, 0.7f);
        createTruck(-2, -160, 90, 0.7f);
        createTruck(2, -160, 90, 0.7f);
        createTruck(-6, -160, 90, 0.7f);
        createTruck(-4, -120, 270, 0.7f);
        createTruck(0, -120, 270, 0.7f);
        createTruck(4, -120, 270, 0.7f);
        
        createCar(-3, -80, 90, 0.2f);
        createCar(-5, -80, 90, 0.2f);
        createCar(5, -80, 90, 0.2f);
        createCar(1, -80, 90, 0.2f);
        createCar(0, -40, 270, 0.3f);
        createCar(5, -40, 270, 0.3f);
        createCar(-7, -40, 270, 0.3f);
        createCar(-3, -40, 270, 0.3f);
        
        createLog(-6, 120, 4, 0.4f);
        createLog(-2, 200, 3, 0.4f);
        createLog(5, 200, 3, 0.4f);
        createLog(-4, 280, 2, 0.2f);
        createLog(0, 280, 2, 0.2f);
        createLog(4, 280, 2, 0.2f);
        
        createRiverTurtle(-4, 160, 4, 0.3f);
        createRiverTurtle(5, 160, 4, 0.3f);
        createRiverTurtle(-1, 240, 3, 0.4f);
        createRiverTurtle(-8, 240, 3, 0.4f);
    }
    
    private void level4() {
        createTruck(-8, -200, 270, 0.5f);
        createTruck(-2, -200, 270, 0.5f);
        createTruck(6, -200, 270, 0.5f);
        createTruck(4, -160, 90, 0.6f);
        createTruck(-1, -160, 90, 0.6f);
        createTruck(-6, -160, 90, 0.6f);
        
        createCar(-4, -120, 270, 0.3f);
        createCar(0, -120, 270, 0.3f);
        createCar(4, -120, 270, 0.3f);
        createCar(7, -120, 270, 0.3f);
        createCar(-3, -80, 90, 0.2f);
        createCar(-5, -80, 90, 0.2f);
        createCar(5, -80, 90, 0.2f);
        createCar(1, -80, 90, 0.2f);
        createCar(0, -40, 270, 0.3f);
        createCar(5, -40, 270, 0.3f);
        createCar(-7, -40, 270, 0.3f);
        createCar(-3, -40, 270, 0.3f);
        
        createLog(-3, 120, 3, 0.3f);
        createLog(-3, 200, 3, 0.3f);
        createLog(-3, 280, 3, 0.3f);
        
        createRiverTurtle(-4, 160, 4, 0.3f);
        createRiverTurtle(4, 160, 4, 0.3f);
        createRiverTurtle(-7, 160, 1, 0.3f);
        createRiverTurtle(-1, 240, 3, 0.4f);
        createRiverTurtle(-8, 240, 3, 0.4f);
        createRiverTurtle(3, 240, 2, 0.4f);
    }
    
    private void level5() {
        createCar(-4, -200, 270, 0.3f);
        createCar(0, -200, 270, 0.3f);
        createCar(4, -200, 270, 0.3f);
        createCar(7, -200, 270, 0.3f);
        createCar(-3, -160, 90, 0.2f);
        createCar(-5, -160, 90, 0.2f);
        createCar(5, -160, 90, 0.2f);
        createCar(1, -160, 90, 0.2f);
        createCar(8, -160, 90, 0.2f);
        createCar(-4, -120, 270, 0.3f);
        createCar(0, -120, 270, 0.3f);
        createCar(4, -120, 270, 0.3f);
        createCar(7, -120, 270, 0.3f);
        createCar(-3, -80, 90, 0.2f);
        createCar(-5, -80, 90, 0.2f);
        createCar(4, -80, 90, 0.2f);
        createCar(1, -80, 90, 0.2f);
        createCar(7, -80, 90, 0.2f);
        createCar(0, -40, 270, 0.3f);
        createCar(5, -40, 270, 0.3f);
        createCar(-7, -40, 270, 0.3f);
        createCar(-3, -40, 270, 0.3f);
        
        createLog(-5, 120, 2, 0.2f);
        createLog(0, 200, 2, 0.1f);
        createLog(-5, 280, 2, 0.2f);
        
        createRiverTurtle(-4, 160, 2, 0.3f);
        createRiverTurtle(4, 160, 3, 0.3f);
        createRiverTurtle(-7, 160, 2, 0.3f);
        createRiverTurtle(-1, 240, 2, 0.3f);
        createRiverTurtle(-8, 240, 2, 0.3f);
        createRiverTurtle(3, 240, 3, 0.3f);
    }
    
    // ==================== OBJECT CREATION ====================
    
    private void createTruck(int x, int y, int direction, float speed) {
        trucks.add(new Kamion(x * 40, y, (int)(speed * 2), direction == 90, 50));
    }
    
    private void createCar(int x, int y, int direction, float speed) {
        cars.add(new Auto(x * 40, y, (int)(speed * 2), direction == 90, 40));
    }
    
    private void createLog(int x, int y, int length, float speed) {
        logs.add(new Kmen(x * 40, y, length * 40, (int)(speed * 2)));
    }
    
    private void createRiverTurtle(int x, int y, int length, float speed) {
        riverTurtles.add(new Korytnacka(x * 40, y, (int)(speed * 2), length * 40));
    }
    
    // ==================== GETTERS FOR RENDERING ====================
    
    public List<Auto> getCars() { return cars; }
    public List<Kamion> getTrucks() { return trucks; }
    public List<Kmen> getLogs() { return logs; }
    public List<Korytnacka> getRiverTurtles() { return riverTurtles; }
    public List<Lekno> getPads() { return pads; }
    public zaba getFrog() { return frog; }
    public int getLives() { return lives; }
    public int getLevel() { return level; }
    public int getJumps() { return jumps; }
    public float getTimeLeft() { return timeLeft; }
    public boolean isDead() { return dead; }
    
    public void setAction(int action) {
        this.action = action;
    }
    
    public static void main(String[] args) {
        Frogger game = new Frogger();
        game.setup();
    }
}