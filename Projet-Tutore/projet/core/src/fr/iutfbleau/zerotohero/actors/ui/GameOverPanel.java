package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import fr.iutfbleau.zerotohero.ZeroToHero;

public class GameOverPanel extends Window {
    private static WindowStyle defaultStyle() {
        WindowStyle s = new WindowStyle();
        s.titleFont = ZeroToHero.getAssetManager().getDefaultFont();
        return s;
    }

    public GameOverPanel(ZeroToHero game, String seed, Label.LabelStyle titleStyle, Label.LabelStyle labelStyle, Drawable background) {
        super("", GameOverPanel.defaultStyle());

        this.setModal(true);
        this.setBackground(background);

        this.initHeading(titleStyle);
        this.initContent(labelStyle,seed, game);
    }

    private void initHeading(Label.LabelStyle titleStyle) {
        Label title = new Label("GAME OVER", titleStyle);
        this.add(title).spaceBottom(80f);
        this.row();
    }

    private void initContent(Label.LabelStyle labelStyle, String seed, ZeroToHero game) {
        Label label = new Label("You can retry with the same seed ("+seed.toUpperCase()+") or with a new one.", labelStyle);

        GameButton sameSeedBtn = new GameButton("Same seed", new StartGameListener(game,seed));
        GameButton newSeedBtn = new GameButton("New seed", new StartGameListener(game));
        GameButton quitBtn = new GameButton("Quit", new ExitToMenuListener(game));

        Table buttons = new Table();
        buttons.add(sameSeedBtn).expand().space(16);
        buttons.add(newSeedBtn).expand().space(16);
        buttons.add(quitBtn).expand().space(16);

        this.add(label).spaceBottom(32f);
        this.row();
        this.add(buttons);
    }
}
