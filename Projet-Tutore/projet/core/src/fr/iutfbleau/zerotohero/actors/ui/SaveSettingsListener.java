package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fr.iutfbleau.zerotohero.game.GameSettings;

public class SaveSettingsListener extends ClickListener {
    private final GameSettings settings;

    public SaveSettingsListener(GameSettings gs) { this.settings = gs; }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        this.settings.savePreferences();
    }
}
