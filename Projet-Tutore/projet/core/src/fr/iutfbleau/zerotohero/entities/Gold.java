//package fr.iutfbleau.zerotohero.entities;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.scenes.scene2d.Action;
//import fr.iutfbleau.zerotohero.ZeroToHero;
//import fr.iutfbleau.zerotohero.actions.ActionFactory;
//import fr.iutfbleau.zerotohero.physics.Body;
//
//public class Gold extends PickupableActor {
//    private static final short ANIMATION_ID = 0;
//
//    private int amount;
//
//    public Gold(String textureFilePath, Body body, int amount) {
//        super("gold_"+amount+"_actor", body, true);
//
//        this.amount = amount;
//
//        ZeroToHero.getAssetManager().addAsset(textureFilePath, Texture.class);
//        this.addAnimation(Gold.ANIMATION_ID,
//                          ZeroToHero.getAssetManager().getAsset(textureFilePath, Texture.class),
//                          1, 1, 0.1f);
//        this.setCurrentAnimation(Gold.ANIMATION_ID, false);
//    }
//
//    @Override
//    public Action getPickupAction() {
//        return ActionFactory.pickupGold(this.amount);
//    }
//}
