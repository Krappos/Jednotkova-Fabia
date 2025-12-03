import java.util.*;
import javax.swing.Timer;

/**
 * Main Frogger game class - entry point and GUI manager
 * Delegates all game logic to manazer
 */
public class Frogger {
    private Platno platno;
    private manazer gameManager;
    private Timer gameTimer;

    public Frogger() {
        gameManager = new manazer();
        platno = new Platno(800, 600);
        platno.setGame(this);

        setup();

        gameTimer = new Timer(50, e -> { 
                if (!gameManager.isDead()) {
                    gameManager.update();
                    platno.vykresli();
                }
            });
        gameTimer.start();
    }

    public void setup() {
        gameManager.reset();
        gameManager.nextLevel();
    }
    // Delegates for game state access
    public zaba getFrog() { return gameManager.getFrog(); }

    public List<Auto> getCars() { return gameManager.getCars(); }

    public List<Kamion> getTrucks() { return gameManager.getTrucks(); }

    public List<Kmen> getLogs() { return gameManager.getLogs(); }

    public List<Korytnacka> getRiverTurtles() { return gameManager.getRiverTurtles(); }

    public List<Lekno> getPads() { return gameManager.getPads(); }

    public int getLives() { return gameManager.getLives(); }

    public int getLevel() { return gameManager.getLevel(); }

    public int getJumps() { return gameManager.getJumps(); }

    public float getTimeLeft() { return gameManager.getTimeLeft(); }

    public boolean isDead() { return gameManager.isDead(); }

    public void setAction(int action) { 
        gameManager.setAction(action); 
    }

    public static void main(String[] args) { 
        Frogger g = new Frogger(); 
        g.setup(); 
    }
}