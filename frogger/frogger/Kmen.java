
/**
 * Log class - moving platform in water
 */
public class Kmen extends Obrazok {
    private int dlzka;
    private int rychlost;
    
    public Kmen(int x, int y, int dlzka, int rychlost) {
        super(x, y);
        this.dlzka = dlzka;
        this.rychlost = rychlost;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    @Override
    public void zobraz() {
        // Display log
    }
    
    @Override
    public void skry() {
        // Hide log
    }
    
    public void posunSa() {
        x += rychlost;
    }

    public void setX(int newX) {
        x = newX;
    }

    public int getDlzka() {
        return dlzka;
    }

    public int getRychlost() {
        return rychlost;
    }
}
