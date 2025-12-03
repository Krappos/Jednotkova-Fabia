
/**
 * Truck class - larger moving obstacle
 */
public class Kamion extends Obrazok {
    private int rychlost;
    private boolean smer;
    private int rad;
    
    public Kamion(int x, int y, int rychlost, boolean smer, int rad) {
        super(x, y);
        this.rychlost = rychlost;
        this.smer = smer;
        this.rad = rad;
    }
    
    public void posunSa() {
        if (smer) {
            x += rychlost;
        } else {
            x -= rychlost;
        }
    }
    
    public boolean getSmer() {
        return smer;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setX(int newX) {
        x = newX;
    }
    
    public int getRychlost() {
        return rychlost;
    }
    
    @Override
    public void zobraz() {
        // Display truck
    }
    
    @Override
    public void skry() {
        // Hide truck
    }
}
