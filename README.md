# AreaGame

`AreaGame` is a **grid-game** concept in which the game world is divided into playable areas.

## Core concepts

| Concept | Description |
|---|---|
| `AreaGame` | The main game concept. It can contain one or more `Area` objects. |
| `Area` | A 2D grid made of `Cell` objects, in which `Actor`s evolve and interact. |
| `AreaBehavior` | The grid associated with an `Area`; each area has its own characteristic grid. |
| `Cell` | A unit of the 2D grid composing an `Area`. |
| `Actor` | An entity or component that evolves and interacts within an `Area`. |

## Important methods

### Area lifecycle

- `begin()`  
  Performs all initializations required to start a playable area.

- `update()`  
  Evolves the actors by invoking each actor’s own `update()` method.

- `draw()`  
  Renders the actors by drawing them and playing their possible sound effects, using the actors’ `beep()` and `draw()` methods.

- `end()`  
  Performs all actions required when the game on the current area ends.

### Actor management

- `registerActor(Actor actor)`  
  Adds an actor to the area’s actor list.

- `unregisterActor(Actor actor)`  
  Removes an actor from the area’s actor list.

### Area management

- `addArea(Area area)`  
  Dynamically adds an area to the game.

- `setCurrentArea(String title, boolean forceBegin)`  
  Selects the current simulated area among all available areas.

  - `forceBegin = true`: restarts the area when entering it.
  - `forceBegin = false`: continues the area from where it was left during a previous visit.

## Area transitions

- `void suspend()`  
  Can be overridden to implement a strategy when leaving one play area for another, such as saving the state of the game and the current area.

- `boolean resume(Window window, FileSystem fileSystem)`  
  Returns `true` by default. It can be overridden to resume an area from an intermediate state in which it was previously left.

  - Return value `true`: resuming play on the area was possible.
  - Return value `false`: resuming play on the area was not possible.

## Camera and view

- `getCameraScaleFactor()`  
  Abstract method that can be overridden in subclasses to return the desired camera scale factor.

- `setViewCandidate(Actor actor)`  
  Centers the view on an actor. This enables the camera to follow a character within an area.

## Area lookup

- `Map<String, Area>`  
  A map used to find a play area based on its name, obtained through `getTitle()`.

## Entity hierarchy

- `Entity`  
  Basic implementation of an `Actor`.

- `Actor`  
  Subclass of `Entity`, with a speed, a reference frame, and movement-related behavior.
