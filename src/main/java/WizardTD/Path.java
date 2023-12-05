package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

public class Path extends Tile{
     /**
     * Generic tile path call has constructor and draw method
     * Also handles the path rotation
     */
    
    protected final PImage[] sprites;
    public int spriteNumber;

    public Path(int x, int y, String path, PImage[] sprites) {
        super(x, y, path);
        this.sprites = sprites;

    /**
     * Conditions that handle path rotation
     */

        if(path.equals("path0")) {
            spriteNumber = 0;
          } else if(path.equals("path1")) {  
                spriteNumber = 1; 
          } else if(path.equals("path2")) {
                spriteNumber = 2;
          } else if(path.equals("path3")) {
                spriteNumber = 3;
          } else if(path.equals("path4")) {
                spriteNumber = 4;
          }else if(path.equals("path5")) {  
                spriteNumber = 5; 
          } else if(path.equals("path6")) {
                spriteNumber = 6;
          } else if(path.equals("path7")) {
                spriteNumber = 7;
          } else if(path.equals("path8")) {
                spriteNumber = 8;
          } else if(path.equals("path9")) {
            spriteNumber = 9;
          } else if(path.equals("path10")) {
            spriteNumber = 10;
        } 
    }

    

    static int calculatePathSpriteNumber(App app, int row, int col) {
        int numNeighbors = 0;

        // Check left neighbor
        if (col > 0 && app.tile_letter_config[row][col - 1].equals("X")) {
            numNeighbors++;
        }

        // Check right neighbor
        if (col < app.tile_letter_config[0].length - 1 && app.tile_letter_config[row][col + 1].equals("X")) {
            numNeighbors++;
        }

        // Check top neighbor
        if (row > 0 && app.tile_letter_config[row - 1][col].equals("X")) {
            numNeighbors++;
        }

        // Check bottom neighbor
        if (row < app.tile_letter_config.length - 1 && app.tile_letter_config[row + 1][col].equals("X")) {
            numNeighbors++;
        }
        boolean hasLeftNeighbor = col > 0 && app.tile_letter_config[row][col - 1].equals("X");
        boolean hasRightNeighbor = col < app.tile_letter_config[0].length - 1 && app.tile_letter_config[row][col + 1].equals("X");
        boolean hasBelowNeighbor = row < app.tile_letter_config.length - 1 && app.tile_letter_config[row + 1][col].equals("X");
        boolean hasAboveNeighbor = row > 0 && app.tile_letter_config[row - 1][col].equals("X");
        if (numNeighbors == 1) {
            if(hasAboveNeighbor == true || hasBelowNeighbor == true){
                return 4;
            }
            else if(hasRightNeighbor == true || hasLeftNeighbor == true){
                return 0;
            }
        } else if (numNeighbors == 2) {
            if (hasRightNeighbor == true && hasLeftNeighbor == true) {
                return 0;
            }
            if (hasAboveNeighbor == true && hasBelowNeighbor == true) {
                return 4;
            } 
            else if (hasLeftNeighbor == true && hasBelowNeighbor == true) {
                return 1;
            } 
            else if (hasRightNeighbor == true && hasBelowNeighbor == true){
                return 8;
            }
            else if (hasRightNeighbor == true && hasAboveNeighbor == true){
                return 7;
            } 
            else if (hasLeftNeighbor == true && hasAboveNeighbor == true){
                return 6;
            }
        } else if (numNeighbors == 3) {
            if (hasRightNeighbor == true && hasLeftNeighbor == true && hasAboveNeighbor == true) {
                return 10;
            }
            else if (hasLeftNeighbor == true && hasBelowNeighbor == true && hasAboveNeighbor == true) {
                return 5;
            }
            else if (hasLeftNeighbor == true && hasBelowNeighbor == true && hasRightNeighbor == true) {
                return 2;
            }
            else if (hasAboveNeighbor == true && hasBelowNeighbor == true && hasRightNeighbor == true) {
                return 9;
            }
        } else if (numNeighbors == 4) {
            return 3;
        }

        return 0; // Default case
    }
    

    public void draw(PApplet app) {
        app.image(this.sprites[spriteNumber], super.getX(), super.getY());
    }
    
}
