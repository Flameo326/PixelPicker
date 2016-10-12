
package pixelpicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Display {
    
    private int WIDTH;
    private int HEIGHT;
    private int[] pixels;
    private Settings settings;
    
    private Font titleFont; // maybe I could allow these to be changed??
    private Font wordFont;
    private Font categoryFont;
    private String searchFormat;
    private int textColor;
    
    private Rectangle playButton;
    private Rectangle helpButton;
    private Rectangle settingsButton;
    private Rectangle generateButton;
    private Rectangle tileButton;
    private Rectangle tileSelected;
    private boolean playButtonHovered;
    private boolean settingsButtonHovered;
    private boolean helpButtonHovered;
    private boolean generateButtonHovered;
    private boolean tileButtonHovered;
    private boolean selected;
    private String answerString;
    private Color hover;
    private Color darkText;

    int time = 0; // this should probably go into main class and be passed.
    
    public Display(int width, int height, Settings _settings){
        WIDTH = width;
        HEIGHT = height;
        pixels = new int[WIDTH * HEIGHT];
        settings = _settings;
        
        hover = new Color(0x5E5C5C);
        darkText = new Color(0x222222);

        //Find some way to adjust if size is changed
        titleFont = new Font("Times New Roman", Font.PLAIN, 32);
        categoryFont = new Font("Times New Roman", Font.PLAIN, 24);
        wordFont = new Font("Calibri", Font.PLAIN, 18);
        searchFormat = "RGB";
    }
    
    public void update(int width, int height){
        WIDTH = width;
        HEIGHT = height;
        pixels = new int[WIDTH * HEIGHT];
        //calculate to make sure everything is still fine.... 
    }
    
    public void titleDisplay(){
        time++;
        BufferedImage titleDisplay = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = titleDisplay.getGraphics();
        int color;
        if(settings.isTextColored()){
            color = Color.HSBtoRGB(time/360f, 1.0f, 0.5f);
        } else{
            color = settings.getTextColor();
        }
        
        playButton = new Rectangle(WIDTH/2-35, HEIGHT/2-15, 70, 20);
        settingsButton = new Rectangle(WIDTH/2-35, HEIGHT/2+5, 70, 20);
        helpButton = new Rectangle(WIDTH/2-35, HEIGHT/2+25, 70, 20);
        
        
        g.setColor(new Color(color));
        g.setFont(titleFont);
        g.drawString("Pixel Picker", (WIDTH-g.getFontMetrics().stringWidth("Pixel Picker"))/2, HEIGHT/4);        
        
        g.setFont(wordFont);
        g.setColor(hover);
        if(playButtonHovered){            
            g.fillRect(WIDTH/2-35, HEIGHT/2-15, 70, 20);
        }
        if(settingsButtonHovered){
            g.fillRect(WIDTH/2-35, HEIGHT/2+5, 70, 20);
        }
        if(helpButtonHovered){           
            g.fillRect(WIDTH/2-35, HEIGHT/2+25, 70, 20);
        }
        
        g.setColor(new Color(color));
        g.drawString("Play", (WIDTH-g.getFontMetrics().stringWidth("Play"))/2, HEIGHT/2); 
        g.drawString("Settings", (WIDTH-g.getFontMetrics().stringWidth("Settings"))/2, HEIGHT/2+20);
        g.drawString("Help", (WIDTH-g.getFontMetrics().stringWidth("Help"))/2, HEIGHT/2+40); 
        
        titleDisplay.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
        titleDisplay.flush();
    }
    
    //occasionally the generating may take a while. I could add in a state sTring in genertor class and change it to display a screen if it is generating.
    //may need to have it run on a different thread though.
    
    public void gameDisplay(Generator generator){
        time++;
        //play button refers to the game area in this situation !!!!!
        
        
        BufferedImage gameDisplay = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        gameDisplay.setRGB(0, 0, WIDTH, generator.getHeight(), generator.getPixels(), 0, WIDTH);
        Graphics g = gameDisplay.getGraphics();
        
        int color = Color.HSBtoRGB(time/360f, 1.0f, 0.5f);
        
        
        g.setColor(Color.BLACK);
        g.setFont(wordFont);
        int middlePoint = (HEIGHT-generator.getHeight())/3 + generator.getHeight()+g.getFontMetrics().getHeight()/4;
        int bottomPoint = (HEIGHT-generator.getHeight())*2/3 + generator.getHeight()+g.getFontMetrics().getHeight()/4;
        
        // play refers to Back Button...
        playButton = new Rectangle((int) (WIDTH*7/8-g.getFontMetrics().stringWidth("Back")*1.2),
                bottomPoint-g.getFontMetrics().getAscent(), 40, 20);
        generateButton =  new Rectangle(WIDTH*3/4-2, middlePoint-g.getFontMetrics().getAscent(), 150, 20);    
        settingsButton = new Rectangle(WIDTH*7/8-2, bottomPoint-g.getFontMetrics().getAscent(), 70, 20);
        helpButton = new Rectangle(WIDTH*3/4-2, bottomPoint-g.getFontMetrics().getAscent(), 40, 20);
        tileButton = new Rectangle(WIDTH*5/8-2, middlePoint-g.getFontMetrics().getAscent()*2, 80, 50);
        
        if(tileSelected != null){
            g.drawRect(tileSelected.x, tileSelected.y, tileSelected.width, tileSelected.height);
        }
        g.drawRect(0, generator.getHeight(), WIDTH, HEIGHT-generator.getHeight());        
        g.setColor(new Color(color));
        g.fillRect(5, generator.getHeight()+2, WIDTH-10, HEIGHT-generator.getHeight()-7);

        g.setColor(hover);
        if(settingsButtonHovered){
            g.fillRect(WIDTH*7/8-2, bottomPoint-g.getFontMetrics().getAscent(), 70, 20);
        }
        if(helpButtonHovered){           
            g.fillRect(WIDTH*3/4-2, bottomPoint-g.getFontMetrics().getAscent(), 40, 20);
        }
        if(generateButtonHovered){
            g.fillRect(WIDTH*3/4-2, middlePoint-g.getFontMetrics().getAscent(), 150, 20);
        }
        if(tileButtonHovered){
            g.fillRect(WIDTH*5/8-2, (int) (middlePoint-g.getFontMetrics().getAscent()*1.5), 70, 40);
        }
        if(playButtonHovered){
            g.fillRect((int) (WIDTH*7/8-g.getFontMetrics().stringWidth("Back")*1.2),
                    bottomPoint-g.getFontMetrics().getAscent(), 40, 20);
        }
        //System.out.println(middlePoint + ". " + bottomPoint + "   " + g.getFontMetrics().getHeight());
        g.setColor(Color.WHITE);
        g.drawString("Your " + searchFormat + " Value is: " + format(generator.getTileValue()), 20, middlePoint);
        g.drawString("Generate New Tiles", WIDTH*3/4, middlePoint);
        g.drawString("Settings", WIDTH*7/8, bottomPoint);
        g.drawString("Help", WIDTH*3/4, bottomPoint);
        g.drawString("Select A", WIDTH*5/8, middlePoint-g.getFontMetrics().getAscent()/2);
        g.drawString("New Tile", WIDTH*5/8, middlePoint+g.getFontMetrics().getAscent()/2);
        g.drawString("Back", (int) (WIDTH*7/8-g.getFontMetrics().stringWidth("Back")*1.2), bottomPoint);
        if(selected){
            g.drawString(answerString, 20, bottomPoint);
        }
        
        gameDisplay.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
        gameDisplay.flush();
    }
    
    public void settingsDisplay(){
        time++;
        BufferedImage settingsDisplay = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = settingsDisplay.getGraphics();
        int color;
        if(settings.isTextColored()){
            color = Color.HSBtoRGB(time/360f, 1.0f, 0.5f);
        } else{
            color = settings.getTextColor();
        }
        
        g.setFont(titleFont);
        g.setColor(new Color(color));
        g.drawString("Settings", WIDTH/2-g.getFontMetrics().stringWidth("Settings")/2, HEIGHT/7);
        
        g.setFont(categoryFont);
        g.drawString("Text", WIDTH/4-g.getFontMetrics().stringWidth("Text")*2, HEIGHT/6+g.getFontMetrics().getAscent());
        g.drawString("Program", WIDTH*2/4-g.getFontMetrics().stringWidth("Program")/2, HEIGHT/6+g.getFontMetrics().getAscent());
        g.drawString("Game", WIDTH*3/4+g.getFontMetrics().stringWidth("Game")/2, HEIGHT/6+g.getFontMetrics().getAscent());
        
        g.setFont(wordFont);
        
        playButton = new Rectangle(WIDTH/20, HEIGHT/4, 10, 10);
        if(playButtonHovered){
            g.setColor(hover);
            g.fillRect(WIDTH/20, HEIGHT/4, 10, 10);
        }
        g.setColor(new Color(color));
        g.drawRect(WIDTH/20, HEIGHT/4, 10, 10);
        g.drawString("Colored Text", WIDTH/20 + 15, HEIGHT/4+g.getFontMetrics().getAscent()*2/3);
        if(settings.isTextColored()){
            g.drawLine(WIDTH/20+2, HEIGHT/4+2, WIDTH/20+5, HEIGHT/4+8);
            g.drawLine(WIDTH/20+5, HEIGHT/4+8, WIDTH/20+8, HEIGHT/4+2);
            g.setColor(darkText);
        }       
        g.drawString("Text Color: " + Integer.toHexString(settings.getTextColor()),
                WIDTH/20, HEIGHT/4+15+g.getFontMetrics().getAscent()*2/3);        
        
        g.setColor(new Color(color));
        g.drawString("Width, Height", WIDTH*2/4-g.getFontMetrics().stringWidth("Width, Height")/2,
                HEIGHT/4+g.getFontMetrics().getAscent()*2/3);
        g.drawLine(WIDTH*2/4-g.getFontMetrics().stringWidth("Width, Height")/2-5,
                HEIGHT/4+g.getFontMetrics().getAscent()*2/3 + 5,
                WIDTH*2/4+g.getFontMetrics().stringWidth("Width, Height")/2+5,
                HEIGHT/4+g.getFontMetrics().getAscent()*2/3 + 5);    
        Dimension temp = settings.getDimension();
        g.drawString(temp.width + ", " + temp.height, 
                WIDTH*2/4-g.getFontMetrics().stringWidth(temp.width + ", " + temp.height)/2,
                HEIGHT/4+20+g.getFontMetrics().getAscent()*2/3);
            // we need to display the current W and H and add interaction
        
        
        helpButton = new Rectangle(WIDTH*3/4+g.getFontMetrics().stringWidth("Game")-g.getFontMetrics().stringWidth("Difficulty: " + settings.getDifficulty())/2+g.getFontMetrics().stringWidth("Difficulty: ")-2,
                HEIGHT/4+g.getFontMetrics().getAscent()*2/3-g.getFontMetrics().getAscent(),
                g.getFontMetrics().stringWidth(settings.getDifficulty())+5, 20);
        if(helpButtonHovered){
            g.setColor(hover);
            g.fillRect(WIDTH*3/4+g.getFontMetrics().stringWidth("Game")-g.getFontMetrics().stringWidth("Difficulty: " + settings.getDifficulty())/2+g.getFontMetrics().stringWidth("Difficulty: ")-2,
                HEIGHT/4+g.getFontMetrics().getAscent()*2/3-g.getFontMetrics().getAscent(),
                g.getFontMetrics().stringWidth(settings.getDifficulty())+5, 22);
        }       
        g.setColor(new Color(color));
        g.drawString("Difficulty: " + settings.getDifficulty(),
                WIDTH*3/4+g.getFontMetrics().stringWidth("Game")-g.getFontMetrics().stringWidth("Difficulty: " + settings.getDifficulty())/2,
                HEIGHT/4+g.getFontMetrics().getAscent()*2/3);
        // display difficulty
        
        settingsButton = new Rectangle(WIDTH*3/4+g.getFontMetrics().stringWidth("Game")-g.getFontMetrics().stringWidth("Search Mode: " + settings.getSearchFormat())/2+g.getFontMetrics().stringWidth("Search Mode: "), 
        HEIGHT/4+20+g.getFontMetrics().getAscent()*2/3-g.getFontMetrics().getAscent(),
        g.getFontMetrics().stringWidth(settings.getSearchFormat())+5, 20);
        if(settingsButtonHovered){
            g.setColor(hover);
            g.fillRect(WIDTH*3/4+g.getFontMetrics().stringWidth("Game")-g.getFontMetrics().stringWidth("Search Mode: " + settings.getSearchFormat())/2+g.getFontMetrics().stringWidth("Search Mode: "), 
        HEIGHT/4+20+g.getFontMetrics().getAscent()*2/3-g.getFontMetrics().getAscent(),
        g.getFontMetrics().stringWidth(settings.getSearchFormat())+5, 20);
        }
        g.setColor(new Color(color));
        g.drawString("Search Mode: " + settings.getSearchFormat(),
                WIDTH*3/4+g.getFontMetrics().stringWidth("Game")-g.getFontMetrics().stringWidth("Search Mode: " + settings.getSearchFormat())/2,
                HEIGHT/4+20+g.getFontMetrics().getAscent()*2/3);
        
        generateButton = new Rectangle(7, HEIGHT-10-g.getFontMetrics().getAscent(), g.getFontMetrics().stringWidth("Back"), 20);
        if(generateButtonHovered){
            g.setColor(hover);
            g.fillRect(7-2, HEIGHT-10-g.getFontMetrics().getAscent(), g.getFontMetrics().stringWidth("Back")+4, 20);
            g.setColor(new Color(color));
        }
        g.drawString("Back", 7, HEIGHT-10);
        
        settingsDisplay.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
        settingsDisplay.flush();
    }
    
    public void helpDisplay(){
        time++;
        BufferedImage helpDisplay = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        int color = (settings.isTextColored()) ? Color.HSBtoRGB(time/360f, 1.0f, 0.5f) : settings.getTextColor();
//        if(settings.getColoredText()) {
//        		color = Color.HSBtoRGB(time/360f, 1.0f, 0.5f);
//        } else{
//            color = settings.getTextColor();
//        }
        
        Graphics g = helpDisplay.getGraphics();
        g.setColor(new Color(color));
        g.setFont(titleFont);
        g.drawString("Help - How To Play", (WIDTH-g.getFontMetrics().stringWidth("Help - How To Play"))/2, HEIGHT/6);
                
        g.setFont(wordFont);
        String temp = "Hello, Welcome to Pixel Picker";
        g.drawString(temp, (WIDTH-g.getFontMetrics().stringWidth(temp))/2, HEIGHT/5 + HEIGHT/13);
        
        temp = "This game allows you to test your color knowledge.";
        g.drawString(temp, (WIDTH-g.getFontMetrics().stringWidth(temp))/2, HEIGHT/5 + HEIGHT*2/13);
        
        temp = "The game is fairly simple, all you have to do is ";
        g.drawString(temp, (WIDTH-g.getFontMetrics().stringWidth(temp))/2, HEIGHT/5 + HEIGHT*3/13);
        
        temp = "guess the color corellated to the value you are given";
        g.drawString(temp, (WIDTH-g.getFontMetrics().stringWidth(temp))/2, HEIGHT/5 + HEIGHT*7/26);
        
        temp = "You can change the form that the value is given ";
        g.drawString(temp, (WIDTH-g.getFontMetrics().stringWidth(temp))/2, HEIGHT/5 + HEIGHT*9/26);
        
        temp = "(i.e. Hexdecimal, Binary, decimal, RGB) in the settings menu.";
        g.drawString(temp, (WIDTH-g.getFontMetrics().stringWidth(temp))/2, HEIGHT/5 + HEIGHT*5/13);
        
        temp = "You can also change the screen size, difficulty,";
        g.drawString(temp, (WIDTH-g.getFontMetrics().stringWidth(temp))/2, HEIGHT/5 + HEIGHT*6/13);
        
        temp = "or amount of pixels you can guess from.";
        g.drawString(temp, (WIDTH-g.getFontMetrics().stringWidth(temp))/2, HEIGHT/5 + HEIGHT/2);
        
        temp = "If you have an issue or reccomendation,";
        g.drawString(temp, (WIDTH-g.getFontMetrics().stringWidth(temp))/2, HEIGHT/5 + HEIGHT*15/26);
        
        temp = "chances are you know who created this.";
        g.drawString(temp, (WIDTH-g.getFontMetrics().stringWidth(temp))/2, HEIGHT/5 + HEIGHT*8/13);
        
        temp = "Tell him what you think!";
        g.drawString(temp, (WIDTH-g.getFontMetrics().stringWidth(temp))/2, HEIGHT/5 + HEIGHT*17/26);
        
        temp = "Back";
        tileButton = new Rectangle(5, HEIGHT/5 + HEIGHT*20/26-g.getFontMetrics().getAscent(),
                g.getFontMetrics().stringWidth(temp)+2, 20);
        if(tileButtonHovered){
            g.setColor(hover);
            g.fillRect(5, HEIGHT/5 + HEIGHT*20/26-g.getFontMetrics().getAscent(), g.getFontMetrics().stringWidth(temp)+2, 20);
        }
        g.setColor(new Color(color));
        g.drawString(temp, 5, HEIGHT/5 + HEIGHT*20/26);
        
        temp = "Try it!";
        playButton = new Rectangle(WIDTH-g.getFontMetrics().stringWidth(temp)-12,
                HEIGHT/5 + HEIGHT*20/26-g.getFontMetrics().getAscent(), g.getFontMetrics().stringWidth(temp)+2, 20);
        if(playButtonHovered){
            g.setColor(hover);
            g.fillRect(WIDTH-g.getFontMetrics().stringWidth(temp)-12, HEIGHT/5 + HEIGHT*20/26-g.getFontMetrics().getAscent(),
                    g.getFontMetrics().stringWidth(temp)+2, 20);
        }
        g.setColor(new Color(color));
        g.drawString(temp, WIDTH-g.getFontMetrics().stringWidth(temp)-10, HEIGHT/5 + HEIGHT*20/26);
             
        helpDisplay.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
        helpDisplay.flush();
    }
    
    public String format(int tileValue){
        
        switch(searchFormat){
            case "Hexadecimal":
                return Integer.toHexString(tileValue);
            case "Binary":
                return Integer.toBinaryString(tileValue);
            case "RGB":
                Color temp = new Color(tileValue);
                return Integer.toString(temp.getRed()) + ", " + Integer.toString(temp.getGreen()) + ", " 
                        + Integer.toString(temp.getBlue());
            case "Octal":
                return Integer.toOctalString(tileValue);
            case "Decimal":
                return Integer.toString(tileValue);
            default:
                return "Error";
        }
    }
    
    public void setPlayButtonHovered(Boolean b){
    	playButtonHovered = b;
    }
    public void setSettingsButtonHovered(Boolean b){
    	settingsButtonHovered = b;
    }
    public void setHelpButtonHovered(Boolean b){
    	helpButtonHovered = b;
    }
    public void setGenerateButtonHovered(Boolean b){
    	generateButtonHovered = b;
    }
    public void setTileButtonHovered(Boolean b){
    	tileButtonHovered = b;
    }
    public void setSelected(Boolean b){
    	selected = b;
    } 
    public void setSearchFormat(String s){
        searchFormat = s; //Maybe better if I use enum... 
    }
    public void setAnswerString(String s){
    	answerString = s;
    }
    public void setTileSelected(Rectangle r){
    	tileSelected = r;
    }
    
    public int[] getPixels(){
        return pixels;
    }
    public Rectangle getPlayButton(){
        return playButton;
    }
    public Rectangle getSettingsButton(){
        return settingsButton;
    }
    public Rectangle getHelpButton(){
        return helpButton;
    }
    public Rectangle getGenerateButton(){
        return generateButton;
    }
    public Rectangle getTileButton(){
        return tileButton;
    }
}
