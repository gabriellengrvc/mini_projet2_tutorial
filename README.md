# AreaGame

`AreaGame` is a **grid-game** concept in which the game world is divided into playable areas.
| Concept | Description |
|---|---|
| `AreaGame` | The main game concept. It can contain one or more `Area` objects. |
| `Area` | A 2D grid made of `Cell` objects, in which `Actor`s evolve and interact. |
| `AreaBehavior` | The grid associated with an `Area`; each area has its own characteristic grid. |
| `Cell` | A unit of the 2D grid composing an `Area`. |
| `Actor` | Subclass of entity that evolves and interacts within an `Area` with a speed, a reference frame, and movement-related behavior. |
| `Entity` | Basic implementation of an `Actor`. |

## Important methods

- `begin()`  
  Performs all initializations required to start a playable area.

- `update()`  
  Evolves the actors by invoking each actor’s own `update()` method.

- `draw()`  
  Renders the actors by drawing them and playing their possible sound effects, using the actors’ `beep()` and `draw()` methods.

- `end()`  
  Performs all actions required when the game on the current area ends.

- `registerActor(Actor actor)`  
  Adds an actor to the area’s actor list.

- `unregisterActor(Actor actor)`  
  Removes an actor from the area’s actor list.

- `addArea(Area area)`  
  Dynamically adds an area to the game.

- `setCurrentArea(String title, boolean forceBegin)`  
  Selects the current simulated area among all available areas.

  - `forceBegin = true`: restarts the area when entering it.
  - `forceBegin = false`: continues the area from where it was left during a previous visit.

- `void suspend()`  
  Can be overridden to implement a strategy when leaving one play area for another, such as saving the state of the game and the current area.

- `boolean resume(Window window, FileSystem fileSystem)`  
  Returns `true` by default. It can be overridden to resume an area from an intermediate state in which it was previously left.

  - Return value `true`: resuming play on the area was possible.
  - Return value `false`: resuming play on the area was not possible.

- `getCameraScaleFactor()`  
  Abstract method that can be overridden in subclasses to return the desired camera scale factor.

- `setViewCandidate(Actor actor)`  
  Centers the view on an actor. This enables the camera to follow a character within an area.

## Area lookup

- `Map<String, Area>`  
  A map used to find a play area based on its name, obtained through `getTitle()`.

## Questions: 

- `What happens if we forget to invoke update of the superclass in the update by tuto1?` If you override update() in Tuto1 and leave out super.update(deltaTime), Java runs only that version. If that version is empty, the active area and its actors stop updating.
- `Why do you think it's better to declare variable game of the Play program as a Game rather than AreaGame?` Game is broader than AreaGame because every AreaGame is a Game, Play implements the methods in Game, and using that type lets it launch more kinds of games not just AreaGame based 
