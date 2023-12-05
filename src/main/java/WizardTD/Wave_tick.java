package WizardTD;

import processing.core.PApplet;

/**
* Handles logic important to wave
*/

public class Wave_tick extends Wave{

    /**
     * Generic wave logic class has constructor and getters and setters
     */

    protected Monsters monster_type;
    protected int quantity;
    static float duration;
    static float pre_wave_pause;
    static  PApplet p;

    public Wave_tick(Monsters monster_type, int quantity) {
        super(duration, pre_wave_pause,  p);
        this.monster_type = monster_type;
        this.quantity = quantity;
    }

    public Monsters getmonster_type() {
        return monster_type;
    }

    public int getquantity() {
        return quantity;
    }

    public void decrementquantity() {
        this.quantity -= 1;
    }

    
}
