• AreaGame : ”grid game” concept.
• Area : area of an AreaGame. An AreaGame potentially contains several Area. 2d grid composed of cells (Cell) in which actors/components (Actor) evolve and interact.
• AreaBehavior : grid associated with an Area (each area of the game has a characteristic grid).

IMPORTANT METHODS TO REMEMBER :
• begin : carries out all the initializations necessary for starting a playable area ;
• update : makes the actors evolve (by invoking their own update method) ;
• draw : renders the actors by drawing them and playing their possible sound effects (actors’ beep and draw method) ;
• registerActor and unregisterActor : allow you to add/remove an actor from the list ;
• end : carries out all the actions to be carried out when the game on the current area ends.
• addArea : allows the dynamic addition of areas to the game ;
• setCurrentArea : allows you to choose the current (simulated) area among all the areas ; second parameter allows you to indicate whether when you move to this
  area you want to restart it (parameter forceBegin to true) or continue it where youleft it during a possible previous passage).


• void suspend() : once redefined, can implement any strategy when leaving one play area to another (such as saving information on the state of the game and the area) ;
• boolean resume(Window window, FileSystem fileSystem) : returns true by default but  once redefined, can allow the game to be resumed on an area from a possible 
  intermediate state where it would have been left. The return boolean indicates whether resuming play on the area was possible or not.
• getCameraScaleFactor() : an abstract method which can be redefined in subclasses to return the desired scale factor ;
• setViewCandidate() : center the view on an actor. This is what will allow the camera to follow a character in an area.

• Map<String, Area> : Map type allows you to find play area based on its name (getTitle)

• Entity- basic implementation of an Actor
• Actor- subclass of Entity entity with speed, reference frame, speed 
