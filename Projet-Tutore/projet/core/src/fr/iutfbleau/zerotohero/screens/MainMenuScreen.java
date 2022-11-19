package fr.iutfbleau.zerotohero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.stages.MainMenuStage;

public class MainMenuScreen implements Screen {

    private ZeroToHero game;
    private Stage stage;

    public MainMenuScreen(ZeroToHero g) {
        this.game = g;

        this.stage = new MainMenuStage(g);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f,0.1f,0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.stage.draw();
        this.stage.act(delta);

//        if (Gdx.input.isTouched()) {
//            this.game.setScreen(new GameplayScreen(this.game, null));
//            this.dispose();
//        }

    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        this.stage.dispose();
    }
    
    public ZeroToHero getGame() {
		return game;
	}
}
