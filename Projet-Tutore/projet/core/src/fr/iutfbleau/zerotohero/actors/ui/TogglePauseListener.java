package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.screens.GameplayScreen;

public class TogglePauseListener extends ClickListener {
    @Override
    public void clicked(InputEvent event, float x, float y) {
        Screen s = ZeroToHero.getCurrentScreen();
        if (s instanceof GameplayScreen) {
            GameplayScreen gs = (GameplayScreen) s;
            gs.togglePause();
//            if (gs.isPaused())
        }
    }
}
