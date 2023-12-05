package WizardTD;

public abstract class Object {
    /**
     * Parent of all static tiles
     */


    protected int y;
    
    protected int x;

    /**
     * Has contructor and basic setters and getters
     */

    public Object(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
      
}
