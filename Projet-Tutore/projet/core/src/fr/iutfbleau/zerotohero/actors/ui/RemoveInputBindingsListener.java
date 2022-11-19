package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fr.iutfbleau.zerotohero.game.GameSettings;
import fr.iutfbleau.zerotohero.utils.InputAction;

public class RemoveInputBindingsListener extends ClickListener  {
    private final GameSettings settings;
    private final InputAction action;
    private final InputMappingView view;

    public RemoveInputBindingsListener(InputAction a, GameSettings s, InputMappingView view) {
        this.action = a;
        this.settings = s;
        this.view = view;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        this.settings.removeInputMappings(this.action);
        this.view.updateBindingsLabel(this.action);
        this.view.setButtonsVisible(true);
    }
}
