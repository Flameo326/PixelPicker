
package pixelpicker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Display {
    
    private int WIDTH;
    private int HEIGHT;
    private int xOffset;
    private int yTopOffset;
    private int yBotOffset;
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
    
    public Display(int width, int height, Insets inset, Settings _settings){
        WIDTH = width;
        HEIGHT = height;
        xOffset = inset.left;
        yTopOffset = inset.top;
        yBotOffset = inset.bottom;
        pixels = new int[WIDTH * HEIGHT];
        settings = _settings;
        
        hover = new Color(0x5E5C5C);
        darkText = new Color(0x222222);

        titleFont = new Font("Times New Roman", Font.PLAIN, (int) (width *.0444));
        categoryFont = new Font("Times New Roman", Font.PLAIN, (int) (width * .0333));
        wordFont = new Font("Calibri", Font.PLAIN, (int) (width * .025));
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
        Color color = (settings.isTextColored()) ? new Color(Color.HSBtoRGB(time/360f, 1.0f, 0.5f)) : settings.getTextColor();   
        
        g.setColor(color);
        g.setFont(titleFont);
        g.drawString("Pixel Picker", (WIDTH-g.getFontMetrics().stringWidth("Pixel Picker"))/2, HEIGHT/4);        
        
        g.setFont(wordFont);        
        playButton = drawButton(g, "Play", WIDTH/2-2*xOffset, HEIGHT/2+yTopOffset-yBotOffset-g.getFontMetrics().getHeight(), color, playButtonHovered);
        settingsButton = drawButton(g, "Settings", WIDTH/2-2*xOffset, HEIGHT/2+yTopOffset-yBotOffset, color, settingsButtonHovered);
        helpButton = drawButton(g, "Help", WIDTH/2-2*xOffset, HEIGHT/2+yTopOffset-yBotOffset+g.getFontMetrics().getHeight(), color, helpButtonHovered);
        
        titleDisplay.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
        titleDisplay.flush();
    }
    
    //occasionally the generating may take a while. I could add in a state sTring in genertor class and change it to display a screen if it is generating.
    //may need to have it run on a different thread though.
    
    public void gameDisplay(Generator generator){
        time++;
        BufferedImage gameDisplay = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        gameDisplay.setRGB(0, 0, WIDTH, generator.getHeight(), generator.getPixels(), 0, WIDTH);
        Graphics g = gameDisplay.getGraphics();
        Color color = (settings.isTextColored()) ? new Color(Color.HSBtoRGB(time/360f, 1.0f, 0.5f)) : settings.getTextColor();
     
        g.setColor(Color.BLACK);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke((WIDTH/1000)+1));
        if(tileSelected != null) {g2.drawRect(tileSelected.x, tileSelected.y, tileSelected.width, tileSelected.height); }
        
        g.setColor(new Color(Color.HSBtoRGB(~time/360f, 1.0f, 0.5f)));
        Rectangle infoArea = new Rectangle(2*xOffset, generator.getHeight()+yBotOffset, WIDTH-4*xOffset, HEIGHT-generator.getHeight()-3*yBotOffset);
        g.fillRect(infoArea.x, infoArea.y, infoArea.width, infoArea.height);
        
        // play refers to Back Button...
        // May need to change the color for based on User. Could make background different color as well... 
        g.setFont(wordFont); 
        g.setColor(color);
        String temp = "Settings";
        settingsButton = drawButton(g, temp, (int) (WIDTH*.98-g.getFontMetrics().stringWidth(temp)/2), (int) (HEIGHT*.95-g.getFontMetrics().getAscent()/2), color, settingsButtonHovered);
        temp = "Back";
        playButton = drawButton(g, temp, (int)(settingsButton.x-g.getFontMetrics().stringWidth(temp)/2-HEIGHT*.07), (int)settingsButton.getCenterY(), color, playButtonHovered);
        temp = "Help";
        helpButton = drawButton(g, temp, (int)(playButton.x-g.getFontMetrics().stringWidth(temp)/2-HEIGHT*.07), (int)settingsButton.getCenterY(), color, helpButtonHovered);
        temp = "Generate New Tiles";
        generateButton = drawButton(g, temp, (int) (WIDTH*.98-g.getFontMetrics().stringWidth(temp)/2), (int)(settingsButton.y-g.getFontMetrics().getAscent()-HEIGHT*.02), color, generateButtonHovered);
        temp = "New Tile";
        tileButton = drawButton(g, temp, (int)(generateButton.x-g.getFontMetrics().stringWidth(temp)/2-HEIGHT*.02), (int)generateButton.getCenterY()+g.getFontMetrics().getAscent()/2, color, tileButtonHovered);
        temp = "Select A";
        tileButton = tileButton.union(drawButton(g, temp, (int)tileButton.getCenterX(), (int)generateButton.getCenterY()-g.getFontMetrics().getAscent()/2, color, tileButtonHovered));
        
        g.drawString("Your " + searchFormat + " Value is: " + format(generator.getTileValue()), (int) (infoArea.x+WIDTH*.02), (int) (generateButton.getCenterY()+g.getFontMetrics().getAscent()/2));
        if(selected){
            g.drawString(answerString, (int) (infoArea.x+WIDTH*.02), (int) (settingsButton.getCenterY()+g.getFontMetrics().getAscent()/2));
        }
        
        gameDisplay.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
        gameDisplay.flush();
    }
    
    public void settingsDisplay(){
        time++;
        BufferedImage settingsDisplay = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = settingsDisplay.getGraphics();
        Color color = (settings.isTextColored()) ? new Color(Color.HSBtoRGB(time/360f, 1.0f, 0.5f)) : settings.getTextColor();
        FontMetrics fm;
        String temp;
        
        g.setColor(color);
        g.setFont(titleFont);
        fm = g.getFontMetrics();
        g.drawString("Settings", WIDTH/2-g.getFontMetrics().stringWidth("Settings")/2, 2*yTopOffset+g.getFontMetrics().getAscent());
        
        g.setFont(categoryFont);
        fm = g.getFontMetrics();
        int textPos = WIDTH/4+fm.stringWidth("Text")/2; //
        int progPos = WIDTH/2; // Center
        int gamePos = WIDTH*3/4-fm.stringWidth("Game")/2;
        g.drawString("Text", textPos-fm.stringWidth("Text"), HEIGHT/6+fm.getAscent());
        g.drawString("Program", progPos-fm.stringWidth("Program")/2, HEIGHT/6+fm.getAscent());
        g.drawString("Game", gamePos, HEIGHT/6+fm.getAscent());
        
        g.setFont(wordFont);
        fm = g.getFontMetrics();
        temp = "Difficulty: ";
        g.drawString(temp, gamePos, HEIGHT/6+2*fm.getHeight());
        
        temp = settings.getDifficulty();
        helpButton = drawButton(g, temp, gamePos+fm.stringWidth("Difficulty: ")+fm.stringWidth(temp)/2, HEIGHT/6+2*fm.getHeight()-fm.getAscent()/2, color, helpButtonHovered);
        
        temp = "Search Mode: ";
        g.drawString(temp, gamePos, HEIGHT/6+3*fm.getHeight());
        
        temp = settings.getSearchFormat();
        settingsButton = drawButton(g, temp, gamePos+fm.stringWidth("Search Mode: ")+fm.stringWidth(temp)/2, HEIGHT/6+3*fm.getHeight()-fm.getAscent()/2, color, settingsButtonHovered);
        
        temp = "Back";
        generateButton = drawButton(g, temp, g.getFontMetrics().stringWidth(temp)/2+2*xOffset, HEIGHT-4*yBotOffset-g.getFontMetrics().getAscent()/2, color, generateButtonHovered);
        
        temp = "  "; // Box placement and manual setup
        playButton = new Rectangle(textPos-fm.stringWidth(temp), HEIGHT/6+2*fm.getHeight()-fm.stringWidth(temp), fm.stringWidth(temp), fm.stringWidth(temp));
        
        temp = "Colored Text";
        g.drawString(temp, playButton.x-fm.stringWidth(temp + " "), playButton.y+playButton.height);
        
        g.drawRect(playButton.x, playButton.y, playButton.width, playButton.height);
        if(playButtonHovered){ // Hovering
          g.setColor(hover);
          g.fillRect(playButton.x, playButton.y, playButton.width, playButton.height);
          g.setColor(color);
        }
        if(settings.isTextColored()){ // If text is colored
        	Graphics2D g2 = (Graphics2D) g;
        	g2.setStroke(new BasicStroke((WIDTH/1000)+1));
        	g2.drawLine(playButton.x-playButton.width/4, playButton.y+playButton.height/4, playButton.x+playButton.width/2, playButton.y+playButton.height*5/6);
	        g2.drawLine(playButton.x+playButton.width/2, playButton.y+playButton.height*5/6, playButton.x+playButton.width*4/3, playButton.y-playButton.height/4);
	        g.setColor(darkText);
        }
        
        temp = settings.getTextColorString();
        tileButton = drawButton(g, temp, textPos-fm.stringWidth(temp)/2, HEIGHT/6+3*fm.getHeight()-fm.getAscent()/2, g.getColor(), (settings.isTextColored()) ? false : tileButtonHovered);
        
        temp = "Text Color: " + temp;
        g.drawString("Text Color: ", textPos-fm.stringWidth(temp), HEIGHT/6+3*fm.getHeight());                
        
        g.setColor(color);
        temp = "Width, Height";
        g.drawString(temp, progPos-g.getFontMetrics().stringWidth(temp)/2, HEIGHT/6+2*fm.getHeight());
        g.drawLine(progPos-g.getFontMetrics().stringWidth(temp), HEIGHT/6+2*fm.getHeight()+yBotOffset,
        		progPos+g.getFontMetrics().stringWidth(temp), HEIGHT/6+2*fm.getHeight()+yBotOffset); 
        
        temp = WIDTH + " " + HEIGHT;
        tileSelected = drawButton(g, temp, progPos, HEIGHT/6+3*fm.getHeight()-fm.getAscent()/2, color, selected);
//        Dimension dimension = settings.getDimension();
//        g.drawString(dimension.width + ", " + dimension.height, 
//                WIDTH*2/4-g.getFontMetrics().stringWidth(dimension.width + ", " + dimension.height)/2,
//                HEIGHT/4+20+g.getFontMetrics().getAscent()*2/3);
            // we need to display the current W and H and add interaction
        
        settingsDisplay.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
        settingsDisplay.flush();
    }
    
    public void helpDisplay(){
        time++;     
        BufferedImage helpDisplay = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = helpDisplay.getGraphics();
        Color color = (settings.isTextColored()) ? new Color(Color.HSBtoRGB(time/360f, 1.0f, 0.5f)) : settings.getTextColor();
        
        g.setColor(color);
        g.setFont(titleFont);
        g.drawString("Help - How To Play", (WIDTH-g.getFontMetrics().stringWidth("Help - How To Play"))/2, HEIGHT/6);
                
        g.setFont(wordFont);
        String temp = "Hello, Welcome to Pixel Picker";
        g.drawString(temp, (WIDTH-g.getFontMetrics().stringWidth(temp))/2, HEIGHT/5 + HEIGHT/13);
        
        temp = "This game allows you to test your color knowledge.";
        g.drawString(temp, (WIDTH-g.getFontMetrics().stringWidth(temp))/2, HEIGHT/5 + HEIGHT*2/13);
        
        temp = "The game is fairly simple, all you have to do is ";
        g.drawString(temp, (WIDTH-g.getFontMetrics().stringWidth(temp))/2, HEIGHT/5 + HEIGHT*3/13);
        
        temp = "guess the color corellated to the value you are given.";
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
        tileButton = drawButton(g, temp, g.getFontMetrics().stringWidth(temp)/2+2*xOffset, HEIGHT-4*yBotOffset-g.getFontMetrics().getAscent()/2, color, tileButtonHovered);
        
        temp = "Try it!";
        playButton = drawButton(g, temp, WIDTH-3*xOffset-g.getFontMetrics().stringWidth(temp)/2, HEIGHT-4*yBotOffset-g.getFontMetrics().getAscent()/2, color, playButtonHovered);    
        
        helpDisplay.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
        helpDisplay.flush();
    }

    public Rectangle drawButton(Graphics g, String s, int midX, int midY, Color c, boolean buttonHover){
    	FontMetrics fm = g.getFontMetrics();
    	int sWidth = fm.stringWidth(s);
    	int sAscent = fm.getAscent();
    	
    	int xPoint = midX-sWidth/2;
    	int yPoint = midY-sAscent/2;
    	int width = sWidth;
    	int height = sAscent;  	
    	Rectangle rect = new Rectangle(xPoint, yPoint, width, height);
    	
		if(buttonHover){
            g.setColor(hover);
            g.fillRect(rect.x, (int) (rect.y+sAscent*.1), rect.width, rect.height);
		}	
    	g.setColor(c);
        g.drawString(s, rect.x, rect.y + sAscent);
    			
    	return rect;
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
