package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import fr.iutfbleau.zerotohero.game.GameSettings;
import fr.iutfbleau.zerotohero.utils.InputAction;

import java.util.Arrays;

public class AddMappingInputProcessor extends InputAdapter {

    private final InputMappingView view;
    private final InputAction action;
    private final GameSettings settings;
    private final InputProcessor oldInputProcessor;

    public AddMappingInputProcessor(InputMappingView view, InputAction action, GameSettings settings, InputProcessor oldProcessor) {
        this.view     = view;
        this.action   = action;
        this.settings = settings;
        this.oldInputProcessor = oldProcessor;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Arrays.stream(this.settings.getInputKeys(this.action)).noneMatch(i -> i == keycode))
            this.settings.addInputKey(this.action, keycode);

        this.updateView();

        Gdx.input.setInputProcessor(this.oldInputProcessor);
//
//        System.out.println("Added mapping : " + this.action.name() + " -> " + Input.Keys.toString(keycode));
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (Arrays.stream(this.settings.getInputMouseButtons(this.action)).noneMatch(i -> i == button))
            this.settings.addInputMouseButton(this.action, button);

        this.updateView();

        Gdx.input.setInputProcessor(this.oldInputProcessor);

//        System.out.println("Added mapping : " + this.action.name() + " -> Mouse " + button);

        return true;
    }

    private void updateView() {
        this.view.updateBindingsLabel(this.action);
        this.view.showAddInputMapping(null);
    }
}
