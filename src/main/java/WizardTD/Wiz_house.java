package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

/**
* Handles things vital to the wizard house
*/

public class Wiz_house extends Tile{

    /**
     * Generic tile wizard_house call has constructor and draw method
     */

    protected final PImage sprite;
    protected int width;
    protected int height;

    public Wiz_house(int x, int y, String wiz_house, PImage sprite,int width, int height) {
        super(x, y, wiz_house);
        this.sprite = sprite;
        this.width = width;
        this.height = height;

    }

    public void draw(PApplet app) {
        app.image(this.sprite, super.getX(), super.getY(),width,height);
    }
    
}
