package pixelpicker;

import java.awt.Color;
import java.awt.Dimension;

public class Settings {

	private Dimension dimension;
	private final String[] Formats = {"RGB", "Hexadecimal", "Decimal", "Octal", "Binary"};
	private final String[] Difficulties = {"Easy", "Normal", "Hard"};
	private final Color[] colors = {Color.WHITE, Color.BLUE, Color.CYAN, Color.PINK, Color.RED, Color.MAGENTA, Color.ORANGE, Color.GREEN, Color.GRAY, Color.BLACK};
	
	private boolean hasChanged;
	private boolean isTextColored;
	private String difficulty;
	private String searchFormat;
	private Color textColor;
	private int diffCount;
	private int searchCount;
	private int colorCount;
	//private int textColor;
	private int xTileSize;
	private int yTileSize;
	
	public Settings(int width, int height){
		dimension = new Dimension(width, height);
		
		//textColor = 0xffffff;
		searchFormat = Formats[searchCount = 0];
		difficulty = Difficulties[diffCount = 0];
		textColor = colors[colorCount = 0];
	}
	
	public void toggleDiff(){
		if(diffCount++ >= Difficulties.length-1) { diffCount = 0; }
		difficulty = Difficulties[diffCount];
	}
	public void toggleSearch(){
		if(searchCount++ >= Formats.length-1) { searchCount = 0; }
		searchFormat = Formats[searchCount];
	}
	public void toggleColor(){
		if(colorCount++ >= colors.length-1) { colorCount = 0; }
		textColor = colors[colorCount];
	}
	public void toggleColoredText(){
		isTextColored = !isTextColored;
		colorCount = 0;
	}
	
	public boolean hasChanged(){
		return hasChanged;
	}
	public boolean isTextColored(){
		return isTextColored;
	}
	public int getXTileSize(){
		return xTileSize;
	}
	public int getYTileSize(){
		return yTileSize;
	}
	public String getDifficulty(){
		return difficulty;
	}
	public String getSearchFormat(){
		return searchFormat;
	}
	public String getTextColorString(){
		String s = null;
		// to make this better, I would have to make a new color class and create or override a string method.
		switch(colorCount){
		case 0:
			s = "White";
			break;
		case 1:
			s = "Blue";
			break;
		case 2:
			s = "Cyan";
			break;
		case 3:
			s = "Pink";
			break;
		case 4:
			s = "Red";
			break;
		case 5:
			s = "Magenta";
			break;
		case 6:
			s = "Orange";
			break;
		case 7:
			s = "Green";
			break;
		case 8:
			s = "Gray";
			break;
		case 9:
			s = "Black";
			break;
		default:
			System.err.println("Color could not be found. Error was made");
		}
		return s;
	}
	public Color getTextColor(){
		return textColor;
	}
	public Dimension getDimension(){
		return dimension;
	}
}
