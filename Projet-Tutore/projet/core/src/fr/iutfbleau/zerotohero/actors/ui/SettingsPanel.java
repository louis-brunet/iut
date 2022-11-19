package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.game.GameSettings;
import fr.iutfbleau.zerotohero.utils.InputAction;

public class SettingsPanel extends Window {
    private static final String TITLE = "Settings";
    private static final String CLOSE_BUTTON_TEXT = "Close";
    private static final String APPLY_BUTTON_TEXT = "Apply";
    private static final float HEADER_WIDTH = 800f;
    private static final float CONTENT_WIDTH = 1000f;
    private static final Drawable TITLE_BACKGROUND = ZeroToHero.getAssetManager().createColorDrawable(new Color(0x3e3a4bff));
    private static final Drawable WINDOW_BACKGROUND = ZeroToHero.getAssetManager().createColorDrawable(new Color(0x4b5d67ff));
    private static final InputAction[] CUSTOMIZABLE_INPUT_SETTINGS = { InputAction.INTERACT,
                                                                       InputAction.USE_WEAPON,
                                                                       InputAction.MOVE_LEFT,
                                                                       InputAction.MOVE_RIGHT,
                                                                       InputAction.JUMP,
                                                                       InputAction.PAUSE,
                                                                       InputAction.DROP_BOMB,
                                                                       InputAction.FALL_THROUGH };
    private static final ScrollPaneStyle SCROLL_PANE_STYLE =
            new ScrollPaneStyle(null,null,null,
                               null,// ZeroToHero.getAssetManager().createColorDrawable(12,1,Color.BLACK),
                                ZeroToHero.getAssetManager().createColorDrawable(10,1, new Color( 0, 0, 0, 0.2f)));

    private static WindowStyle defaultWindowStyle() {
        WindowStyle res = new WindowStyle();
        res.titleFont = ZeroToHero.getAssetManager().getDefaultFont();
        return res;
    }

    private final GameSettings settings;
    private InputMappingView inputMappingView;
    private AudioSettingsView audioSettingsView;
    private ScrollPane scrollPane;

    public SettingsPanel(GameSettings settings) {
        this(settings, SettingsPanel.WINDOW_BACKGROUND);
    }
    public SettingsPanel(GameSettings settings, Drawable background) {
        super(""/*SettingsPanel.TITLE*/, SettingsPanel.defaultWindowStyle());
        this.setModal(true);

        this.settings = settings;
        this.setBackground(background);
        this.align(Align.top);

        this.initHeader();
        this.initContent();
//        this.setDebug(true, true);
    }

    private void initHeader() {
        Label title = new Label(SettingsPanel.TITLE, new Label.LabelStyle(ZeroToHero.getAssetManager().getBiggerFont(), Color.WHITE));
        title.setAlignment(Align.center);
        Table header = new Table();
        header.setBackground(SettingsPanel.TITLE_BACKGROUND);
        Table headerContent = new Table();

        // Close btn
        ClickListener l = new SetVisibilityListener(this, false);
        Actor closeBtn = new GameButton(SettingsPanel.CLOSE_BUTTON_TEXT, l);

        // Apply btn
        l = new SaveSettingsListener(this.settings);
        Actor applyBtn = new GameButton(SettingsPanel.APPLY_BUTTON_TEXT, l);


        headerContent.add(closeBtn).pad(GameButton.DEFAULT_PADDING);
        headerContent.add(title).expand().fill().pad(8);
        headerContent.add(applyBtn).pad(GameButton.DEFAULT_PADDING);

        header.add(headerContent).width(SettingsPanel.HEADER_WIDTH);

        this.add(header)
            .expandX().fill();//.spaceBottom(4f);
        this.row();

//        header.setDebug(true, true);
    }

    private void initContent() {
        Table contentTable = new Table();

        // Audio settings
        this.audioSettingsView = new AudioSettingsView(this.settings,
                                                       SettingsPanel.TITLE_BACKGROUND);

        // Input mappings
        this.inputMappingView = new InputMappingView(SettingsPanel.CUSTOMIZABLE_INPUT_SETTINGS,
                                                     this.settings,
                                                     SettingsPanel.TITLE_BACKGROUND);

        contentTable.add(this.audioSettingsView).expand().fill();
        contentTable.row();
        contentTable.add(this.inputMappingView).expand().fill();


        this.scrollPane = new ScrollPane(contentTable, SettingsPanel.SCROLL_PANE_STYLE);
        this.scrollPane.setCancelTouchFocus(false);
        this.scrollPane.setFadeScrollBars(false);
        this.add(this.scrollPane).width(SettingsPanel.CONTENT_WIDTH);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (visible)  {
//            this.audioSettingsView.resetValues();
            this.inputMappingView.updateAllBindingsLabels();
            this.inputMappingView.setButtonsVisible(true);
            this.getStage().setScrollFocus(this.scrollPane);
        }else {
            this.settings.clearModifications();
            this.audioSettingsView.resetValues();
        }
    }
}
