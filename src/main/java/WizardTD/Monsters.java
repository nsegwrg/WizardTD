package WizardTD;

import processing.core.PImage;

 /**
* Has all vital things for a monster like the other tile classes
*/

public class Monsters {
    protected String type;
    protected float hp;
    protected float speed;
    protected float armour;
    protected float mana_gained_on_kill;
    protected PImage sprite;

    /**
     * Has contructor with read in values from config file for monstersand basic setters and getters
     */

    public Monsters(String type, float hp, float speed, float armour, float mana_gained_on_kill, PImage sprite) {
        this.type = type;
        this.hp = hp;
        this.speed = speed;
        this.armour = armour;
        this.mana_gained_on_kill = mana_gained_on_kill;
        this.sprite = sprite;
    }

    public String getType() {
        return type;
    }

    public float getHp() {
        return hp;
    }

    public float getSpeed() {
        return speed;
    }

    public float getArmour() {
        return armour;
    }

    public float getManaGainedOnKill() {
        return mana_gained_on_kill;
    }
    
    public PImage getSprite() {
        return sprite;
    }
}
