package WizardTD;

import processing.core.PImage;
import java.util.*;
import processing.core.PApplet;


public interface Draw {

    /**
     * Draws the game's background. This is called at the start of the game
     */

    static void drawBackground(App app) {
        app.fill(191, 153, 114);
        app.rect(-1, -1, App.WIDTH+2 , App.HEIGHT+2 );
    }

    /**
     * Draws the back layer of the game board. It provides a specific color for the area where the game tiles are displayed.
     */

    static void drawBack(App app) {
        app.fill(133, 171, 128);
        app.rect(1, 40, App.WIDTH-120 , App.HEIGHT+40 );
    }

    /**
     * Draws all the tiles on the game board. It iterates over the grid and calls the draw method of each tile
     * @param app 
     */
    static void drawTiles(App app) {
        for (int x = 0; x < app.grid.getXLength(); x++) {
            for (int y = 0; y < app.grid.getYLength(); y++) {
                Tile tile = app.grid.getTile(x, y);
                tile.tick();
                if (!tile.isEmpty()) {
                    tile.draw(app);
                }
            }
        }
    }

    /**
     * Manages the logic and drawing related to the waves of monsters, including spawning monsters and managing mana
     */

    static void drawWaveTick(App app){
        if (!App.game_pause) {
            app.mana_current = App.min(app.mana_current + (app.mana_regenerate_rate * App.game_speed) / App.FPS, app.mana_cap);
            
        
            Wave currentWave = app.waves.get(app.waves.size() - 1);
            Tick.updatewave(((1.0f / App.FPS) * App.game_speed), currentWave);

            if (Tick.canspawn(currentWave)) {
                Mon_move monster = currentWave.spawnmonster(app, app.wizard_house_X, app.wizard_house_Y);
                if (monster != null) {
                    app.active_monsters.add(monster);
                }
            }
            if (currentWave.wavedone() && !app.wave_final) {
                app.curr_wave_index++;
                app.config_loader.next_wave();

            } 
        }

    }

    /**
     * Draws the towers that can fire at monsters, manages firing logic,fireball animation
     * @param app 
     */

    static void drawFireballsandTower(App app){
        for (Tower tower : app.towers) {
            tower.draw(app);

            Mon_move target_monster = null;
            float closestDistance = Float.MAX_VALUE;
        
            
            tower.fire_cooldown -= (1.0f / App.FPS) * App.game_speed;
        
         
            for (Mon_move monster : app.active_monsters) {
                if (!monster.is_dying) {
                    float distance = App.dist(tower.x * App.CELLSIZE + App.CELLSIZE / 2, tower.y * App.CELLSIZE + App.CELLSIZE / 2 + App.TOPBAR, monster.getmonX() * App.CELLSIZE, monster.getmonY() * App.CELLSIZE + App.TOPBAR);
                    if (distance <= tower.range && distance < closestDistance) {
                        target_monster = monster;
                        closestDistance = distance;
                }
            } 
                }
                
        
            if (!App.game_pause) {
                if (target_monster != null && tower.fire_cooldown <= 0) {
                        Tick.fire(target_monster, app, app.fireball_img,tower);
                    tower.fire_cooldown = 1.0f / tower.firing_speed;
                }
            } 
            

              
                Iterator<Fireball> fireballIterator = tower.fireballs.iterator();
                while (fireballIterator.hasNext()) {
                    Fireball fireball = fireballIterator.next();
                    if (!App.game_pause) {
                        fireball.move_fireball();
                    }
                    
                    if (fireball.reached_Destination()) {
                   
                        Mon_move hitMonster = fireball.target_monster;
                        hitMonster.monster_health(fireball.damage);
                        
                        if (hitMonster.is_dying) {
                            if (!hitMonster.get_mana_gain()) {
                                hitMonster.set_mana_gain(true);
                            }
                            PImage deathImg = hitMonster.get_monster_death();
                            if (deathImg != null) {
                                float size_adjusted = 1f * 32;
                                float offset_x = (App.CELLSIZE - size_adjusted) / 2;
                                float offset_y = (App.CELLSIZE - size_adjusted) / 2;
                                app.image(deathImg, hitMonster.getmonX() * App.CELLSIZE + offset_x, hitMonster.getmonY() * App.CELLSIZE + App.TOPBAR + offset_y, size_adjusted, size_adjusted);
                                app.active_monsters.remove(hitMonster);
                            }
                        } else {
                            fireballIterator.remove();
                        }     
                    } else {
                        fireball.draw(app);
                    }
                }
            
        }
    }

