package pixelpicker;

import java.awt.Dimension;

public class Settings {

	private Dimension dimension;
	private final String[] Formats = {"RGB", "Hexadecimal", "Decimal", "Octal", "Binary"};
	private final String[] Difficulties = {"Easy", "Normal", "Hard"};
	
	private boolean hasChanged;
	private boolean isTextColored;
	private String difficulty;
	private String searchFormat;
	private int diffCount;
	private int searchCount;
	private int textColor;
	private int xTileSize;
	private int yTileSize;
	
	public Settings(int width, int height){
		dimension = new Dimension(width, height);
		
		textColor = 0xffffff;
		searchFormat = Formats[searchCount = 0];
		difficulty = Difficulties[diffCount = 0];
	}
	
	public void toggleDiff(){
		if(diffCount++ >= Difficulties.length) { diffCount = 0; }
		difficulty = Difficulties[diffCount];
	}
	public void toggleSearch(){
		if(searchCount++ >= Formats.length) { searchCount = 0; }
		searchFormat = Formats[searchCount];
	}
	public void toggleColoredText(){
		isTextColored = !isTextColored;
	}
	
	public boolean hasChanged(){
		return hasChanged;
	}
	public boolean isTextColored(){
		return isTextColored;
	}
	public int getTextColor(){
		return textColor;
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
	public Dimension getDimension(){
		return dimension;
	}
}
