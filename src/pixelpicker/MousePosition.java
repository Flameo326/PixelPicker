
package pixelpicker;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MousePosition implements MouseListener, MouseMotionListener{
    
    private int mouseX = 0;
    private int mouseY = 0;
    private MouseInteraction mouseInter;
    
    public MousePosition(MouseInteraction _mouseInter){
        mouseInter = _mouseInter;
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        mouseX = me.getX();
        mouseY = me.getY();
        System.out.println("X: " + mouseX + " Y: " + mouseY);
        mouseInter.Interaction(mouseX, mouseY, "Clicked");
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseDragged(MouseEvent me) {
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        mouseX = me.getX();
        mouseY = me.getY();       
        mouseInter.Interaction(mouseX, mouseY, "Moved");
    }
    
    
}
