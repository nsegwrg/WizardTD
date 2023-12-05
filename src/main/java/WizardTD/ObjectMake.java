package WizardTD;

import processing.core.PImage;

import java.util.HashMap;
import java.util.Map;

public class ObjectMake {

    /**
     * maker methods for static objects
     */

    public static Map<String, PImage> spritesMap = new HashMap<>();

    public static void addToImageMap(String name, PImage image) {
        spritesMap.put(name, image);
    }

    /**
     * Defines different maker methods for static components and a cnstructor
     */
    public static PImage getImage(String imageName) {
        return spritesMap.get(imageName);
    }

    public static EmptyTile makeEmptyTile(int y, int x){
        return new EmptyTile(x, y, "empty");
    }
    
    public static Grass makeGrass(int x, int y) {
        return new Grass(x, y, "grass", spritesMap.get("grass"));
    }

    public static Shrub makeShrub(int x, int y) {
        return new Shrub(x, y, "shrub", spritesMap.get("shrub"));
    }

    public static Path makePath(int x, int y) {
        return new Path(x, y, "path", new PImage[]{
            spritesMap.get("path0"),spritesMap.get("path1"),
            spritesMap.get("path2"),spritesMap.get("path3"),
            spritesMap.get("path4"),spritesMap.get("path5"),
            spritesMap.get("path6"),spritesMap.get("path7"),
            spritesMap.get("path8"),spritesMap.get("path9"),
            spritesMap.get("path10")
});
    }

    public static Wiz_house makeWiz_house(int x, int y) {
        return new Wiz_house(x, y, "wizard_house", spritesMap.get("wizard_house"),48,48);
    }

}
