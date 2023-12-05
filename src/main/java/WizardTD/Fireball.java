package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

/**
* Handles everything vital for firebalss
*/

public class Fireball implements Draw{
    float x, y;
    Mon_move target_monster;
    float damage;
    float speed = 5;
    PApplet p;
    PImage fireball_img;

     /**
     * Constructor
     */

    public Fireball(float x, float y, Mon_move target_monster, float damage, PApplet p, PImage fireball_img) {
        this.x = x;
        this.y = y;
        this.target_monster = target_monster;
        this.damage = damage;
        this.p = p;
        this.fireball_img = fireball_img;
    }
     /**
     * Moves the fireball towards its target monster
     */
    public void move_fireball() {
        float targetX = target_monster.getmonX() * App.CELLSIZE;
        float targetY = target_monster.getmonY() * App.CELLSIZE + App.TOPBAR;
        float distance = PApplet.dist(x, y, targetX, targetY);
        float dx = (targetX - x) / distance;
        float dy = (targetY - y) / distance;
        x += dx * speed;
        y += dy * speed;
    }

     /**
     * Determines whether the fireball has reached its target monster
     */
    public boolean reached_Destination() {
        float distance = PApplet.dist(x, y, target_monster.getmonX() * App.CELLSIZE, target_monster.getmonY() * App.CELLSIZE + App.TOPBAR);
        return distance < speed;
    }

    /**
     * Draws Fireball
     */
    @Override
    public void draw(PApplet p) {
        p.image(fireball_img, x, y);
    }
}

