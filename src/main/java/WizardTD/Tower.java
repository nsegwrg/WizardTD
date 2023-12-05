package WizardTD;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

/**
* Handles everything important to the tower
*/

public class Tower extends Tile {
    public int x;
    public int y;
    float range;
    float firing_speed;
    float damage;
    int upgrade_level_range = 0;
    int upgrade_level_speed = 0;
    int upgrade_level_damage = 0;
    boolean upgrade_range = false;
    boolean upgrade_speed = false;
    boolean upgrade_damage = false;
    public int cost_range;
    public int cost_speed;
    public int cost_damage;
    public int type_tower;
    PImage tower_0;
    PImage tower_1;
    PImage tower_2;
    PImage tower_3;
    boolean isBeingPlaced = false;
    public List<Fireball> fireballs = new ArrayList<>();
    public float fire_cooldown = 0;
    public Fireball currentFireball = null;
    public static final int UPGRADE_COST_INCREMENT = 10;
    public static final int BASE_UPGRADE_COST = 20;
    public static final float RANGE_UPGRADE_AMOUNT = 32;
    public static final float SPEED_UPGRADE_AMOUNT = 0.5f;

    /**
     * Constructs a Tower with specified attributes and images for different levels.
     */

    public Tower(int x, int y, float initialRange, float initialfiring_speed, float initialDamage, PImage tower_0, PImage tower_1, PImage tower_2, PImage tower_3) {
        super(x,y,"tower");
        this.x = x;
        this.y = y;
        this.range = initialRange;
        this.firing_speed = initialfiring_speed;
        this.damage = initialDamage;
        this.tower_0 = tower_0; this.tower_1 = tower_1; this.tower_2 = tower_2; this.tower_3 = tower_3;
        
        
    }

     
    /**
     * Upgrades the range of the tower if there is enough mana
     */
    public float upgraderange(float mana_current) {
        if (mana_current >= getCostRange()) {
            range += RANGE_UPGRADE_AMOUNT;
            upgrade_level_range++;
            mana_current -= getCostRange();
            return mana_current; 
        }
        return -1;
    } public int getCostRange() {
        int cost_range = BASE_UPGRADE_COST + upgrade_level_range * UPGRADE_COST_INCREMENT;
        return cost_range;
    }

    /**
     * Calculates the cost to upgrade the tower's range
     */
    
    public float upgradespeed(float mana_current) {
        if (mana_current >= getCostSpeed()) {
            firing_speed += SPEED_UPGRADE_AMOUNT;
            upgrade_level_speed++;
            mana_current -= getCostSpeed();
            return mana_current;
        }
        return -1;
    } public int getCostSpeed() {
        int cost_speed = BASE_UPGRADE_COST + upgrade_level_speed * UPGRADE_COST_INCREMENT;
        return cost_speed;
    }

    /**
     * Upgrades the damage of the tower if there is enough mana
     */
    
    public float upgradedamage(float mana_current) {
        if (mana_current >= getCostDamage()) {
            damage += damage / 2;
            upgrade_level_damage++;
            mana_current -= getCostDamage();
            return mana_current;
        }
        return -1;
    } public int getCostDamage() {
        int cost_damage = BASE_UPGRADE_COST + upgrade_level_damage * UPGRADE_COST_INCREMENT;
        return cost_damage;
    }

    
    /**
     * Draws the tower and its range indicator when the mouse is over it. Also draws the cost of upgrades when applicablle
     */

    @Override
    public void draw(PApplet p) {
        
        if (upgrade_level_range >= 3 && upgrade_level_speed >= 3 && upgrade_level_damage >= 3){
            p.image(tower_3, x * App.CELLSIZE, y * App.CELLSIZE + App.TOPBAR, App.CELLSIZE, App.CELLSIZE);
            type_tower = 3;
        }else if (upgrade_level_range >= 2 && upgrade_level_speed >= 2 && upgrade_level_damage >= 2) {
            p.image(tower_2, x * App.CELLSIZE, y * App.CELLSIZE + App.TOPBAR, App.CELLSIZE, App.CELLSIZE);
            type_tower = 2;
        } else if (upgrade_level_range >= 1 && upgrade_level_speed >= 1 && upgrade_level_damage >= 1) {
            p.image(tower_1, x * App.CELLSIZE, y * App.CELLSIZE + App.TOPBAR, App.CELLSIZE, App.CELLSIZE);
            type_tower = 1;
        } else {
            p.image(tower_0, x * App.CELLSIZE, y * App.CELLSIZE + App.TOPBAR, App.CELLSIZE, App.CELLSIZE);
            type_tower = 0;
        }

        if (Tick.mouse_hovers(p,this)) {
            p.noFill();
            p.stroke(255, 255, 0); 
            p.ellipse(x * App.CELLSIZE + App.CELLSIZE / 2, y * App.CELLSIZE + App.CELLSIZE / 2 + App.TOPBAR, range * 2, range * 2);
            p.stroke(0); 
            if (App.upgrade_desired_range || App.upgrade_desired_speed || App.upgrade_desired_damage) {
               Draw.drawcostandUpgrades(p,this);
            }
            
        }
        if (upgrade_range) {
            upgrade_range = false;
        } if (upgrade_speed) {
            upgrade_speed = false;
        } if (upgrade_damage) {
            upgrade_damage = false;
        }
        Draw.drawupgradeSymbols(p,this);
    }

}
