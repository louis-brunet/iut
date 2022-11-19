package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SystemCursorChangeListener extends ClickListener {
    private final Cursor.SystemCursor mouseEntered, clicked;

    public SystemCursorChangeListener(Cursor.SystemCursor mouseEntered) {
        this(mouseEntered, false);
    }
    public SystemCursorChangeListener(Cursor.SystemCursor mouseEntered, boolean revertOnClick) {
        this(mouseEntered, revertOnClick ? Cursor.SystemCursor.Arrow : null);
    }
    public SystemCursorChangeListener(Cursor.SystemCursor mouseEntered,
                                      Cursor.SystemCursor clicked) {
        this.mouseEntered = mouseEntered;
        this.clicked = clicked;
    }

    /**
     * Called any time the mouse cursor or a finger touch is moved over an actor. On
     * the desktop, this event occurs even when no
     * mouse buttons are pressed (pointer will be -1).
     *
     * @param event
     * @param x
     * @param y
     * @param pointer
     * @param fromActor May be null.
     * @see InputEvent
     */
    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        Gdx.graphics.setSystemCursor(this.mouseEntered);
    }

    /**
     * Called any time the mouse cursor or a finger touch is moved out of an actor. On
     * the desktop, this event occurs even when no
     * mouse buttons are pressed (pointer will be -1).
     *
     * @param event
     * @param x
     * @param y
     * @param pointer
     * @param toActor May be null.
     * @see InputEvent
     */
    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        // the mouse was hovered out
        if (pointer == -1)
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        // the mouse was clicked
        else if (this.clicked != null)
            Gdx.graphics.setSystemCursor(this.clicked);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        if (this.clicked != null)
            Gdx.graphics.setSystemCursor(this.clicked);
    }
}
