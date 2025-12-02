
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Canvas/Screen class for the game with full GUI
 */
public class Platno extends JFrame {
    private GamePanel gamePanel;
    private Frogger game;
    
    // GUI Components
    private JLabel livesLabel;
    private JLabel levelLabel;
    private JLabel timeLabel;
    private JLabel jumpsLabel;
    private JButton newGameBtn;
    private JButton startBtn;
    private JSlider startLivesSlider;
    private JSlider startTimeSlider;
    private JSlider startLevelSlider;
    
    public Platno(int width, int height) {
        setTitle("Frogger - Klasická Hra");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Create main panel with game and controls
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Game panel
        gamePanel = new GamePanel(width, height);
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        
        // Control panel on the right
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.EAST);
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Ovládanie"));
        panel.setPreferredSize(new Dimension(250, 600));
        
        // Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setBorder(new TitledBorder("Tlačidlá"));
        
        newGameBtn = new JButton("NOVÁ HRA");
        newGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameBtn.addActionListener(e -> {
            if (game != null) {
                game.setup();
                updateDisplays();
            }
        });
        
        startBtn = new JButton("ŠTART");
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.addActionListener(e -> {
            startBtn.setEnabled(false);
        });
        
        btnPanel.add(Box.createVerticalStrut(10));
        btnPanel.add(newGameBtn);
        btnPanel.add(Box.createVerticalStrut(10));
        btnPanel.add(startBtn);
        btnPanel.add(Box.createVerticalGlue());
        
        // Monitors
        JPanel monitorPanel = new JPanel();
        monitorPanel.setLayout(new BoxLayout(monitorPanel, BoxLayout.Y_AXIS));
        monitorPanel.setBorder(new TitledBorder("Stav Hry"));
        
        livesLabel = new JLabel("Životov: 3");
        livesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        levelLabel = new JLabel("Level: 1");
        levelLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timeLabel = new JLabel("Čas: 300");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        jumpsLabel = new JLabel("Skokov: 0");
        jumpsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        monitorPanel.add(Box.createVerticalStrut(10));
        monitorPanel.add(livesLabel);
        monitorPanel.add(Box.createVerticalStrut(8));
        monitorPanel.add(levelLabel);
        monitorPanel.add(Box.createVerticalStrut(8));
        monitorPanel.add(timeLabel);
        monitorPanel.add(Box.createVerticalStrut(8));
        monitorPanel.add(jumpsLabel);
        monitorPanel.add(Box.createVerticalGlue());
        
        // Sliders
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        sliderPanel.setBorder(new TitledBorder("Nastavenia"));
        
        JLabel startLivesLabel = new JLabel("Počiatok životov:");
        startLivesSlider = new JSlider(1, 10, 3);
        startLivesSlider.setMajorTickSpacing(1);
        startLivesSlider.setPaintTicks(true);
        startLivesSlider.setPaintLabels(true);
        
        JLabel startTimeLabel = new JLabel("Počiatok času:");
        startTimeSlider = new JSlider(100, 600, 300);
        startTimeSlider.setMajorTickSpacing(100);
        startTimeSlider.setPaintTicks(true);
        startTimeSlider.setPaintLabels(true);
        
        JLabel startLevelLabel = new JLabel("Počiatok levelu:");
        startLevelSlider = new JSlider(1, 5, 1);
        startLevelSlider.setMajorTickSpacing(1);
        startLevelSlider.setPaintTicks(true);
        startLevelSlider.setPaintLabels(true);
        
        sliderPanel.add(startLivesLabel);
        sliderPanel.add(startLivesSlider);
        sliderPanel.add(Box.createVerticalStrut(15));
        sliderPanel.add(startTimeLabel);
        sliderPanel.add(startTimeSlider);
        sliderPanel.add(Box.createVerticalStrut(15));
        sliderPanel.add(startLevelLabel);
        sliderPanel.add(startLevelSlider);
        sliderPanel.add(Box.createVerticalGlue());
        
