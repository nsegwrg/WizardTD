package WizardTD;

import java.io.File;
import java.util.*;
import processing.core.PImage;

/**
* Handles the game setup
*/


public interface Loading {

    /**
     * Initiates the loading of a level in the game
     */

    static void loadLevel(App app, int levelNum) {
        setValuesFromMap(app, levelNum);
        readLevel(app);
        createassest_loc(app);
        loadObjects(app);
    }

    /**
     * Sets the values based on the specified level number
     */

    static void setValuesFromMap(App app, int levelNum) {
        app.layout_file = app.levels_map.get(levelNum).get("layout");
        
    }   
    
     /**
     * Reads the config file for static objects
     */
    static void readLevel(App app) {
        File fileName = new File(app.layout_file);
        try {
            try (Scanner scanner = new Scanner(fileName)) {
                String[][] configArray = new String[20][20];
                int x = 0;
                while (scanner.hasNextLine()) {
                    configArray[x] = scanner.nextLine().split("(?<=.)");
                    x += 1;
                }
                app.tile_letter_config = configArray;
            }
        } catch (Exception e) {
            System.out.println("Incorrect config file");
        }
    }

    /**
     * Assigns different tiles to different tiles
     */
    static void createassest_loc(App app) {
        app.assest_loc = makeDefaultassest_loc();
        String[] ls = {"Wiz_house", "Grass", "Shrub", "Path"};
        String[] codes = {"W", " ", "S", "X"};
        app.grid = new Board();
        for (int x = 0; x < app.tile_letter_config.length; x++) {
            for (int y = 0; y < app.tile_letter_config[x].length; y++) {
                for (int i = 0; i < codes.length; i++) {
                    if (app.tile_letter_config[x][y].equals(codes[i])) {
                        app.assest_loc.get(ls[i]).add(new Integer[] {x, y});
                        break;
                    }
                }
            }
        }

    }


    /**
     * Puts it ll in a map
     */
    static Map<String, List<Integer[]>> makeDefaultassest_loc() {
        String[] ls = {"Wiz_house", "Grass", "Shrub", "Path"};
        Map<String, List<Integer[]>> map = new HashMap<>();
        for (String s : ls) {
            map.put(s, new ArrayList<>());
        }
        return map;
    }
    
    /**
     * Loads static tiles according to certain conditions
     */
    static void loadObjects(App app) {
    
        for (Integer[] grassLocation : app.assest_loc.get("Grass")) {
            app.grid.setTile(grassLocation[1], grassLocation[0],
                    ObjectMake.makeGrass(grassLocation[1] * 32, (grassLocation[0] * 32) + 40));
        }

        for (Integer[] shrubLocation : app.assest_loc.get("Shrub")) {
            app.grid.setTile(shrubLocation[1], shrubLocation[0],
                    ObjectMake.makeShrub(shrubLocation[1] * 32, (shrubLocation[0] * 32) + 40));
        }

    
        for (Integer[] pathLocation : app.assest_loc.get("Path")) {
            int tileX = pathLocation[1] * 32;
            int tileY = (pathLocation[0] * 32) + 40;
        
            int spriteNumber = Path.calculatePathSpriteNumber(app, pathLocation[0], pathLocation[1]);
        
            Path path = ObjectMake.makePath(tileX, tileY);
            path.spriteNumber = spriteNumber;
        
            app.grid.setTile(pathLocation[1], pathLocation[0], path);
        }
        
        

        for (Integer[] wiz_houseLocation : app.assest_loc.get("Wiz_house")) {

            int tileX = wiz_houseLocation[1] * 32;
            int tileY = (wiz_houseLocation[0] * 32) + 40;
          
            PImage wizardTileImage = ObjectMake.getImage("wizard_house");
          
            Wiz_house wiz_house = new Wiz_house(
              tileX - 15, // Offset X by 8 pixels 
              tileY - 15, // Offset Y by 8 pixels
              "Wiz_house",
              wizardTileImage,
              48, 48); 
          
            wiz_house.draw(app);
          
            app.grid.setTile(wiz_houseLocation[1], wiz_houseLocation[0], wiz_house);
          
          }
    }


