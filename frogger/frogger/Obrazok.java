
/**
 * Base class for all game objects
 */
public abstract class Obrazok {
    protected int x;
    protected int y;
    
    public Obrazok(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public abstract void zobraz();
    
    public abstract void skry();
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}
