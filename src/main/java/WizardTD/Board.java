package WizardTD;

public class Board {

 /**
 * Represents the game board for the game.
 */

    protected final Tile[][] tileGrid = makeTileGrid();

    /**
     * Creates a 20x20 grid of Tile objects representing the default state of the game board.
     * This method initializes each tile as an empty tile using the method.
     *gives two-dimensional array of Tile objects representing the game board.
     */

    protected Tile[][] makeTileGrid() {
        Tile[][] grid = new Tile[20][20];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y] = ObjectMake.makeEmptyTile(x, y);
            }
        }
        return grid;
    }

     /**
     * gives basic method to return the length of grid and the which tile we need
     */

    public int getYLength() {
        return this.tileGrid.length;
    }


    public int getXLength() {
        return this.tileGrid[0].length;
    }

    public Tile getTile(int x, int y) {
        return this.tileGrid[x][y];
    }

 
    public void setTile(int x, int y, Tile tile) {
        this.tileGrid[x][y] = tile;
    }
    
}
