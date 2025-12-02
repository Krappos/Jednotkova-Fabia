
/**
 * Turtle class - moving water platform
 */
public class Korytnacka extends Obrazok {
    private boolean smer;
    private int rychlost;
    private int dlzka;
    
    public Korytnacka(int x, int y, int rychlost, int dlzka) {
        super(x, y);
        this.smer = true;
        this.rychlost = rychlost;
        this.dlzka = dlzka;
    }
    
    public void posunSa() {
        if (smer) {
            x -= rychlost;  // Move left
        } else {
            x += rychlost;  // Move right
        }
    }
    
    public boolean getSmer() {
        return smer;
    }
    
    public void setX(int newX) {
        x = newX;
    }
    
    public int getDlzka() {
        return dlzka;
    }
    
    @Override
    public void zobraz() {
        // Display turtle
    }
    
    @Override
    public void skry() {
        // Hide turtle
    }
}
