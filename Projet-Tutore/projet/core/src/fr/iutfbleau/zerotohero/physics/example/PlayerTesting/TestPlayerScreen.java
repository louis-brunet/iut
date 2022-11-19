//package fr.iutfbleau.zerotohero.physics.example.PlayerTesting;
//
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//
//public class TestPlayerScreen implements Screen {
//
//    private Stage stage;
//
//    public TestPlayerScreen() {
//        super();
//        this.stage = new TestPlayerStage();
//    }
//
//    @Override
//    public void show() {}
//
//    @Override
//    public void render(float delta) {
//        Gdx.gl.glClearColor(0,0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        this.stage.draw();
//        this.stage.act(delta);
//    }
//
//    @Override
//    public void resize(int width, int height) {}
//
//    @Override
//    public void pause() {}
//
//    @Override
//    public void resume() {}
//
//    @Override
//    public void hide() {}
//
//    @Override
//    public void dispose() {
//        this.stage.dispose();
//    }
//}
