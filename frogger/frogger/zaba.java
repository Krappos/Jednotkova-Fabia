
/**
 * Frog class - the player character
 */
public class zaba extends obdlznik {
    private int poziciaX;
    private int poziciaY;
    
    public zaba(int x, int y) {
        super(x, y, 30, 30);
        this.poziciaX = x;
        this.poziciaY = y;
    }
    
    public void posunDolava() {
        if (poziciaX > -360) {
            poziciaX -= 20;
            this.x = poziciaX;
        }
    }
    
    public void posunDoprava() {
        if (poziciaX < 360) {
            poziciaX += 20;
            this.x = poziciaX;
        }
    }
    
    public void posunHore() {
        if (poziciaY < 240) {
            poziciaY += 20;
            this.y = poziciaY;
        }
    }
    
    public void posunDole() {
        if (poziciaY > -240) {
            poziciaY -= 20;
            this.y = poziciaY;
        }
    }
    
    public void setX(int newX) {
        poziciaX = newX;
        this.x = newX;
    }
    
    public void setY(int newY) {
        poziciaY = newY;
        this.y = newY;
    }
    
    public int getX() {
        return poziciaX;
    }
    
    public int getY() {
        return poziciaY;
    }
    
    @Override
    public void zobraz() {
        // Display frog
    }
    
    @Override
    public void skry() {
        // Hide frog
    }
}
