package fr.iutfbleau.zerotohero.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.ui.*;

/**
 * This stage contains the main menu widgets.
 */
public class MainMenuStage extends Stage {
    /**
     * Menu title
     */
    private static final String TITLE = "Zero To Hero";
    /**
     * Start button text
     */
    private static final String START_TEXT = "Start";
    /**
     * Settings button text
     */
    private static final String SETTINGS_TEXT = "Settings";
    /**
     * Quit button text
     */
    private static final String QUIT_TEXT = "Quit";

    /**
     * The game instance
     */
    private final ZeroToHero game;
    /**
     * The SettingsPanel to display.
     */
    private final SettingsPanel settingsPanel;

    /**
     * Creates a MainMenuStage.
     * @param game the game instance
     */
    public MainMenuStage(ZeroToHero game) {
        super(new ScreenViewport());
        this.game = game;
        Gdx.input.setInputProcessor(this);



        this.settingsPanel = new SettingsPanel(ZeroToHero.getSettings());
        this.settingsPanel.setVisible(false);
        this.settingsPanel.setFillParent(true);

        SeedSelectionDialog seedSelectDialog = new SeedSelectionDialog(game);
        seedSelectDialog.setVisible(false);

        Table menu = new Table();
        this.initMenu(menu, seedSelectDialog);

        Stack root = new Stack();
        root.setFillParent(true);
        root.add(menu);
        root.add(seedSelectDialog);
        root.add(this.settingsPanel);


        this.addActor(root);
//        this.addActor(this.settingsPanel);
    }

    /**
     * Initialize menu title and buttons.
     */
    private void initMenu(Table table, SeedSelectionDialog dialog) {
//        table.setFillParent(true);

        BitmapFont bigFont = ZeroToHero.getAssetManager().getTitleFont();
        Label.LabelStyle bigStyle = new Label.LabelStyle(bigFont,Color.WHITE);

        Label title = new Label(MainMenuStage.TITLE, bigStyle);
        table.add(title);

        table.row();

        EventListener svListener = new SetVisibilityListener(dialog, true);
        GameButton startButton = new GameButton(MainMenuStage.START_TEXT, svListener);
        table.add(startButton).pad(GameButton.DEFAULT_PADDING);

        table.row();

        SetVisibilityListener
                sListener = new SetVisibilityListener(this.settingsPanel, true);
        GameButton settingsButton = new GameButton(MainMenuStage.SETTINGS_TEXT, sListener);
        table.add(settingsButton).pad(GameButton.DEFAULT_PADDING);

        table.row();

        GameButton quitButton = new GameButton(MainMenuStage.QUIT_TEXT, new QuitGameListener());
        table.add(quitButton).pad(GameButton.DEFAULT_PADDING);

//        this.addActor(table);
    }
}
