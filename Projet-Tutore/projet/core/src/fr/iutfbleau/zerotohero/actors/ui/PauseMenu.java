package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import fr.iutfbleau.zerotohero.ZeroToHero;

public class PauseMenu extends Table {
    private static final String TITLE = "PAUSED";
    private static final String RESUME_TEXT = "Resume";
    private static final String QUIT_TEXT = "Quit";
    private static final String SETTINGS_TEXT = "Settings";
    private static final Color BACKGROUND_COLOR = new Color(0f,0f,0f,0.85f);
    private static final float WEAPON_SPACING = 64f;
    private static final float CONTENT_WIDTH = 640f;

    private final WeaponDescriptionTable weaponTable;
    private final ItemsDescriptionTable itemsTable;

    public PauseMenu(Label.LabelStyle titleStyle, Label.LabelStyle headingStyle, Label.LabelStyle labelStyle,
                     SettingsPanel settingsPanel, ZeroToHero game, String seed) {
//        this.setDebug(true);
        Drawable bg = ZeroToHero.getAssetManager()
                                .createColorDrawable(PauseMenu.BACKGROUND_COLOR);
        this.setBackground(bg);

        Label menuTitle = new Label(PauseMenu.TITLE, titleStyle);
        GameButton resumeBtn = new GameButton(PauseMenu.RESUME_TEXT,
                                              new TogglePauseListener());
        GameButton settingsBtn = new GameButton(
                PauseMenu.SETTINGS_TEXT,
                new SetVisibilityListener(settingsPanel, true));
        GameButton quitBtn = new GameButton(PauseMenu.QUIT_TEXT,
                                            new ExitToMenuListener(game));
        this.weaponTable = new WeaponDescriptionTable(headingStyle, labelStyle);
        this.itemsTable = new ItemsDescriptionTable(headingStyle, labelStyle);

        Label seedLabel = new Label("Seed: "+seed.toUpperCase(), labelStyle);
        seedLabel.setAlignment(Align.center);

        this.add(menuTitle).colspan(3).pad(16f);
        this.row();
        this.add(resumeBtn).spaceBottom(16f);
        this.add(settingsBtn).spaceBottom(16f);
        this.add(quitBtn).spaceBottom(16f);

        this.row();
        this.add(this.weaponTable)
            .colspan(3).width(PauseMenu.CONTENT_WIDTH).space(PauseMenu.WEAPON_SPACING);

        this.row();
        this.add(this.itemsTable).colspan(3).width(PauseMenu.CONTENT_WIDTH);
        this.row();
        this.add(seedLabel).bottom().expandY().colspan(3).padBottom(16f);

//        this.setDebug(true, true);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (visible) {
            this.weaponTable.update();
            this.itemsTable.update();
        }
    }
}