    /**
     * Draws the UI text elements, such as key on the game board and wave counters, on the screen.
     * @param app 
     */

    static void drawKeys(App app){
        app.fast_forward_button.draw(app);
        app.button_pause.draw(app);
        app.button_build_tower.draw(app);
        app.button_upgrade_range.draw(app);
        app.button_upgrade_speed.draw(app);
        app.button_upgrade_damage.draw(app);
        app.button_mana_pool_spell.draw(app);
        drawWaveTime(app);
        draw_Mana(app);
    }

     /**
     * Draws the wave counter, which displays the current wave 
     * @param app 
     */
    static void drawMonsterupd(App app) {
        Iterator<Mon_move> iterator = app.active_monsters.iterator();
        while (iterator.hasNext()) {
            Mon_move monster = iterator.next();
            if (!App.game_pause) {
                monster.move_monster();
                monster.draw(app);
                float[] pos = monster.getmonster_Pos();
                if (pos[0] >= 0 && pos[0] < App.BOARD_WIDTH && pos[1] >= 0 && pos[1] < App.BOARD_HEIGHT) {
                    if (app.layout[Math.round(pos[1])][Math.round(pos[0])] == 'W') {
                        if (app.mana_current < monster.getcurr_hp()) {
                            app.fill(0); 
                            app.textSize(30);
                            app.text("You Lost", 320, 320);
                            app.noLoop();
                        } else if (app.curr_wave_index >= app.waves.size() && app.mana_current > monster.getcurr_hp()) {
                            app.fill(0); 
                            app.textSize(30);
                            app.text("You Win", 320, 320);
                            app.noLoop();
                        
                        }else {
                            app.mana_current -= monster.getcurr_hp();
                            monster.monster_health(monster.getcurr_hp());
                            iterator.remove();
                        }
                    }
                }
            } else {
                monster.draw(app);
            }
        }
    }

    /**
     * Updates and draws the monsters on the game board, checks if they have reached the wizard's house
     * @param app 
     */
    static void drawWaveTime(App app) {
        app.fill(0); 
        app.textSize(20);  
        app.stroke(2);
    
        String waveText = "";

        if (app.curr_wave_index < app.waves.size()) {
            Wave currentWave = app.waves.get(app.curr_wave_index);

             if (!currentWave.wavedone()) {
                if (app.curr_wave_index + 1 <= app.waves.size()) {
                    float remainingTime = (currentWave.getDuration()+10) - currentWave.gettimespent();
                    waveText = "Wave " + (app.curr_wave_index + 2) + " starts: " + (int)Math.max(0, Math.ceil(remainingTime));
                }
                
            }
        } else {
        
            waveText = "All Waves Spawned";
        }

        app.text(waveText, 10, 20); 
    }


     /**
     * Draws the mana bar UI element
     * @param app
     */

    static void draw_Mana(App app) {
        int barWidth = 300;
        int barHeight = 20;
        int startX = App.WIDTH - barWidth - 10;
        int startY = (App.TOPBAR - barHeight) / 2;

      
        int fillWidth = (int) (barWidth * (app.mana_current / app.mana_cap));

        app.fill(7, 222, 250);  
        app.rect(startX, startY, fillWidth, barHeight);

        app.fill(255);  
        app.rect(startX + fillWidth, startY, barWidth - fillWidth, barHeight);

        app.stroke(0);  
        app.noFill();
        app.rect(startX, startY, barWidth, barHeight);

       
        app.fill(0); 
        app.textSize(18);
        app.textAlign(App.RIGHT, App.CENTER);
        app.text("MANA:", startX - 10, startY + barHeight / 2);

        
        String manaText = String.format("%.0f / %.0f", app.mana_current, app.mana_cap);
        app.textAlign(App.CENTER, App.CENTER);
        app.text(manaText, startX + barWidth / 2, startY + barHeight / 2);
    }


     /**
     * Draws the upgrade cost table, showing the player how much it will cost to upgrade each attribute of a tower
     */

