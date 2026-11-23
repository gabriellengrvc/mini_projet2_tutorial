package ch.epfl.cs107.play.tuto1.area.maps;

import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.tuto1.actor.SimpleGhost;
import ch.epfl.cs107.play.tuto1.area.SimpleArea;

public class Village extends SimpleArea {
    @Override
    protected void createArea() {
        registerActor(new SimpleGhost(new Vector(20, 10), "ghost.2"));
    }

    @Override
    public String getTitle() {
        return "zelda/Village";
    }
}