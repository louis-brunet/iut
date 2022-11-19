package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SetVisibilityListener extends ClickListener {

    private final boolean isVisible;
    private final Actor actor;

    public SetVisibilityListener(Actor component, boolean isVisible) {
        this.actor = component;
        this.isVisible = isVisible;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        this.actor.setVisible(this.isVisible);
    }
}
