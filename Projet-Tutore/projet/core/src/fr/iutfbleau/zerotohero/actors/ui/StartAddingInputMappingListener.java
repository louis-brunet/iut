package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fr.iutfbleau.zerotohero.game.GameSettings;
import fr.iutfbleau.zerotohero.utils.InputAction;

public class StartAddingInputMappingListener extends ClickListener {
    private final InputMappingView view;
    private final InputAction action;
    private final GameSettings settings;

    public StartAddingInputMappingListener(InputMappingView v, InputAction a, GameSettings s) {
        this.view = v;
        this.action = a;
        this.settings = s;
    }


    @Override
    public void clicked(InputEvent event, float x, float y) {

        InputProcessor oldInputProcessor = Gdx.input.getInputProcessor();
        this.view.showAddInputMapping(this.action);
        Gdx.input.setInputProcessor(new AddMappingInputProcessor(this.view, this.action, this.settings, oldInputProcessor));
//        this.view.addListener(new AddMappingInputListener(this.view, this.action, this.settings) );
    }
}
