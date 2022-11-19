package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import fr.iutfbleau.zerotohero.ZeroToHero;

public class SeedSelectionDialog extends Window {
    private static final float DIALOG_WIDTH = 400;
    private static final float DIALOG_HEIGHT = 300;
    private static final Drawable DEFAULT_BACKGROUND = ZeroToHero.getAssetManager().createColorDrawable(0,0,0,0.95f);
    private static final Drawable DEFAULT_DIALOG_BG = ZeroToHero.getAssetManager().createColorDrawable(0,0.4f,0.6f,1f);
    private static final Drawable TEXT_FIELD_BG = ZeroToHero.getAssetManager().createColorDrawable(1,1,1,0.8f);
    private static final Drawable TEXT_FIELD_CURSOR = ZeroToHero.getAssetManager().createColorDrawable(0,0,0,1f);
    private static final Drawable TEXT_FIELD_SELECTION = ZeroToHero.getAssetManager().createColorDrawable(0.3f,0.3f,1f,0.7f);
    private static final Label.LabelStyle DEFAULT_TITLE_STYLE = new Label.LabelStyle(ZeroToHero.getAssetManager().getBiggerFont(), null);
    private static final Label.LabelStyle DEFAULT_LABEL_STYLE = new Label.LabelStyle(ZeroToHero.getAssetManager().getDefaultFont(), null);

    private static WindowStyle defaultStyle() {
        WindowStyle s = new WindowStyle();
        s.titleFont = ZeroToHero.getAssetManager().getBiggerFont();
        return s;
    }

    public SeedSelectionDialog(ZeroToHero game) {
        this(game, SeedSelectionDialog.DEFAULT_BACKGROUND, SeedSelectionDialog.DEFAULT_DIALOG_BG, SeedSelectionDialog.DEFAULT_TITLE_STYLE, SeedSelectionDialog.DEFAULT_LABEL_STYLE);
    }

    public SeedSelectionDialog(ZeroToHero game, Label.LabelStyle titleStyle, Label.LabelStyle labelStyle) {
        this(game, SeedSelectionDialog.DEFAULT_BACKGROUND, SeedSelectionDialog.DEFAULT_DIALOG_BG,
             titleStyle, labelStyle);
    }

    public SeedSelectionDialog(ZeroToHero game, Drawable background, Drawable dialogBackgound,
                               Label.LabelStyle titleStyle, Label.LabelStyle labelStyle) {
        super ("", SeedSelectionDialog.defaultStyle());

        this.setBackground(background);
        this.setModal(true);

        Table dialog = new Table();
        dialog.setBackground(dialogBackgound);
        dialog.align(Align.top);
//        dialog.setDebug(true, true);

        this.initHeading(dialog, titleStyle);
        this.initContent(dialog, game, labelStyle);

        this.add(dialog).center().size(SeedSelectionDialog.DIALOG_WIDTH, SeedSelectionDialog.DIALOG_HEIGHT);

//        this.setDebug(true, true);
    }

    private void initHeading(Table dialog, Label.LabelStyle titleStyle) {
        GameButton backBtn = new GameButton("Back", new SetVisibilityListener(this, false));
        Label title = new Label("Seed", titleStyle);

        dialog.add(backBtn).left().expandX().pad(16f).maxWidth(80f);
        dialog.row();
        dialog.add(title).center().spaceBottom(16f);
        dialog.row();
    }

    private void initContent(Table dialog, ZeroToHero game, Label.LabelStyle labelStyle) {
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = ZeroToHero.getAssetManager().getDefaultFont();
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = SeedSelectionDialog.TEXT_FIELD_BG;
        textFieldStyle.cursor = SeedSelectionDialog.TEXT_FIELD_CURSOR;
        textFieldStyle.selection = SeedSelectionDialog.TEXT_FIELD_SELECTION;
        TextField textField = new TextField("", textFieldStyle);

        textField.setMessageText("Enter a seed");
        textField.setTextFieldFilter(((textField1, c) -> c <= 127 && Character.isLetterOrDigit(c)));
        textField.setBlinkTime(0.5f);
        textField.setMaxLength(12);
        textField.setAlignment(Align.center);

        GameButton setSeedBtn = new GameButton("Set seed", new StartGameListener(game, textField::getText));
        GameButton randomSeedBtn = new GameButton("Random seed", new StartGameListener(game));

        Table btns = new Table();
        btns.add(setSeedBtn).pad(8f);
        btns.add(randomSeedBtn).pad(8f);

        dialog.add(textField).size(200f,40f);
        dialog.row();
        dialog.add(btns).expandY().bottom().padBottom(16f);

    }
}