        panel.add(btnPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(monitorPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(sliderPanel);
        
        return panel;
    }
    
    public void setGame(Frogger game) {
        this.game = game;
        gamePanel.setGame(game);
    }
    
    public void updateDisplays() {
        if (game != null) {
            livesLabel.setText("Životov: " + game.getLives());
            levelLabel.setText("Level: " + game.getLevel());
            timeLabel.setText("Čas: " + (int)game.getTimeLeft());
            jumpsLabel.setText("Skokov: " + game.getJumps());
        }
    }
    
    public void vykresli() {
        gamePanel.repaint();
        updateDisplays();
    }
    
    public void zobraz() {
        setVisible(true);
    }
    
    public void skry() {
        setVisible(false);
    }
    
    public void setZaba(zaba zaba) {
        gamePanel.setZaba(zaba);
    }
    
    public int getStartLives() {
        return startLivesSlider.getValue();
    }
    
    public int getStartTime() {
        return startTimeSlider.getValue();
    }
    
    public int getStartLevel() {
        return startLevelSlider.getValue();
    }
    
    // ==================== GAME PANEL ====================
    
    private class GamePanel extends JPanel implements KeyListener {
        private int width;
        private int height;
        private Frogger game;
        private zaba frog;
        private BufferedImage autoImg;
        private BufferedImage kamionImg;
        private BufferedImage korytnackaImg;
        private BufferedImage leknoImg;
        private BufferedImage zabaImg;
        
        public GamePanel(int width, int height) {
            this.width = width;
            this.height = height;
            setPreferredSize(new Dimension(width, height));
            setBackground(Color.WHITE);
            setFocusable(true);
            addKeyListener(this);
            
            // Load images
            loadImages();
        }
        
        private void loadImages() {
            try {
                String basePath = "d:\\vmraz\\Documents\\úvod do štúdia\\Jednotkova-Fabia\\frogger\\frogger\\obrazky\\";
                autoImg = ImageIO.read(new File(basePath + "auto.jpg.png"));
                kamionImg = ImageIO.read(new File(basePath + "kamion.jpg"));
                korytnackaImg = ImageIO.read(new File(basePath + "korytnacka.jpg"));
                leknoImg = ImageIO.read(new File(basePath + "lekno.jpg"));
                // optional frog image
                File frogFilePng = new File(basePath + "zaba.png");
                File frogFileJpg = new File(basePath + "zaba.jpg");
                if (frogFilePng.exists()) {
                    zabaImg = ImageIO.read(frogFilePng);
                } else if (frogFileJpg.exists()) {
                    zabaImg = ImageIO.read(frogFileJpg);
                }
            } catch (Exception e) {
                System.out.println("Chyba pri načítaní obrázkov: " + e.getMessage());
            }
        }
        
        public void setGame(Frogger game) {
            this.game = game;
        }
        
        public void setZaba(zaba zaba) {
            this.frog = zaba;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (game == null) return;
            
            // Draw background - grass at bottom
            g2.setColor(new Color(34, 139, 34));
            g2.fillRect(0, 0, width, height);
            
            // Draw road area
            g2.setColor(new Color(128, 128, 128));
            g2.fillRect(0, height - 300, width, 150);
            
            // Draw river area
            g2.setColor(new Color(30, 144, 255));
            g2.fillRect(0, height - 150, width, 150);
            
            // Draw grid for better visibility
            g2.setColor(new Color(100, 100, 100));
            g2.setStroke(new BasicStroke(1));
            for (int i = 0; i <= width; i += 40) {
                g2.drawLine(i, 0, i, height);
            }
            for (int i = 0; i <= height; i += 40) {
                g2.drawLine(0, i, width, i);
            }
            
            // Draw trucks
            for (Kamion truck : game.getTrucks()) {
                drawTruck(g2, truck);
            }
            
            // Draw cars
            for (Auto car : game.getCars()) {
                drawCar(g2, car);
            }
            
            // Draw logs
            for (Kmen log : game.getLogs()) {
                drawLog(g2, log);
            }
            
            // Draw turtles
            for (Korytnacka turtle : game.getRiverTurtles()) {
                drawTurtle(g2, turtle);
            }
            
            // Draw pads
            for (Lekno pad : game.getPads()) {
                drawPad(g2, pad);
            }
            
            // Draw frog
            if (frog != null) {
                drawFrog(g2, frog);
            }
            
            // Draw info text
            drawInfo(g2);
        }
        
        private void drawCar(Graphics2D g, Auto car) {
            int x = car.getX() + 400;
            int y = car.getY() + 300;
            
            if (autoImg != null) {
                g.drawImage(autoImg, x, y, 35, 25, null);
            } else {
                g.setColor(new Color(220, 20, 60));
                g.fillRect(x, y, 35, 25);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, 35, 25);
            }
        }
        
        private void drawTruck(Graphics2D g, Kamion truck) {
            int x = truck.getX() + 400;
            int y = truck.getY() + 300;
            
            if (kamionImg != null) {
                g.drawImage(kamionImg, x, y, 50, 25, null);
            } else {
                g.setColor(new Color(184, 134, 11));
                g.fillRect(x, y, 50, 25);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, 50, 25);
            }
        }
        
