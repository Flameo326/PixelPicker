
package pixelpicker;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

public class PixelPicker extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private boolean running;
    private Dimension size;
    private int WIDTH;
    private int HEIGHT;
    private String currMode;
    
    private BufferedImage image;
    private int[] pixels;
    
    private MousePosition mousePos;
    private MouseInteraction mouseInter;
    private Display display;
    private Generator generator;
    private Settings settings;
    private int xTileSize;
    private int yTileSize;
    private String difficulty;
    
    
    public PixelPicker(){ 
    	pack();
        setTitle("Pixel Picker");
        setSize(size = Toolkit.getDefaultToolkit().getScreenSize());
        //setSize(size = new Dimension(1080, 720));
        //setSize(size = new Dimension(720, 480));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        setVisible(true);

        WIDTH = size.width;
        HEIGHT = size.height;
        currMode = "Title";
        running = true; 
        
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        xTileSize = WIDTH/5;
        yTileSize = HEIGHT*3/4/5;
        difficulty = "Easy";
        generator = new Generator(WIDTH, HEIGHT*3/4, xTileSize, yTileSize, getInsets(), difficulty); 
        settings = new Settings(WIDTH, HEIGHT); // Should I pass the previous variables for initialization?
        display = new Display(WIDTH, HEIGHT, getInsets(), settings); //Will somewhat glitch if generator takes a while generate, ??? make a temporary genertor display? need thread?  
        mouseInter = new MouseInteraction(currMode, display, generator, settings);
        mousePos = new MousePosition(mouseInter);
        
        addMouseListener(mousePos);
        addMouseMotionListener(mousePos);
    }
    
    public void update(){
        size = settings.getDimension();
        WIDTH = size.width;
        HEIGHT = size.height;
        setSize(size);
        
        xTileSize = settings.getXTileSize();
        yTileSize = settings.getYTileSize();
        
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        
        display.update(WIDTH, HEIGHT);
        generator.update(WIDTH, HEIGHT*3/4, xTileSize, yTileSize, difficulty);
    }
    
    @Override
    public void run() {
        while(running){
            if(settings.hasChanged()){
                update();
            }
            graphics();
        }
    }
    
    public void graphics(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            createBufferStrategy(2);
            return;
        }
        // I think i'll just check the settings class to make sure nothing has changed.
        // probs pass information and check.
        currMode = mouseInter.getMode();
        switch(currMode){
            case "Title":
                display.titleDisplay();
                break;
            case "Game":
                display.gameDisplay(generator);
                break;
            case "Settings":
                display.settingsDisplay();
                break;
            case "Help":
                display.helpDisplay();
                break;
            default:
                System.out.println("I have made a mistake somewhere");
                break;
        }
        
        System.arraycopy(display.getPixels(), 0, pixels, 0, pixels.length);
        
        Graphics g = bs.getDrawGraphics();
        
        g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
        bs.show();
        g.dispose();
    }

    public static void main(String[] args) {
        PixelPicker game = new PixelPicker();
        Thread thread = new Thread(game);
        thread.start();        
    }

    
    
}
