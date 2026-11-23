package ch.epfl.cs107.play.areagame.area;

import java.util.List;
import ch.epfl.cs107.play.engine.actor.Draggable;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.io.ResourcePath;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Image;
import ch.epfl.cs107.play.window.Window;


/**
 * AreaBehavior is a basically a map made of Cells. Those cells are used for the game behavior
 * Note: implementation from Interactable.Listener not excpected from students
 */

public abstract class AreaBehavior implements Interactable.Listener, Interactor.Listener{

    /// The behavior is an Image of size height x width
    private final Image behaviorMap;
    private final int width, height;
    /// We will convert the image into an array of cells
    private final Cell[][] cells;

    /**
     * Default AreaBehavior Constructor
     * @param window (Window): graphic context, not null
     * @param name (String): name of the behavior image, not null
     */
    public AreaBehavior(Window window, String name){
        this(loadImage(window, name));
    }

    public AreaBehavior(Image behaviorMap){
        this(behaviorMap, behaviorMap.getWidth(), behaviorMap.getHeight());
    }

    public AreaBehavior(Image behaviorMap, int width, int height) {
        this.behaviorMap = behaviorMap;
        this.width = width;
        this.height = height;
        this.cells = new Cell[width][height];
    }

    private static Image loadImage(Window window, String name) {
        return window.getImage(ResourcePath.getBehavior(name), null, false);
    }

    public void dropInteractionOf(Draggable draggable, DiscreteCoordinates mouseCoordinates) {
    	if(mouseCoordinates.x >= 0 && mouseCoordinates.y >= 0 && mouseCoordinates.x < width && mouseCoordinates.y < height) {
    		cells[mouseCoordinates.x][mouseCoordinates.y].dropInteractionOf(draggable);
    	}
    }

    /// AreaBehavior implements Interactor.Listener

    @Override
    public void cellInteractionOf(Interactor interactor){
        for(DiscreteCoordinates dc : interactor.getCurrentCells()){
            if(dc.x < 0 || dc.y < 0 || dc.x >= width || dc.y >= height)
                continue;
            cells[dc.x][dc.y].cellInteractionOf(interactor);
        }
    }

    @Override
    public void viewInteractionOf(Interactor interactor){
        for(DiscreteCoordinates dc : interactor.getFieldOfViewCells()){
            if(dc.x < 0 || dc.y < 0 || dc.x >= width || dc.y >= height)
                continue;
            cells[dc.x][dc.y].viewInteractionOf(interactor);
        }
    }
    
    protected void setCell(int x,int y, Cell cell) {
    	cells[x][y] = cell;
    }
    
    protected Cell getCell(int x, int y) {
    	return cells[x][y];
    }
    protected Cell[][] getCells() { return cells;}
    protected int getRGB(int r, int c) {
    	return behaviorMap.getRGB(r, c);
    }
    
    protected int getHeight() {
    	return height;
    }
    
    protected int getWidth() {
    	return width;
    }
    

    /// AreaBehavior implements Interactable.Listener

    @Override
    public boolean canLeave(Interactable entity, List<DiscreteCoordinates> coordinates) {

        for(DiscreteCoordinates c : coordinates){
            if(c.x < 0 || c.y < 0 || c.x >= width || c.y >= height)
                return false;
            if(!cells[c.x][c.y].canLeave(entity))
                return false;
        }
        return true;
    }

    @Override
    public boolean canEnter(Interactable entity, List<DiscreteCoordinates> coordinates) {
        for(DiscreteCoordinates c : coordinates){
            if(c.x < 0 || c.y < 0 || c.x >= width || c.y >= height)
                return false;
            if(!cells[c.x][c.y].canEnter(entity))
                return false;
        }
        return true;
    }

    @Override
    public void leave(Interactable entity, List<DiscreteCoordinates> coordinates) {
        for(DiscreteCoordinates c : coordinates){
            cells[c.x][c.y].leave(entity);
        }

    }

    @Override
    public void enter(Interactable entity, List<DiscreteCoordinates> coordinates) {
        for(DiscreteCoordinates c : coordinates){
            cells[c.x][c.y].enter(entity);
        }
    }
}
