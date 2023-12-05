package WizardTD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;


/**
 * Loads and processes the configuration for the Wizard Tower Defense game. It handles the layout, tower
 * properties, and wave information from config file.
 */
public class Read_Config {

    /**
    * initialize variables
    */
    protected PApplet p;
    protected App app;
    protected float tower_initial_range;
    protected float tower_initial_firing_speed;
    protected float tower_initial_damage;
    protected float tower_cost;
    protected float mana_current;
    protected float mana_cap;
    protected float mana_regenerate_rate;
    protected float mana_cost_initial_pool_spell;
    protected float mana_cost_increase_pool_spell;
    protected float mana_cap_mult_pool_spell;
    protected float mana_gain_mult_pool_spell;
    protected float quantity_total;
    protected char[][] layout;
    protected int current_wave_JSON_index = 0;
    protected List<Wave> waves = new ArrayList<>();
    protected boolean wave_final = false;
    protected int BOARD_HEIGHT = App.BOARD_HEIGHT;
    protected int BOARD_WIDTH = App.BOARD_WIDTH;

    /**
    * Constructor
    */
    
    public Read_Config(PApplet p, App app) {
        this.p = p;
        this.app = app;

    }


        /**
     * Basic getter and setter values and gets the current wave number
     */

     public char[][] getmap() {
        return layout;
    }

    public float get_start_towrange() {
        return tower_initial_range;
    }

    public float get_start_fireballspeed() {
        return tower_initial_firing_speed;
    }

    public float get_start_towhealth() {
        return tower_initial_damage;
    }

    public float get_cost_tower() {
        return tower_cost;
    }

    public float get_mana() {
        return mana_current;
    }

    public float get_mana_lim() {
        return mana_cap;
    }

    public float get_mana_build() {
        return mana_regenerate_rate;
    }

    public float get_spell_cost() {
        return mana_cost_initial_pool_spell;
    }

    public float get_spell_cost_increase() {
        return mana_cost_increase_pool_spell;
    }

    public float get_spell_lim() {
        return mana_cap_mult_pool_spell;
    }

    public float get_spell_multiplier() {
        return mana_gain_mult_pool_spell;
    }

    public float get_quant() {
        return quantity_total;
    }

    public int get_index_wave() {
        return current_wave_JSON_index;
    }

    public List<Wave> get_waves() {
        return waves;
    }

    /**
    * Reads the layout file
    */
    public char[][] readFile(String path) {
        layout = new char[BOARD_HEIGHT][BOARD_WIDTH];
        try {
            String[] lines = p.loadStrings(path);
            for (int i = 0; i < lines.length; i++) {
                
                while (lines[i].length() < BOARD_WIDTH) {
                    lines[i] += " ";
                }
                
                layout[i] = lines[i].substring(0, BOARD_WIDTH).toCharArray();
            }
            
            for (int i = lines.length; i < BOARD_HEIGHT; i++) {
                Arrays.fill(layout[i], ' ');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layout;
    }
    
    /**
    * Assigns value to different variables
    */
    public void readvals(String config_path) {
        try {
            JSONObject config = p.loadJSONObject(config_path);
            
            String layout_filePath = config.getString("layout");
            layout = readFile(layout_filePath);

            tower_initial_range = config.getFloat("initial_tower_range");
            tower_initial_firing_speed = config.getFloat("initial_tower_firing_speed");
            tower_initial_damage = config.getFloat("initial_tower_damage");
            tower_cost = config.getFloat("tower_cost");
            mana_current = config.getFloat("initial_mana");
            mana_cap = config.getFloat("initial_mana_cap");
            mana_regenerate_rate = config.getFloat("initial_mana_gained_per_second");
            mana_cost_initial_pool_spell = config.getFloat("mana_pool_spell_initial_cost");
            mana_cost_increase_pool_spell = config.getFloat("mana_pool_spell_cost_increase_per_use");
            mana_cap_mult_pool_spell = config.getFloat("mana_pool_spell_cap_multiplier");
            mana_gain_mult_pool_spell = config.getFloat("mana_pool_spell_mana_gained_multiplier");

            if (get_index_wave() < config.getJSONArray("waves").size()) {
                JSONObject waveData = config.getJSONArray("waves").getJSONObject(get_index_wave());
                read_Wave_vals(waveData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /**
    * Assigns value to different wave and monster variables
    */

    protected void read_Wave_vals(JSONObject waveData) {
        try {
            Wave wave = new Wave(waveData.getFloat("duration"), waveData.getFloat("pre_wave_pause"), p);

            JSONArray monstersArray = waveData.getJSONArray("monsters");
            for (int j = 0; j < monstersArray.size(); j++) {
                JSONObject monsterData = monstersArray.getJSONObject(j);
                String type = monsterData.getString("type");
                float hp = monsterData.getFloat("hp");
                float speed = monsterData.getFloat("speed");
                float armour = monsterData.getFloat("armour");
                float manaGainedOnKill = monsterData.getFloat("mana_gained_on_kill");
                PImage sprite = p.loadImage("src/main/resources/WizardTD/" + type + ".png");
                Monsters monster_type = new Monsters(type, hp, speed, armour, manaGainedOnKill, sprite);
                if (j == 0) {
                    quantity_total = monsterData.getFloat("quantity");
                } else {
                    quantity_total = get_quant() + monsterData.getFloat("quantity");
                }
                Tick.addmon(monster_type, monsterData.getInt("quantity"), wave);
                wave.totalmonWave(get_quant());
            }
            get_waves().add(wave);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /**
     * Loads the next wave configuration from the JSON file and increments the current wave index.
     */
    public void next_wave() {
        try {
            JSONObject config = p.loadJSONObject(app.config_path);
            current_wave_JSON_index = get_index_wave() + 1;
            
            if (get_index_wave() < config.getJSONArray("waves").size()) {
                wave_final = false;
                JSONObject waveData = config.getJSONArray("waves").getJSONObject(get_index_wave());
                read_Wave_vals(waveData);
            } else {
                wave_final = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    


}
