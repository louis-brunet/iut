package fr.iutfbleau.zerotohero.entities;

import java.util.function.Supplier;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actions.ActionFactory;
import fr.iutfbleau.zerotohero.physics.AxisAlignedBoundingBox;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.physics.BoundingShape;
import fr.iutfbleau.zerotohero.physics.CircleBounds;
import fr.iutfbleau.zerotohero.registries.ItemProperties;
import fr.iutfbleau.zerotohero.registries.WeaponProperties;
import fr.iutfbleau.zerotohero.utils.Coordinates;

public class Pickupables {
    private static final String HEART_TEXTURE_PATH = "gui/heartIcon.png";
    private static final String GOLD_TEXTURE_PATH = "gui/moneyIcon.png";
    private static final String KEY_TEXTURE_PATH = "gui/keysIcon.png";
    private static final String BOMB_TEXTURE_PATH = "gui/bombsIcon.png";
    private static final float DEFAULT_SCALE = 0.5f;
//    private static final String SIMPLE_GUN_IDLE_TEXTURE_PATH = ;

    public static PickupableActor hearts(int amount, float x, float y) {
        return createPickupable("hearts", amount, x, y, HEART_TEXTURE_PATH, () -> ActionFactory.pickupHeart(amount));
    }

    public static PickupableActor gold(int amount, float x, float y) {
        return createPickupable("gold", amount, x, y, Pickupables.GOLD_TEXTURE_PATH, () -> ActionFactory.pickupGold(amount));
    }

    public static PickupableActor key(float x, float y) {
        return createPickupable("key", 1, x, y, Pickupables.KEY_TEXTURE_PATH, () -> ActionFactory.pickupKey());
    }

    public static PickupableActor bomb(float x, float y) {
        return createPickupable("bomb", 1, x, y, Pickupables.BOMB_TEXTURE_PATH, () -> ActionFactory.pickupBomb());
    }
    
    private static PickupableActor createPickupable(String name, int amount, float x, float y, String texturePath, Supplier<Action> action) {
        float radius = 48 / 2f;
        BoundingShape shape = new CircleBounds(new Coordinates(x, y),
                                               radius*DEFAULT_SCALE);
        Body body = new Body(Body.Type.NO_CLIP_TILE_ENTITY, true, shape,
                             new Coordinates(x, y), new Coordinates());

        String actorName = (amount > 1) ? name+"_"+amount : name;
        PickupableActor a = new PickupableActor(actorName, body, true, action);

        ZeroToHero.getAssetManager().addAsset(texturePath, Texture.class);
        a.addAnimation(PickupableActor.IDLE_ANIMATION_ID,
                       ZeroToHero.getAssetManager().getAsset(texturePath, Texture.class),
                       1, 1, 0.1f);
        a.setCurrentAnimation(PickupableActor.IDLE_ANIMATION_ID, false);
        a.setScale(DEFAULT_SCALE);

        return a;
    }

    public static PickupableActor weapon(WeaponProperties weapon, float x, float y) {
        BoundingShape shape = new AxisAlignedBoundingBox(new Coordinates(x,y),
                                                         weapon.getWidth() * weapon.getSize() / 2f,
                                                         weapon.getHeight() * weapon.getSize() / 2f);

        Body body =  new Body(Body.Type.NO_CLIP_TILE_ENTITY, true, shape, new Coordinates(x, y));

        PickupableActor actor = new PickupableActor(weapon.getName().toLowerCase(),
                                                    body,
                                                    false,
                                                    () -> ActionFactory.pickupWeapon(weapon));

        ZeroToHero.getAssetManager().addAsset(weapon.getImagePath(), Texture.class);
        actor.addAnimation(PickupableActor.IDLE_ANIMATION_ID,
                           ZeroToHero.getAssetManager().getAsset(weapon.getImagePath(), Texture.class),
                           (int) weapon.getImageSize().getY(),
                           (int) weapon.getImageSize().getX(), 0.1f);
        actor.setCurrentAnimation(PickupableActor.IDLE_ANIMATION_ID, false);

        actor.setScale(weapon.getSize());

        return actor;
    }

    public static PickupableActor item(ItemProperties item, float x, float y) {
        BoundingShape shape = new AxisAlignedBoundingBox(new Coordinates(x,y),
                                                         item.getWidth() / 2f,
                                                         item.getHeight() / 2f);

        Body body =  new Body(Body.Type.NO_CLIP_TILE_ENTITY, true, shape, new Coordinates(x, y));

        PickupableActor actor = new PickupableActor(item.getName().toLowerCase(),
                                                    body,
                                                    item.isAutoPickup(), // false,
                                                    () -> ActionFactory.pickupItem(item));

        ZeroToHero.getAssetManager().addAsset(item.getImagePath(), Texture.class);
        actor.addAnimation(PickupableActor.IDLE_ANIMATION_ID,
                           ZeroToHero.getAssetManager().getAsset(item.getImagePath(), Texture.class),
                           (int) 1, (int) 1, 0.1f);
        actor.setCurrentAnimation(PickupableActor.IDLE_ANIMATION_ID, false);

        actor.setScale(item.getSize());

        return actor;
    }
}
