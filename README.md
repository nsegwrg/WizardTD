# scaffold

For this assignment, Java 8 was used with Processing library for graphics – which is incredibly useful for UI. Gradle was used to build the project, 
which makes handling dependencies and tasks a lot easier. The structure of the project is rooted in OOP (object-oriented programming). The 'App' class 
is the main application for the game – it extends 'PApplet', which is part of Processing and sets up the game window, handles drawing, keys and the mouse. 
The 'Object' class is the parent class of 'Tile'. And from 'Tile', come the following subclasses, including the special 'Tower Tile' that can be placed 
on the grid as deemed necessary.

There are three interfaces to keep code modular, organized and reusable: 'Draw', 'Tick', and 'Loading'. They split up the work – 'Draw' handles the drawing 
of different objects on the screen, 'Tick' handles the game updates and logic, and 'Loading' deals with getting the game set up as needed the initial and 
subsequent setting up.The 'Wave' class controls the waves of monsters and different properties of the wave timing, and the 'Monster' class for the enemies. 
There is a 'Move_Mon' class that handles the movement of monsters, respawn and timing. There's more to the setup: 'ObjectMaker' acts as a factory for creating
various game objects, 'EmptyTile' sets all tiles to be empty or assigns "empty" to all the tiles, and 'Board' creates a grid where that sets all tiles to empty
utilizing the 'EmptyTile'  method.

The extension created was – the "Emerald Tower". It's this special tower that unlocks when you’ve upgraded any tower's range, speed, and damage three times over 
in a game, that makes killing monsters easier.The graphic for this tower was drawn using Processing's PImage class, which allows for intricate sprite creation and 
manipulation. This makes the "Emerald Tower" not only a powerful asset in-game but also a visually distinctive one, setting it apart from other towers with its emerald gleam. 
Please note that the Mana spell button does not change to yellow upon hovering, yet it functions as intended. This minor visual glitch occurred just before the deadline 
and there wasn't an opportunity to address it. Additionally, the wave timer might exhibit some erratic behavior, but rest assured, it doesn't detract from the f
unctionality of the rest of the game, which performs as expected. The code's structure could have been more modular with comprehensive test cases to accompany it.

The inspiration for some of the designs and methodology was taken from the following  tower defense games, that utilize processing to an extent: 
https://github.com/akareen/Gremlins-Sprite-Game/blob/master/src/main/java/gremlins/App.java
https://github.com/bljessica/Java-PacMan/blob/master/src/game/roles/Monster.java



