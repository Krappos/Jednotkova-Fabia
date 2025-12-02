public class Zaba {
    private int poziciaX;
    private int poziciaY;
    private Obrazok obrazok;
    private Manazer manazer;
    public Zaba(int poziciaX, int poziciaY) {
       this.obrazok = new Obrazok ("obrazky/zaba.png");
       this.poziciaX = poziciaX;
       this.poziciaY = poziciaY;
       
       
       this.manazer = new Manazer();
       this.manazer.spravujObjekt(this);
    }
    
    public void posunDole() {
        this.poziciaY += 40;
    }
    
    public void posunHore () {
        this.poziciaY -= 40;
    }
    
    public void posunVpravo () {
        this.poziciaX += 40;
    }
    
    public void posunVlavo () {
        this.poziciaX -= 40;
    }
    
    public void zobraz () {
        this.obrazok.skry();
    }
    
    public void skry () {
        this.obrazok.skry();
    }
    
    public int getPoziciaX () {
        return this.poziciaX;
    }
    
    public int getPoziciaY () {
        return this.poziciaY;
    }
}
