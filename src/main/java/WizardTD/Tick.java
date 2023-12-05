package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

public interface Tick {

    /**
     * Checks if a tower is present at the specified grid position
     */

    static boolean tower_Placed(int x, int y, App app) {
        for (Tower tower : app.towers) {
            if (tower.getX() == x && tower.getY() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Toggles the game speed between normal and fast-forward when the fast-forward button is activated
     */

    static void onfastforwardKey(App app) {
        if (App.game_speed == 1.0f) {
            App.game_speed = 2.0f;
        } else {
            App.game_speed = 1.0f;
        } app.fast_forward_button.key_update();
    }

    /**
     * Activates the tower upgrades selection if selected
     */
    static void onUpgradesKey(App app) {
        for (Tower tower : app.towers) {
            if (Tick.mouse_hovers(app, tower)) {
                if (App.upgrade_desired_range) {
                    float newMana = tower.upgraderange(app.mana_current);
                    if (newMana != -1) {
                        app.mana_current = newMana;
                        app.button_upgrade_range.key_update();
                        App.upgrade_desired_range = false;
                    }
                }
                if (App.upgrade_desired_speed) {
                    float newMana = tower.upgradespeed(app.mana_current);
                    if (newMana != -1) {
                        app.mana_current = newMana;
                        app.button_upgrade_speed.key_update();
                        App.upgrade_desired_speed = false;
                    }
                }
                if (App.upgrade_desired_damage) {
                    float newMana = tower.upgradedamage(app.mana_current);
                    if (newMana != -1) {
                        app.mana_current = newMana;
                        app.button_upgrade_damage.key_update();
                        App.upgrade_desired_damage = false;
                    }
                }
            }
        }
    }

    /**
     * Toggles the tower building mode when the build tower button is activated
     */

    static void onbuildtower(App app) {
        if (app.tower_place) {
            app.tower_place = false;
            app.button_build_tower.key_update();  
            } else {
                app.button_build_tower.key_update();
                app.tower_place = true;
            }
    }

     /**
     * Activates the mana pool spell which increases the mana cap and regen rate and buys from mana
     */

    static void onmanaspell(App app) {
        if (app.mana_current >= app.mana_cost_initial_pool_spell) {
            app.mana_current -= app.mana_cost_initial_pool_spell;
            app.mana_cost_initial_pool_spell += app.mana_cost_increase_pool_spell; 
            app.mana_cap *= app.mana_cap_mult_pool_spell; 
            app.mana_regenerate_rate += app.mana_regenerate_rate * app.mana_gain_mult_pool_spell;
        }
    }

     /**
     * Determines if the mouse is currently over a given tower
     */

    static boolean mouse_hovers(PApplet p, Tower tower) {
        int mouseX = p.mouseX;
        int mouseY = p.mouseY;
        return mouseX >= tower.x * App.CELLSIZE && mouseX <= (tower.x + 1) * App.CELLSIZE && mouseY >= tower.y * App.CELLSIZE + App.TOPBAR && mouseY <= (tower.y + 1) * App.CELLSIZE + App.TOPBAR;
    }

    /**
     * Fires a fireball from a tower at a monster
     */

    static void fire(Mon_move monster, PApplet p, PImage fireball_img, Tower tower) {
        Fireball fireball = new Fireball(tower.x * App.CELLSIZE + App.CELLSIZE/2, tower.y * App.CELLSIZE + App.CELLSIZE/2 + App.TOPBAR, monster, tower.damage, p, fireball_img);
        tower.fireballs.add(fireball);
    }

     /**
     * Updates the wave's state, advancing its timers according to the game's delta time.
     */
    static void updatewave(float delta, Wave wave) {
        if (wave.pre_wave_pause_time < wave.pre_wave_pause) {
            wave.pre_wave_pause_time += delta;
            return;
        }
        wave.time_elapsed += delta;
        wave.time_since_last_spawn += delta;
    }

    /**
     * Adds a monster type to be spawned in a wave a certain number of times
     */
    static void addmon(Monsters monster_type, int quantity, Wave wave) {
        wave.monsters_to_spawn.add(new Wave_tick(monster_type, quantity));
    }

    /**
     * Determines if it is time to spawn a new monster in the wave based on the time since the last spawn
     */
    static boolean canspawn(Wave wave) {
        if (wave.time_since_last_spawn >= wave.duration/wave.count_total_monster && !wave.monsters_to_spawn.isEmpty()) {
            wave.time_since_last_spawn = 0;
            return true;
        }
        return false;
    }
}