    /**
     * Setting other config components
     */
    static void setupGame(App app){

        app.config_loader = new Read_Config(app, app);
        app.config_loader.readvals(app.config_path);
        app.layout = app.config_loader.getmap();

        app.tower_initial_range = app.config_loader.get_start_towrange();
        app.tower_initial_firing_speed = app.config_loader.get_start_fireballspeed();
        app.tower_initial_damage = app.config_loader.get_start_towhealth();
        app.tower_cost = app.config_loader.get_cost_tower();
        app.mana_current = app.config_loader.get_mana();
        app.mana_cap = app.config_loader.get_mana_lim();
        app.mana_regenerate_rate = app.config_loader.get_mana_build();
        app.mana_cost_initial_pool_spell = app.config_loader.get_spell_cost();
        app.mana_cost_increase_pool_spell = app.config_loader.get_spell_cost_increase();
        app.mana_cap_mult_pool_spell = app.config_loader.get_spell_lim();
        app.mana_gain_mult_pool_spell = app.config_loader.get_spell_multiplier();
        app.waves = app.config_loader.get_waves();

        int buttonSize = 50;
        int buttonSpacing = 10;
        int startX = App.CELLSIZE * App.BOARD_WIDTH + (App.SIDEBAR - buttonSize) / 2 - 20;
        int startY = App.TOPBAR + buttonSpacing;

        app.fast_forward_button = new Key(startX, startY, buttonSize, "Fast Forward", "FF");
        app.button_pause = new Key(startX, startY + (buttonSize + buttonSpacing), buttonSize, "Pause", "P");
        app.button_build_tower = new Key(startX, startY + 2 * (buttonSize + buttonSpacing), buttonSize, "Build Tower", "T");
        app.button_upgrade_range = new Key(startX, startY + 3 * (buttonSize + buttonSpacing), buttonSize, "Upgrade Range", "U1");
        app.button_upgrade_speed = new Key(startX, startY + 4 * (buttonSize + buttonSpacing), buttonSize, "2x Speed", "U2");
        app.button_upgrade_damage = new Key(startX, startY + 5 * (buttonSize + buttonSpacing), buttonSize, "Upgrade Damage", "U3");
        app.button_mana_pool_spell = new Key(startX, startY + 6 * (buttonSize + buttonSpacing), buttonSize, "Mana pool cost: 100", "M");


        /**
        * Loads images
        */
        app.tower_0 = app.loadImage("src/main/resources/WizardTD/tower0.png");
        app.tower_1 = app.loadImage("src/main/resources/WizardTD/tower1.png");
        app.tower_2 = app.loadImage("src/main/resources/WizardTD/tower2.png");
        app.tower_3 = app.loadImage("src/main/resources/WizardTD/tower3.png");
        app.fireball_img = app.loadImage("src/main/resources/WizardTD/fireball.png");
        app.beetle = app.loadImage("src/main/resources/WizardTD/beetle.png");
        app.worm = app.loadImage("src/main/resources/WizardTD/worm.png");
        App.death_1 = app.loadImage("src/main/resources/WizardTD/gremlin1.png");
        App.death_1.resize(15, 15);
        App.death_2 = app.loadImage("src/main/resources/WizardTD/gremlin2.png");
        App.death_2.resize(15, 15);
        App.death_3 = app.loadImage("src/main/resources/WizardTD/gremlin3.png");
        App.death_3.resize(15, 15);
        App.death_4 = app.loadImage("src/main/resources/WizardTD/gremlin4.png");
        App.death_4.resize(15, 15);
        for (int i = 0; i < 5; i++) {
            App.death_image[i] = app.loadImage("src/main/resources/WizardTD/gremlin" + (i + 1) + ".png");
            App.death_image[i].resize(15, 15);
        }
        
        for (int y = 0; y < App.BOARD_HEIGHT; y++) {
            for (int x = 0; x < App.BOARD_WIDTH; x++) {
                if (app.layout[y][x] == 'W') {
                    app.wizard_house_X = x;
                    app.wizard_house_Y = y;
                    break;
                }
            }
        }
        find_wizardhouse(app,app.wizard_house_X, app.wizard_house_Y);

    }

    /**
     * Searches for wizard's house
     */

    static void find_wizardhouse(App app,int wizard_house_X, int wizard_house_Y) {
        if (!App.game_pause) {
            char[][] directions = new char[App.BOARD_HEIGHT][App.BOARD_WIDTH];
            for (int i = 0; i < App.BOARD_HEIGHT; i++) {
                Arrays.fill(directions[i], 'X');
            }
            directions[wizard_house_Y][wizard_house_X] = 'W';
        
            Queue<int[]> queue = new LinkedList<>();
            queue.add(new int[]{wizard_house_X, wizard_house_Y});
        
            int[][] moves = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
            char[] moveDirections = {'D', 'U', 'R', 'L'};
        
            while (!queue.isEmpty()) {
                int[] current = queue.poll();
                int x = current[0];
                int y = current[1];
        
                for (int i = 0; i < moves.length; i++) {
                    int newX = x + moves[i][0];
                    int newY = y + moves[i][1];
        
                    if (newX >= 0 && newX < App.BOARD_WIDTH && newY >= 0 && newY < App.BOARD_HEIGHT && app.layout[newY][newX] == 'X' && directions[newY][newX] == 'X') {
                        directions[newY][newX] = moveDirections[i];
                        queue.add(new int[]{newX, newY});
                    }
                }
            } app.path_directions = directions;
        }
    }

    /**
     * What are the tiles that are on the edge of the board that are also paths
     */

    static List<int[]> find_entry(App app) {
        List<int[]> boundaryTiles = new ArrayList<>();
    
        // Check top and bottom boundaries
        for (int x = 0; x < App.BOARD_WIDTH; x++) {
            if (app.layout[0][x] == 'X') {
                boundaryTiles.add(new int[]{x, 0});
            }
            if (app.layout[App.BOARD_HEIGHT - 1][x] == 'X') {
                boundaryTiles.add(new int[]{x, App.BOARD_HEIGHT - 1});
            }
        }
        
  
        for (int y = 0; y < App.BOARD_HEIGHT; y++) {
            if (app.layout[y][0] == 'X') {
                boundaryTiles.add(new int[]{0, y});
            }
            if (app.layout[y][App.BOARD_WIDTH - 1] == 'X') {
                boundaryTiles.add(new int[]{App.BOARD_WIDTH - 1, y});
            }
        }
    
        return boundaryTiles;
    }
}
