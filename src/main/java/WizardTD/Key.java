package WizardTD;

import processing.core.PApplet;

/**
 * Represents a key on the board in the game. 
 */

public class Key implements Draw{
  
    protected int x, y, width, height;
    protected String label;
    protected String label_inner;
    protected boolean is_active = false;

    /**
    * The constructor
    * @param x          The x-coordinate of the button's top-left corner.
    * @param y          The y-coordinate of the button's top-left corner.
    * @param sideLength The length of each side of the square button.
    * @param label      The main label of the button.
    * @param label_inner The label displayed inside the button.
    */
    public Key(int x, int y, int sideLength, String label, String label_inner) {
        this.x = x;
        this.y = y;
        this.width = sideLength;
        this.height = sideLength;
        this.label = label;
        this.label_inner = label_inner;
    }   

    /**
     * Manages the active state of the button.
     */

    public void key_update() {
        this.is_active = !this.is_active;
    }

    /**
     * Manages the drawing of the keys on the board
     */

    @Override
    public void draw(PApplet p) {
        p.stroke(0);
        p.strokeWeight(3);
        if (hovering(p.mouseX, p.mouseY) && !is_active) {
            p.fill(200); 
        } else if (is_active) {
            p.fill(255, 255, 0);
        } else {
            p.fill(191, 153, 114);
        }
        p.rect(x, y, width, height);
        p.strokeWeight(1);
        
        p.textSize(24);  
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.fill(0);
        p.text(label_inner, x + width/2, y + height/2);
        
        p.textSize(12); 
        p.textAlign(PApplet.LEFT, PApplet.CENTER);
        
        String[] labelLines = label.split(" ");
        for (int i = 0; i < labelLines.length; i++) {
            p.text(labelLines[i], x + width + 5, y + height/2 - (labelLines.length - 1) * 6 + i * 12);
        }
    }
    
    public boolean hovering(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public boolean been_clicked(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}