    static void drawcostandUpgrades(PApplet p, Tower tower) {
        int tableWidth = 100; 
        int tableHeight = 140; 
        int startX = App.WIDTH - tableWidth - 10;
        int startY = App.HEIGHT - tableHeight - 10;
        int rowHeight = 10; 
        int padding = 10; 
        
        p.fill(255);
        p.rect(startX, startY, tableWidth, tableHeight);
        
        p.textSize(12);
        p.fill(0);
        p.text("Upgrade cost", startX + padding + 35, startY + rowHeight + 5);
        
        p.line(startX, startY + 2*padding + rowHeight, startX + tableWidth, startY + 2*padding + rowHeight);
        
        if (App.upgrade_desired_range) {
            p.text("Range:", startX + padding + 17, startY + 3*padding + 2*rowHeight); p.text(tower.getCostRange(), startX + tableWidth - padding, startY + 3*padding + 2*rowHeight);
            tower.cost_range = tower.getCostRange();  
        } else {
            tower.cost_range = 0; 
        } 

        if (App.upgrade_desired_speed) {
            p.text("Speed:", startX + padding + 17, startY + 4*padding + 3*rowHeight); p.text(tower.getCostSpeed(), startX + tableWidth - padding, startY + 4*padding + 3*rowHeight);
            tower.cost_speed = tower.getCostSpeed();
        } else {
            tower.cost_speed = 0;
        } 

        if (App.upgrade_desired_damage) {
            p.text("Damage:", startX + padding + 23, startY + 5*padding + 4*rowHeight); p.text(tower.getCostDamage(), startX + tableWidth - padding, startY + 5*padding + 4*rowHeight); 
            tower.cost_damage = tower.getCostDamage();
        } else {
            tower.cost_damage = 0;
        }
        
        p.line(startX, startY + 6*padding + 5*rowHeight, startX + tableWidth, startY + 6*padding + 5*rowHeight);
        
        int totalCost = tower.cost_range + tower.cost_speed + tower.cost_damage;
        p.text("Total:", startX + padding + 11, startY + 7*padding + 6*rowHeight - 3); p.text(totalCost, startX + tableWidth - padding, startY + 7*padding + 6*rowHeight - 3);
    }

     /**
     * Draws indications on the tower to represent the upgrades that have been applied, such as range or damage increases
     */

    static void drawupgradeSymbols(PApplet p, Tower tower) {
        int towerCenterX = tower.x * App.CELLSIZE + App.CELLSIZE / 2;
        int towerBaseX = tower.x * App.CELLSIZE;
        int towerCenterY = tower.y * App.CELLSIZE + App.TOPBAR + App.CELLSIZE / 2;
        int upgradeSymbolSize = 3; 
        int spacing = 4; 
        int offset_x = 5; 
        int offset_y = 3;
        

        int displayRangeLevels = tower.upgrade_level_range - 2 * tower.type_tower;
        int displaySpeedLevels = tower.upgrade_level_speed - tower.type_tower;
        int displayDamageLevels = tower.upgrade_level_damage - 2 * tower.type_tower;
    
        
        if (tower.upgrade_level_speed > 0) {
            p.noFill();
            p.stroke(61, 196, 245);
            p.strokeWeight(displaySpeedLevels); 
            p.rect(towerCenterX - App.CELLSIZE / 2, towerCenterY - App.CELLSIZE / 2, App.CELLSIZE, App.CELLSIZE);
            p.strokeWeight(1);
            p.stroke(0); 
        }
    
        
        p.fill(240, 70, 220); 
        for (int i = 0; i < Math.max(0, displayRangeLevels); i++) {
            p.textSize(10);
            p.text('O', towerBaseX + offset_x + i * (upgradeSymbolSize + spacing), tower.y * App.CELLSIZE + App.TOPBAR + upgradeSymbolSize - offset_y);
        }
    
        
        for (int i = 0; i < Math.max(0, displayDamageLevels); i++) {
            p.text('X', towerBaseX + offset_x + i * (upgradeSymbolSize + spacing), (tower.y + 1) * App.CELLSIZE + App.TOPBAR - upgradeSymbolSize - offset_y + 2);
        }
    }

     /**
     * Draw an abstract method
     */

    void draw(PApplet p);
       
}
