package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;
import processing.data.JSONObject;

import java.util.*;
import java.io.*;

/**
 * The App class represents the main application window for the Wizard Tower Defense game.
 * It contains game initialization logic, rendering,
 * and input handling methods. The game involves managing towers to defend against waves of monsters.
 */

public class App extends PApplet {


     /**
     * Initialising variables
     */
    public char[][] layout;
    public Read_Config config_loader;
    protected String[][] tile_letter_config;
    protected Map<String, List<Integer[]>> assest_loc;
    protected Board grid;
    protected String layout_file;
    protected int levelNum = 1;
    protected Map<String, String> levelMap;
    Map<Integer, Map<String, String>> levels_map;
    public PImage tower_0;
    public PImage tower_1;
    public PImage tower_2;
    public PImage tower_3;
    public PImage worm ;
    public PImage fireball_img;
    public PImage beetle;
    public static PImage death_1;
    public static PImage death_2;
    public static PImage death_3;
    public static PImage death_4;
    public static PImage[] death_image = new PImage[5];
    public List<Wave> waves = new ArrayList<>();
    public List<Mon_move> active_monsters = new ArrayList<>();
    public int curr_wave_index = 0;
    public int wizard_house_X, wizard_house_Y;
    public boolean wave_final = false;
    public static final int CELLSIZE = 32;
    public static final int SIDEBAR = 120;
    public static final int TOPBAR = 40;
    public static final int BOARD_WIDTH = 20;
    public static final int BOARD_HEIGHT = 20;
    public static final int FPS = 60;
    public static final int WIDTH = CELLSIZE * BOARD_WIDTH + SIDEBAR;
    public static final int HEIGHT = CELLSIZE * BOARD_HEIGHT + TOPBAR;
    public float tower_initial_range;
    public float tower_initial_firing_speed; 
    public float tower_initial_damage;
    public static boolean upgrading_range = false;
    public static boolean upgrading_speed = false;
    public static boolean upgrading_damage = false;
    public static boolean upgrade_desired_range, upgrade_desired_speed, upgrade_desired_damage;
    public String config_path;
    public Random random = new Random();
    public char[][] path_directions = new char[BOARD_HEIGHT][BOARD_WIDTH];
    public Key fast_forward_button, button_pause, button_build_tower, button_upgrade_range, button_upgrade_speed, button_upgrade_damage, button_mana_pool_spell;
    public static boolean game_pause = false;
    public static float game_speed = 1.0f;
    public ArrayList<Tower> towers = new ArrayList<>();
    public boolean tower_place = false;
    public float tower_cost;
    public float mana_current;
    public float mana_cap;
    public float mana_regenerate_rate;
    public float mana_cost_initial_pool_spell;
    public float mana_cost_increase_pool_spell;
    public float mana_cap_mult_pool_spell;
    public float mana_gain_mult_pool_spell;



     /**
     * The constructor which initializes the config path.
     */
    public App() {
        this.config_path = "config.json";
    }


    /**
     * Setting window dimensions
     */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Setup used in combination with loading class helps load tiles
     */
    @Override
    public void setup() {
        surface.setLocation(0, 0);
        background(255);
        frameRate(FPS);

        loadImages();
        readJSON();
        Loading.loadLevel(this, levelNum);
        
        Loading.setupGame(this);

        
    }

    /**
     * Loads images for the static tiles
     */
    protected void loadImages() {
        String[] imageNames = {"grass","shrub","path0","path1","path2","path3","path4","wizard_house","path10","path8","path6","path7","path5","path9"//,"path5",
        };
        for (String name : imageNames) {
            ObjectMake.addToImageMap(name, loadImage(Objects.requireNonNull(
                    this.getClass().getResource(name + ".png"))
                    .getPath().replace("%20", " ")));
        }
    }

