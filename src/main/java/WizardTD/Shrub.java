package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

public class Shrub extends Tile{

    /**
     * Generic tile shrub call has constructor and draw method
     */

    protected final PImage sprite;

    public Shrub(int x, int y, String shrub, PImage sprite) {
        super(x, y, shrub);
        this.sprite = sprite;
    }

    public void draw(PApplet app) {
        app.image(this.sprite, super.getX(), super.getY());
    }
    
}
