
/**
 * Rectangle shape class extending Obrazok
 */
public class obdlznik extends Obrazok {
    private int sirkaPx;
    private int vyskaPx;
    private int farbaPx;
    
    public obdlznik(int x, int y, int sirka, int vyska) {
        super(x, y);
        this.sirkaPx = sirka;
        this.vyskaPx = vyska;
        this.farbaPx = java.awt.Color.BLACK.getRGB();
    }
    
    public void zobraz() {
        // Display rectangle at position (x, y) with given width and height
    }
    
    public void skry() {
        // Hide rectangle
    }
    
    public void zmenaVelkosti(int x, int y, int sirka, int vyska) {
        this.x = x;
        this.y = y;
        this.sirkaPx = sirka;
        this.vyskaPx = vyska;
    }
    
    public int getSirka() {
        return sirkaPx;
    }
    
    public int getVyska() {
        return vyskaPx;
    }
}
