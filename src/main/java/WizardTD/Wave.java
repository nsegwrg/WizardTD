package WizardTD;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
* Basic wave stuff
*/

public class Wave {
    public float duration; 
    public float pre_wave_pause; 
    public float pre_wave_pause_time; 
    public float time_elapsed;  
    public float time_since_last_spawn; 
    public float count_total_monster;
    public List<Wave_tick> monsters_to_spawn = new ArrayList<>();
    protected Random random = new Random();

    /**
     * Constructs a Wave with a specific duration and pre-wave pause time.
     */

    public Wave(float duration, float pre_wave_pause, PApplet p) {
        this.duration = duration / App.game_speed;
        this.pre_wave_pause = pre_wave_pause / App.game_speed;
        this.pre_wave_pause_time = 0;
        this.time_elapsed = 0;
        this.time_since_last_spawn = 0;
    }

     /**
     * Spawns a monster at the beginning of the wave if there are monsters left to spawn
     */
    public Mon_move spawnmonster(App app, int wizard_house_X, int wizard_house_Y) {
        Monsters monster_type = getnextmonsterSpawn();
        if (monster_type != null) {
            List<int[]> boundaryTiles = Loading.find_entry(app);
            int[] spawnPosition = boundaryTiles.get(random.nextInt(boundaryTiles.size()));
            int x = spawnPosition[0];
            int y = spawnPosition[1];

            return new Mon_move(Math.round(x), Math.round(y), monster_type, app, wizard_house_X, wizard_house_Y);
        }
        return null;
    }

    /**
     * Retrieves the next monster_type to spawn from the list of monsters to spawn
     */

    protected Monsters getnextmonsterSpawn() {
        if (monsters_to_spawn.isEmpty()) {
            return null;
        }

        Wave_tick mtq = monsters_to_spawn.get(0);
        Monsters monster_type = mtq.getmonster_type();

        mtq.decrementquantity();
        if (mtq.getquantity() <= 0) {
            monsters_to_spawn.remove(0);
        }

        return monster_type;
    }

    /**
     * Checks if wave is over
     */
    public boolean wavedone() {
        if (monsters_to_spawn.isEmpty() && time_elapsed > duration) {
            time_elapsed = 0;
            return true;
        }
        return false;
    }

    /**
     * number monsters in wave
     */
    public void totalmonWave(float quantity_total) {
        this.count_total_monster = quantity_total;
    }

    /**
     * getters
     */

    public float getDuration() {
        return this.duration;
    }

    public float gettimespent() {
        return this.time_elapsed;
    }

    
}



