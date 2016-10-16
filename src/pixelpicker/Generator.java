
package pixelpicker;

import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Random;

public class Generator {
    
    private int WIDTH, xSize, xAmount, xOffset;
    private int HEIGHT, ySize, yAmount, yOffset;
    private int[] pixels;
    private int[] tiles;
    private Random rand;
    private int randomTile;
    private boolean startingTiles;
    private String currDiff;
    
    public Generator(int width, int height, int _xSize, int _ySize, Insets inset, String difficulty){
        WIDTH = width;
        HEIGHT = height;
        xOffset = inset.left;
        yOffset = inset.top;
        pixels = new int[WIDTH*HEIGHT];
//        xSize = (WIDTH - xOffset*2)/xAmo;
//        ySize = (HEIGHT - yOffset)/yAmo;
//        xAmount = xAmo;
//        yAmount = yAmo;
        xSize = _xSize;
        ySize = _ySize;
        xAmount = (int) Math.ceil((double)(WIDTH - xOffset*2)/ xSize);
        yAmount = (int) Math.ceil((double)(HEIGHT - yOffset)/ySize);
        
        tiles = new int[xAmount*yAmount];
        
        rand = new Random();
        currDiff = difficulty;
        //scale?
    }
    
    public void update(int width, int height, int _xSize, int _ySize, String difficulty){ // Could I make it                         
        WIDTH = width;              //so I pass in an amount and calculate size from there?
        HEIGHT = height;        
        pixels = new int[WIDTH*HEIGHT];
//        xSize = WIDTH/xAmo;
//        ySize = HEIGHT/yAmo;
//        xAmount = xAmo;
//        yAmount = yAmo;
        xSize = _xSize; // should i adjust these accordidng to Height?
        ySize = _ySize;
        xAmount = (int) Math.ceil((double)(WIDTH - xOffset*2)/ xSize);
        yAmount = (int) Math.ceil((double)(HEIGHT - yOffset)/ySize);
        
        tiles = new int[xAmount*yAmount];
        currDiff = difficulty;
    }
    
    public void randomTile(){
        randomTile = tiles[rand.nextInt(tiles.length)];
    }
    
    public void generate(){
        for(int tileY = 0; tileY < yAmount; tileY++){
            for(int tileX = 0; tileX < xAmount; tileX++){
                
                int tile = tileX + tileY*xAmount;
                if(!startingTiles){
                    //difficulty doesn do anything
                    tiles[tile] = rand.nextInt(0xffffff);
                }
                
                for(int y = tileY*ySize+yOffset; y < ySize || y < HEIGHT; y++){
                    for(int x = tileX*xSize+xOffset; x < xSize || x < WIDTH-xOffset; x++){
                        
                        pixels[x + y * WIDTH] = tiles[tile];
                        
                    }
                }
            }   
        } 
        randomTile();
    }
    
    public void setDifficulty(String Diff){
        currDiff = Diff;
    }
    
    public void sample(){ // Should have 25 tiles
        update(WIDTH, HEIGHT, WIDTH/5, HEIGHT/5, currDiff); // Will need to adjust for settings
        tiles[0] = 0xff0000; tiles[1] = 0x00ff00; tiles[2] = 0x0000ff;
        tiles[3] = 0xffff00; tiles[4] = 0xff00ff; tiles[5] = 0x00ffff;
        tiles[6] = 0x000000; tiles[7] = 0x808080; tiles[8] = 0x990099;
        tiles[9] = 0xffffff; tiles[10] = 0x009999; tiles[11] = 0xff8090;
        tiles[12] = 0xdd3311; tiles[13] = 0xff99ff; tiles[14] = 0x444444;
        tiles[15] = 0xdddddd; tiles[16] = 0x993300; tiles[17] = 0x002040;
        tiles[18] = 0xf010b0; tiles[19] = 0x801050; tiles[20] = 0xff8810;
        tiles[21] = 0xaaaaaa; tiles[22] = 0x409970; tiles[23] = 0x00f070;
        tiles[24] = 0x8ba700;
        startingTiles = true;
        generate();
        startingTiles = false;
    }
    
    public int[] getPixels(){
        return pixels;
    }
    //overload????
    public int getTileValue(){
        return randomTile;
    }
    
    public int getTileValue(int tile){
        return tiles[tile];
    }
    
    public int getTile(int mouseX, int mouseY){
        int tileX = (mouseX - xOffset)/ xSize;
        int tileY = (mouseY - yOffset)/ ySize;
        return tileX + tileY*xAmount;
    }
    
    public Rectangle TiletoRectangle(int tile){
        return new Rectangle(tile%xAmount*xSize+xOffset, tile/xAmount*ySize+yOffset
                , xSize, ySize);
    }
    
    public int getHeight(){
        return HEIGHT;
    }
}
/*

Assuming that the borders are 5 pixels each that means the total width is 630 instead.

We can get 5 tiles at 126 pixels,
63 at 10
42 at 15
21 at 30
and the reverse
the problem woud be height though




*/