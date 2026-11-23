package ch.epfl.cs107.play.tuto1;

import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.tuto1.area.maps.Ferme;
import ch.epfl.cs107.play.tuto1.area.maps.Village;
import ch.epfl.cs107.play.window.Window;
import ch.epfl.cs107.play.tuto1.actor.SimpleGhost;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Keyboard;

public class Tuto1 extends AreaGame {
    private SimpleGhost player;

    private void createAreas() {
        addArea(new Ferme());
        addArea(new Village());
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (!super.begin(window, fileSystem)) {
            return false;
        }

        createAreas();
        player = new SimpleGhost(new Vector(18, 7), "ghost.1");
        Area startingArea = setCurrentArea("zelda/Ferme", true);
        startingArea.registerActor(player);
        startingArea.setViewCandidate(player);
        return true;
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getWindow().getKeyboard();

        if (keyboard.get(Keyboard.UP).isDown()) {
            player.moveUp();
        }
        if (keyboard.get(Keyboard.DOWN).isDown()) {
            player.moveDown();
        }
        if (keyboard.get(Keyboard.LEFT).isDown()) {
            player.moveLeft();
        }
        if (keyboard.get(Keyboard.RIGHT).isDown()) {
            player.moveRight();
        }

        super.update(deltaTime);

        if (player.isWeak()) {
            switchArea();
        }
    }

    private void switchArea() {
        Area currentArea = getCurrentArea();
        String nextTitle = currentArea.getTitle().equals("zelda/Ferme")
                ? "zelda/Village"
                : "zelda/Ferme";

        currentArea.unregisterActor(player);

        Area nextArea = setCurrentArea(nextTitle, false);
        nextArea.registerActor(player);
        nextArea.setViewCandidate(player);
        player.strengthen();
    }

    @Override
    public void end() {
    }

    @Override
    public String getTitle() {
        return "Tuto1";
    }
}