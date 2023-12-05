package WizardTD;

import processing.core.PApplet;

/**
* Handles tile properties
*/


public abstract class Tile extends Object{

    protected String type;

    protected boolean empty = false;

    /**
     * Abstract class with abstract method defining tile properties has constructor with getters and setters
     */

    public Tile(int x, int y, String type){
        super(x,y);
        this.type = type;
    }

    public void tick() {}

    public void draw(PApplet app) {}

    public String getType() {
        return type;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    
}
