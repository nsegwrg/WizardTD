package WizardTD;

public class EmptyTile extends Tile{

     /**
     * Constructs an EmptyTile with specified coordinates and name
     */

    public EmptyTile(int x, int y, String name) {
        super(x, y, name);
        super.setEmpty(true);
    }
    
}
