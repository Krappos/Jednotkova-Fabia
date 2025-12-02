
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
    
    public void zobraz() {
        // Display log
    }
    
    public void skry() {
        // Hide log
    }
    
    public void setX(int newX) {
        x = newX;
    }
    
    public int getDlzka() {
        return dlzka;
    }
}
