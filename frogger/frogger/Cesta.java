
/**
 * Road class - moving obstacles
 */
public class Cesta extends obdlznik {
    public Cesta(int x, int y) {
        super(x, y, 800, 50);
    }
    
    @Override
    public void zobraz() {
        // Display road
    }
    
    @Override
    public void skry() {
        // Hide road
    }
    
    public void nastavVelkosti(int x, int y, int sirka, int vyska) {
        zmenaVelkosti(x, y, sirka, vyska);
    }
    
    public void nastavPozicuu(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
