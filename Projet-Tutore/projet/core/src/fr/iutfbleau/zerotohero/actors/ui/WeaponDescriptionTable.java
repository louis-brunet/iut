package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.registries.WeaponProperties;
import fr.iutfbleau.zerotohero.weapons.WeaponActor;

public class WeaponDescriptionTable extends Table {
//    private static final String TITLE = "Weapon";
//    private static final float DESCRIPTION_PADDING = 8f;
    private static final float TEXT_PADDING = 8f;
//    private static final float NAME_PADDING = 16f;
    private static final float IMAGE_PADDING = 8f;
    private static final Color BACKGROUND = null;//new Color(0.38f, 0f, 0.5f, 0.8f);

    private final Cell<Image> imageCell;
    private final Label descriptionLabel, nameLabel;

    public WeaponDescriptionTable(Label.LabelStyle headingStyle,Label.LabelStyle labelStyle) {
        Drawable background = ZeroToHero.getAssetManager()
                                        .createColorDrawable(WeaponDescriptionTable.BACKGROUND);
        this.setBackground(background);

        this.nameLabel = new Label("{ Name }", headingStyle);
        this.nameLabel.setWrap(true);

        this.descriptionLabel = new Label("{ Description }", labelStyle);
        this.descriptionLabel.setWrap(true);

        Table textTable = new Table();
        textTable.add(this.nameLabel).expand().fill().spaceBottom(16f);//.pad(WeaponDescriptionTable.NAME_PADDING);
        textTable.row();
        textTable.add(this.descriptionLabel).expand().fill();//.pad(WeaponDescriptionTable.DESCRIPTION_PADDING);
//        textTable.setDebug(true);

//        this.add(this.nameLabel)
//            .colspan(2)
//            .align(Align.left)
//            .pad(WeaponDescriptionTable.NAME_PADDING);
//
//        this.row();
        this.imageCell = this.add ( (Image) null )
                             .pad(WeaponDescriptionTable.IMAGE_PADDING);
        this.add(textTable).expandX().fill().pad(WeaponDescriptionTable.TEXT_PADDING);
//        this.add(this.descriptionLabel)
//            .pad(WeaponDescriptionTable.DESCRIPTION_PADDING)
//            .width(new Value() {
//                          @Override
//                          public float get(Actor actor) {
//                              return getWidth() - imageCell.getPrefWidth() - 2 * WeaponDescriptionTable.DESCRIPTION_PADDING ;
//                          }
//                      });

        this.update();
//        this.setDebug(true);
    }

    public void update() {
        WeaponActor w = ZeroToHero.getPlayer().getWeapon();
        if (w == null) {
            this.setVisible(false);
            this.imageCell.setActor(null);
            this.descriptionLabel.setText("");
            this.nameLabel.setText("");
        } else {
            WeaponProperties weapon = w.getType();

            String imagePath = weapon.getImagePath();
            Image img = new Image(ZeroToHero.getAssetManager()
                                            .getAsset(imagePath, Texture.class));

            this.imageCell.setActor(img);
            this.descriptionLabel.setText(weapon.getDescription());
            this.nameLabel.setText(weapon.getName());
            this.setVisible(true);
        }
    }

}
