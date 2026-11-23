package ch.epfl.cs107.play.tuto1.actor;

import java.awt.Color;

import ch.epfl.cs107.play.engine.actor.Entity;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.engine.actor.TextGraphics;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class SimpleGhost extends Entity {
    private static final float FULL_ENERGY = 10f;
    private static final float MOVE_STEP = 0.05f;
    private final Sprite sprite;
    private final TextGraphics hpText;
    private float hp;

    public SimpleGhost(Vector position, String spriteName) {
        super(position);
        hp = FULL_ENERGY;
        sprite = new Sprite(spriteName, 1f, 1f, this);

        hpText = new TextGraphics(Integer.toString((int) hp), 0.4f, Color.BLUE);
        hpText.setParent(this);
        hpText.setAnchor(new Vector(-0.3f, 0.1f));
    }

    public boolean isWeak() {
        return hp <= 0f;
    }

    public void strengthen() {
        hp = FULL_ENERGY;
        hpText.setText(Integer.toString((int) hp));
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        hpText.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        hp = Math.max(0f, hp - deltaTime);
        hpText.setText(Integer.toString((int) hp));
    }

    public void moveUp() {
        setCurrentPosition(getPosition().add(0f, MOVE_STEP));
    }

    public void moveDown() {
        setCurrentPosition(getPosition().add(0f, -MOVE_STEP));
    }

    public void moveLeft() {
        setCurrentPosition(getPosition().add(-MOVE_STEP, 0f));
    }

    public void moveRight() {
        setCurrentPosition(getPosition().add(MOVE_STEP, 0f));
    }
}