package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

public class Grass extends Tile{

    /**
     * Generic tile grass call has constructor and draw method
     */
    protected final PImage sprite;

    public Grass(int x, int y, String grass, PImage sprite) {
        super(x, y, grass);
        this.sprite = sprite;
    }
    

    public void draw(PApplet app) {
        app.image(this.sprite, super.getX(), super.getY());
    }
    
}
