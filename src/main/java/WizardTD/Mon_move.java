package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;


/**
* Handles everything to do with movement of monsters
*/

public class Mon_move implements Draw{
    public static final int frames_per_death_img = 4;
    protected float x, y; 
    protected Monsters type;
    protected App app;
    protected float hp_initial;  
    protected float hp_current;  
    protected int animation_frame_death = 0;
    public int index_img = 0;
    public float size_adjusted = 0.8f * App.CELLSIZE;
    public float offset_x = ((App.CELLSIZE - size_adjusted) / 2)+1;
    public float offset_y = ((App.CELLSIZE - size_adjusted) / 2)+1;
    protected char last_dir = ' ';

    public boolean is_dying = false;
    protected boolean mana_given = false;

     
    /**
     * Constructor for creating a Monster with its initial properties.
     */
    public Mon_move(float spawnX, float spawnY, Monsters type, App app, int wizard_house_X, int wizard_house_Y) {
        this.x = spawnX;
        this.y = spawnY;
        this.type = type;
        this.app = app;
        this.hp_initial = type.getHp();
        this.hp_current = hp_initial;
    }

    /**
     * Draws the monster and its health bar 
     */

    @Override
    public void draw(PApplet p) {
        
        app.image(type.getSprite(), x * App.CELLSIZE + offset_x, y * App.CELLSIZE + App.TOPBAR + offset_y, 20, 20);
    
        float healthBarWidth = 20;  
        float healthBarHeight = 3;  
        float healthBarX = x * App.CELLSIZE + offset_x;
        float healthBarY = y * App.CELLSIZE + App.TOPBAR + offset_y - healthBarHeight - 2;  
        float currentHealthPercentage = hp_current / hp_initial;
    
        app.fill(255, 0, 0);  
        app.rect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);
        app.fill(0, 255, 0); 
        app.rect(healthBarX, healthBarY, healthBarWidth * currentHealthPercentage, healthBarHeight);
    }

    /**
     * Moves the monster along the path towards the wizard's house
     */

    public void move_monster() {
        float moveAmount = (type.getSpeed() * App.game_speed) / App.FPS; 
        float edgeX = Math.round(x) - offset_x;  
        float edgeY = Math.round(y) - offset_y;

        char direction = app.path_directions[Math.round(y)][Math.round(x)];
        Runnable continuelast_direction = () -> {
            switch (last_dir) {
                case 'U': y -= moveAmount; break;
                case 'D': y += moveAmount; break;
                case 'L': x -= moveAmount; break;
                case 'R': x += moveAmount; break;
            }
        };
            if (last_dir == ' ') {
                last_dir = direction;
            }
            switch (direction) {
                case 'U':
                    if (last_dir == 'L') {
                        if (x - offset_x < edgeX) {
                        
                            last_dir = 'U';
                            y -= moveAmount;
                        } else {
                            continuelast_direction.run();
                        }
                    } else if (last_dir == 'R') {
                        if (x - offset_x > edgeX) {
                        
                            last_dir = 'U';
                            y -= moveAmount;
                        } else {
                            continuelast_direction.run();
                        }
                    }
                    
                     else {
                        continuelast_direction.run();
                    }
                    break;

                case 'D':
                    if (last_dir == 'L') {
                        if (x - offset_x <= edgeX) {
                        
                            last_dir = 'D';
                            y += moveAmount;
                        } else {
                            continuelast_direction.run();
                        }
                    } else if (last_dir == 'R') {
                        if (x - offset_x >= edgeX) {
                        
                            last_dir = 'D';
                            y += moveAmount;
                        } else {
                            continuelast_direction.run();
                        }
                    }
                    
                     else {
                        continuelast_direction.run();
                    }
                    break;

                case 'L':
                    if (last_dir == 'U') {
                        if (y - offset_y <= edgeY) {
                        
                            last_dir = 'L';
                            x -= moveAmount;
                        } else {
                            continuelast_direction.run();
                        }
                    } else if (last_dir == 'D') {
                        if (y - offset_y >= edgeY) {
                        
                            last_dir = 'L';
                            x -= moveAmount;
                        } else {
                            continuelast_direction.run();
                        }
                    }
                    
                     else {
                        continuelast_direction.run();
                    }
                    break;
                case 'R':
                    if (last_dir == 'U') {
                        if (y - offset_y <= edgeY) {
                        
                            last_dir = 'R';
                            x += moveAmount;
                        } else {
                            continuelast_direction.run();
                        }
                    } else if (last_dir == 'D') {
                        if (y - offset_y >= edgeY) {
                        
                            last_dir = 'R';
                            x += moveAmount;
                        } else {
                            continuelast_direction.run();
                        }
                    }
                    
                     else {
                        continuelast_direction.run();
                    }
                    break;
            }
      
    }

    /**
     * Checks the position
     */

    public float[] getmonster_Pos() {
        return new float[]{x, y};
    }

    /**
     * Reduces the monster's health by the given damage amount
     */
    public float monster_health(float damage) {
        this.hp_current -= damage * type.getArmour();
        if (this.hp_current < 0) {
            this.hp_current = 0; 
            is_dying = true;
            mana_given = false;
            return 0;
        }
        return this.hp_current;
    }

    /**
     * Provides the image for the monster's death animation based on its current animation frame
     */

    public PImage get_monster_death() {
        if (is_dying) {
            
            animation_frame_death++;
    
            if (animation_frame_death > frames_per_death_img / App.game_speed) {
                is_dying = false;
                animation_frame_death = 0;
                index_img++;
                return null;
            }
    
            if (index_img < 5) {
                return App.death_image[index_img];
            }
        }
        return null;
    }

    /**
     * Basic setters and getters
     */

    public float getmonX() {
        return x;
    }

    public float getmonY() {
        return y;
    }

    public float getWidth() {
        return offset_x;
    }

    public float getHeight() {
        return offset_y;
    }

    public Monsters getType() {
        return type;
    }
    
    public float getcurr_hp() {
        return hp_current;
    }  

    public boolean get_mana_gain() {
        return mana_given;
    }

    public void set_mana_gain(boolean given) {
        this.mana_given = given;
    }
}