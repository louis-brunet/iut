package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.screens.GameplayScreen;

import java.util.function.Supplier;

public class StartGameListener extends ClickListener {
    private final ZeroToHero game;
    private final Supplier<String> seed;

    public StartGameListener(ZeroToHero game) {
        this(game, (String) null);//"zqd1vdsevzls");
    }

    public StartGameListener(ZeroToHero game, String seed) {
        this(game, () -> seed);
    }

    public StartGameListener(ZeroToHero g, Supplier<String> seed) {
        this.game = g;
        this.seed = seed;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        this.game.setScreen(new GameplayScreen(this.game, this.seed.get()));
    }
}