        private void drawLog(Graphics2D g, Kmen log) {
            int x = log.getX() + 400;
            int y = log.getY() + 300;
            
            g.setColor(new Color(139, 69, 19));
            g.fillRect(x, y, 60, 20);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(2));
            g.drawRect(x, y, 60, 20);
        }
        
        private void drawTurtle(Graphics2D g, Korytnacka turtle) {
            int x = turtle.getX() + 400;
            int y = turtle.getY() + 300;
            
            if (korytnackaImg != null) {
                g.drawImage(korytnackaImg, x, y, 40, 20, null);
            } else {
                g.setColor(new Color(85, 107, 47));
                g.fillOval(x, y, 40, 20);
                g.setColor(Color.BLACK);
                g.drawOval(x, y, 40, 20);
            }
        }
        
        private void drawPad(Graphics2D g, Lekno pad) {
            int x = pad.getX() + 400;
            int y = pad.getY() + 300;
            
            if (leknoImg != null) {
                g.drawImage(leknoImg, x, y, 30, 30, null);
            } else {
                g.setColor(new Color(144, 238, 144));
                g.fillOval(x, y, 30, 30);
                g.setColor(Color.BLACK);
                g.drawOval(x, y, 30, 30);
            }
        }
        
        private void drawFrog(Graphics2D g, zaba frog) {
            int x = frog.getX() + 400;
            int y = frog.getY() + 300;
            if (zabaImg != null) {
                g.drawImage(zabaImg, x, y, 30, 30, null);
            } else {
                g.setColor(new Color(50, 205, 50));
                g.fillOval(x, y, 30, 30);
                g.setColor(Color.BLACK);
                g.setStroke(new BasicStroke(2));
                g.drawOval(x, y, 30, 30);
                // Draw eyes
                g.setColor(Color.BLACK);
                g.fillOval(x + 8, y + 8, 4, 4);
                g.fillOval(x + 18, y + 8, 4, 4);
            }
        }
        
        private void drawInfo(Graphics2D g) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Smerčenie: " + (game.isDead() ? "KONIEC" : "V POHYBE"), 20, 30);
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            if (game != null && !game.isDead()) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        game.setAction(3);
                        break;
                    case KeyEvent.VK_DOWN:
                        game.setAction(4);
                        break;
                    case KeyEvent.VK_LEFT:
                        game.setAction(1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        game.setAction(2);
                        break;
                }
            }
        }
        
        @Override
        public void keyReleased(KeyEvent e) {}
        
        @Override
        public void keyTyped(KeyEvent e) {}
    }
}