    /**
     * Reads config file and helps load he tiles onto the screen
     */
    protected void readJSON() {

        JSONObject config = loadJSONObject(new File(this.config_path));

        Map<String, String> levelMap = new HashMap<>();

        String layout = config.getString("layout");

        levelMap.put("layout", layout);

        Map<Integer, Map<String, String>> levels_map = new HashMap<>();

        levels_map.put(1, levelMap);
   

        this.levels_map = levels_map;
    }
 
    /**
     * Controls different keys on keyboard
     */

    @Override
    public void keyPressed() {

        if (key == 'r' || key == 'R') {
            PApplet.main("WizardTD.App");   
        }
    }




    /**
     * Controls keys on sidebar and mouse clicks for upgrading towers speed, range etc.
     */
    
    @Override
    public void mousePressed(MouseEvent e) {

        if (fast_forward_button.been_clicked(mouseX, mouseY)) {
            Tick.onfastforwardKey(this);

        } if (button_pause.been_clicked(mouseX, mouseY)) {
            game_pause = !game_pause;
            button_pause.key_update();

        } else if (button_build_tower.been_clicked(mouseX, mouseY)) {
            Tick.onbuildtower(this);
        

        } if (button_mana_pool_spell.been_clicked(mouseX, mouseY)) {
            button_mana_pool_spell.key_update();
            Tick.onmanaspell(this);
            button_mana_pool_spell.key_update();


        } if (button_upgrade_range.been_clicked(mouseX, mouseY)) {
            button_upgrade_range.key_update();
            upgrade_desired_range = !upgrade_desired_range;
        } else if (button_upgrade_speed.been_clicked(mouseX, mouseY)) {
            button_upgrade_speed.key_update();
            upgrade_desired_speed = !upgrade_desired_speed;
        } else if (button_upgrade_damage.been_clicked(mouseX, mouseY)) {
            button_upgrade_damage.key_update();
            upgrade_desired_damage = !upgrade_desired_damage;
        } Tick.onUpgradesKey(this);
    }


    /**
     * Activates key on the sidebar of window after the key is pressed
     */
    
    @Override
    public void mouseReleased(MouseEvent e) {
        int gridX = mouseX / CELLSIZE;
        int gridY = (mouseY - TOPBAR) / CELLSIZE;
        if (tower_place && mana_current >= tower_cost) {
            try {
                if (layout[gridY][gridX] == ' ' && !Tick.tower_Placed(gridX, gridY,this)) {
                    Tower tower = new Tower(gridX, gridY, tower_initial_range, tower_initial_firing_speed, tower_initial_damage, tower_0, tower_1, tower_2, tower_3);
                    
                    if (upgrading_range) {
                        float newMana = tower.upgraderange(mana_current);
                        if (newMana != -1) {
                            mana_current = newMana;
                        } else {
                            upgrading_range = false;
                        }
                    }
                    
                    if (upgrading_speed) {
                        float newMana = tower.upgradespeed(mana_current);
                        if (newMana != -1) {
                            mana_current = newMana;
                        } else {
                            upgrading_speed = false;
                        }
                    }
                    
                    if (upgrading_damage) {
                        float newMana = tower.upgradedamage(mana_current);
                        if (newMana != -1) {
                            mana_current = newMana;
                        } else {
                            upgrading_damage = false;
                        }
                    }
                    
                    towers.add(tower);
                    button_build_tower.key_update();
                    tower_place = false;
                    mana_current -= tower_cost;
                
                    upgrading_range = false;
                    upgrading_speed = false;
                    upgrading_damage = false;
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
            }
        }
    }

    /**
     * Draws everything on the board
     */

	@Override
    public void draw() {
        
        Draw.drawWaveTick(this);
 
        Draw.drawBackground(this);
        Draw.drawBack(this);
        Draw.drawTiles(this);

        Draw.drawFireballsandTower(this);

        Draw.drawKeys(this);
        
    
        Draw.drawMonsterupd(this);  

      
    }

    


    public static void main(String[] args) {
        PApplet.main("WizardTD.App");
    }
}