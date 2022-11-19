package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.registries.Item;

public class ItemIconsRow extends Table {
    private final float iconWidth, iconHeight, padding;
    private final Label descriptionLabel, nameLabel;

    public ItemIconsRow(float iconSize, float padding) {
        this(iconSize, padding, null);
    }
    public ItemIconsRow(float iconSize, float padding, Color background) {
        this(iconSize, padding, null, null, background);
    }
    public ItemIconsRow(float iconSize, float padding, Label descriptionLabel, Label nameLabel, Color background) {
        this(iconSize, iconSize, padding, descriptionLabel, nameLabel, background);
    }
    public ItemIconsRow(float iconSize, float padding, Label descriptionLabel, Label nameLabel) {
        this(iconSize, iconSize, padding, descriptionLabel, nameLabel, null);
    }
    public ItemIconsRow(float iconWidth, float iconHeight, float padding, Label descriptionLabel, Label nameLabel, Color background) {
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.padding          = padding;
        this.descriptionLabel = descriptionLabel;
        this.nameLabel = nameLabel;

        if (background != null) {
            Drawable bg = ZeroToHero.getAssetManager().createColorDrawable(background);
            this.setBackground(bg);
        }
    }

    public void update() {
        this.clearChildren();

        for(Item item: ZeroToHero.getPlayer().getItems()) {
            String path = item.getProperties().getImagePath();
            Texture t = ZeroToHero.getAssetManager().getAsset(path, Texture.class);
            Image img = new Image(t);

            if (this.descriptionLabel != null) {
                String desc = item.getProperties().getDescription();
                img.addListener(new SetLabelTextClickListener(desc, this.descriptionLabel));
                img.addListener(new SystemCursorChangeListener(Cursor.SystemCursor.Hand,
                                                               Cursor.SystemCursor.Hand));
            }
            if (this.nameLabel != null) {
                String name = item.getProperties().getName();
                img.addListener(new SetLabelTextClickListener(name, this.nameLabel));
            }

            this.add(img)
                .pad(this.padding)
                .size(this.iconWidth, this.iconHeight);
        }
    }
}
