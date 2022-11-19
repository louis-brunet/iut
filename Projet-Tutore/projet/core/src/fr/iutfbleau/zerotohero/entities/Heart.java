//package fr.iutfbleau.zerotohero.entities;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.scenes.scene2d.Action;
//import fr.iutfbleau.zerotohero.ZeroToHero;
//import fr.iutfbleau.zerotohero.actions.ActionFactory;
//import fr.iutfbleau.zerotohero.physics.Body;
//
//public class Heart extends PickupableActor {
//    private static final short ANIMATION_ID = 0;
//
//    private int amount;
//
//    public Heart(String textureFilePath, Body body, int amount) {
//        super("hearts_"+amount+"_actor", body, false);
//
//        this.amount = amount;
//
//        ZeroToHero.getAssetManager().addAsset(textureFilePath, Texture.class);
//        this.addAnimation(Heart.ANIMATION_ID,
//                          ZeroToHero.getAssetManager().getAsset(textureFilePath, Texture.class),
//                          1, 1, 0.1f);
//        this.setCurrentAnimation(Heart.ANIMATION_ID, false);
//    }
//
//    @Override
//    public Action getPickupAction() {
//        return ActionFactory.pickupHeart(this.amount);
//    }
//}
