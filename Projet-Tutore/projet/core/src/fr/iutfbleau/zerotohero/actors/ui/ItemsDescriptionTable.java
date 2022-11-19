package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import fr.iutfbleau.zerotohero.ZeroToHero;

public class ItemsDescriptionTable extends Table {
//    private static final String TITLE = "Items";
    private static final String NO_ITEMS_TEXT = "";//"Picked up items will appear here.";
    private static final String SELECT_ITEM_PROMPT = "Click on an item to see its description";
    private static final Color ITEMS_PANEL_BACKGROUND = null;//new Color(1f,1f,1f, 0.8f);
    private static final float ICON_SIZE = 72f; // width, height
//    private static final float TITLE_PADDING = 16f;
    private static final float DESCRIPTION_PADDING = 16f;
    private static final float ICON_PADDING = 4f;
    private static final float NAME_PADDING = 16f;

    private final Label /*title,*/ description, name;
    private final ItemIconsRow itemIcons;

    public ItemsDescriptionTable(Label.LabelStyle headingStyle, Label.LabelStyle labelStyle) {
//        this.setDebug(true, true);

        Drawable background = ZeroToHero.getAssetManager()
                                        .createColorDrawable(ItemsDescriptionTable.ITEMS_PANEL_BACKGROUND);
        this.setBackground(background);

//        this.title = new Label(ItemsDescriptionTable.TITLE, headingStyle);
        this.description = new Label("{ Description }", labelStyle);
        this.description.setWrap(true);
        this.description.setAlignment(Align.center);
        this.name = new Label("{ Name }", labelStyle);
        this.name.setWrap(true);
        this.name.setAlignment(Align.center);
        this.itemIcons = new ItemIconsRow(ItemsDescriptionTable.ICON_SIZE,
                                          ItemsDescriptionTable.ICON_PADDING,
                                          this.description,
                                          this.name);


//        this.add(this.title)
//            .pad(ItemsDescriptionTable.TITLE_PADDING).top().left().expandX();

//        this.row();
        this.add(this.itemIcons).fill();

        this.row();
        this.add(this.name)
            .pad(ItemsDescriptionTable.NAME_PADDING)
            .fill()
            .expandX();

        this.row();
        this.add(this.description)
            .pad(ItemsDescriptionTable.DESCRIPTION_PADDING)
            .fill()
            .expandX();

//        this.update();
    }

    public void update() {
        int nbItems = ZeroToHero.getPlayer().getItems().size();
        if (nbItems == 0) {
            this.name.setText(ItemsDescriptionTable.NO_ITEMS_TEXT);
//            this.title.setVisible(false);
        } else {
            this.name.setText(ItemsDescriptionTable.SELECT_ITEM_PROMPT);
//            this.title.setVisible(true);
        }
        this.description.setText("");

        this.itemIcons.update();
    }
}
