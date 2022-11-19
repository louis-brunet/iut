package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.screens.MainMenuScreen;

public class ExitToMenuListener extends ClickListener {
    private ZeroToHero game;

    public ExitToMenuListener(ZeroToHero g) { this.game = g; }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        this.game.setScreen(new MainMenuScreen(this.game));
    }
}
