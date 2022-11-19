package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class QuitGameListener extends ClickListener {
//    private ZeroToHero game;
//    public QuitGameListener(ZeroToHero g) { this.game = g; }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Gdx.app.exit();
    }
}
