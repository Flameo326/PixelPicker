
package pixelpicker;

public class MouseInteraction {
    
    private String currMode, prevMode;
    private String action;
    private int mouseX;
    private int mouseY;
    private Display display;
    private Generator generator;
    private Settings settings;
    
    public MouseInteraction(String _currMode, Display _display, Generator _generator, Settings _settings){
        currMode = _currMode;
        display = _display;
        generator = _generator;
        settings = _settings;
    }
    // Bug right now. Opening  the game with the mouse on it creates a null pointer exception.
    // also can not click when moving mouse.
    // To fix????  make two seperate mouse classes for moving and clicking?
    public void Interaction(int _mouseX, int _mouseY, String _action){
        action = _action;
        mouseX = _mouseX;
        mouseY = _mouseY;
        
        switch(currMode){
            case "Title":                
                interactTitle();
                break;
            case "Game":
                interactGame();
                break;
            case "Settings":
                interactSettings();
                break;
            case "Help":
                interactHelp();
                break;
            default:
                System.out.println("I have made a mistake somewhere");
                break;
        }
    }
    
    private void interactTitle(){
        if("Clicked".equals(action)){
            if(display.getPlayButton().contains(mouseX, mouseY)){
                prevMode = currMode;
                currMode = "Game";
                reset();
            }
            if(display.getHelpButton().contains(mouseX, mouseY)){
                prevMode = currMode;
                currMode = "Help";
                reset();
            }
            if(display.getSettingsButton().contains(mouseX, mouseY)){
                prevMode = currMode;
                currMode = "Settings";
                reset();
            }
        } 
        // These are all sets, can implement proper methods.
        if("Moved".equals(action)){
            display.setPlayButtonHovered(display.getPlayButton().contains(mouseX, mouseY));
            display.setHelpButtonHovered(display.getHelpButton().contains(mouseX, mouseY));
            display.setSettingsButtonHovered(display.getSettingsButton().contains(mouseX, mouseY));
        }
    }
    
    private void interactGame(){
        if("Clicked".equals(action)){ 
            if(mouseY < generator.getHeight()){
                int answer = generator.getTileValue(generator.getTile(mouseX, mouseY));
                if(answer == generator.getTileValue()){
                    display.setAnswerString("Correct! That's the right color! Good job.");
                } else{
                    display.setAnswerString("Oops! That color is: " + display.format(answer) + ". Try again.");
                }
                display.setSelected(true);
            }
            
            if(display.getGenerateButton().contains(mouseX, mouseY)){
                generator.generate();
            }
            if(display.getHelpButton().contains(mouseX, mouseY)){
                prevMode = currMode;
                currMode = "Help";
                reset();
            }
            if(display.getSettingsButton().contains(mouseX, mouseY)){
                prevMode = currMode;
                currMode = "Settings";
                reset();
            }
            if(display.getTileButton().contains(mouseX, mouseY)){
                generator.randomTile();                
            }
            if(display.getPlayButton().contains(mouseX, mouseY)){
                currMode = "Title";
                reset();
            }
        } 
        if("Moved".equals(action)){        
            if(mouseY < generator.getHeight()){
                display.setTileSelected(generator.TiletoRectangle(generator.getTile(mouseX, mouseY)));
            }else{
                display.setTileSelected(null);
            }
            display.setPlayButtonHovered(display.getPlayButton().contains(mouseX, mouseY));
            display.setHelpButtonHovered(display.getHelpButton().contains(mouseX, mouseY));
            display.setSettingsButtonHovered(display.getSettingsButton().contains(mouseX, mouseY));
            display.setGenerateButtonHovered(display.getGenerateButton().contains(mouseX, mouseY));
            display.setTileButtonHovered(display.getTileButton().contains(mouseX, mouseY));
        }
    }
    
    private void interactSettings(){
        if("Clicked".equals(action)){
            if(display.getPlayButton().contains(mouseX, mouseY)){
                settings.toggleColoredText();
            }
            if(display.getHelpButton().contains(mouseX, mouseY)){
                settings.toggleDiff();
                generator.setDifficulty(settings.getDifficulty());
                generator.generate();
            }
            if(display.getSettingsButton().contains(mouseX, mouseY)){
                settings.toggleSearch();
                display.setSearchFormat(settings.getSearchFormat());
            }
            if(display.getGenerateButton().contains(mouseX, mouseY)){
                currMode = prevMode;
                reset();
            }
        } 
        if("Moved".equals(action)){
            display.setPlayButtonHovered(display.getPlayButton().contains(mouseX, mouseY));
            display.setHelpButtonHovered(display.getHelpButton().contains(mouseX, mouseY));
            display.setSettingsButtonHovered(display.getSettingsButton().contains(mouseX, mouseY));
            display.setGenerateButtonHovered(display.getGenerateButton().contains(mouseX, mouseY));
            //display.tileButtonHovered = display.getTileButton().contains(mouseX, mouseY);
        }
    }
    
    private void interactHelp(){
        if("Clicked".equals(action)){
            if(display.getPlayButton().contains(mouseX, mouseY)){
                generator.sample();
                currMode = "Game";
                reset();
            }
            if(display.getTileButton().contains(mouseX, mouseY)){                
                currMode = prevMode;
                reset();
            }
        } 
        if("Moved".equals(action)){
            display.setPlayButtonHovered(display.getPlayButton().contains(mouseX, mouseY));
            display.setTileButtonHovered(display.getTileButton().contains(mouseX, mouseY));
        }
    }
    
    private void reset(){
        display.setPlayButtonHovered(false);
        display.setTileButtonHovered(false);
        display.setHelpButtonHovered(false);
        display.setSettingsButtonHovered(false);
        display.setGenerateButtonHovered(false);
        display.setSelected(false);
    }
    
    public String getMode(){
        return currMode;
    }
    
}
