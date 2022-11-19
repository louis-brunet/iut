package fr.iutfbleau.zerotohero.actions;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.iutfbleau.zerotohero.actors.Stat;
import fr.iutfbleau.zerotohero.actors.StatContainer;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.registries.ItemProperties;
import fr.iutfbleau.zerotohero.registries.WeaponProperties;
import fr.iutfbleau.zerotohero.utils.ViewDirection;

/**
 * A factory class for game actions to be added to actors.
 * Use libgdx's action pooling : objects are reused and created only when no objects of
 * the given class are available.
 */
public class ActionFactory {


    public static AccelerateAction accelerateLeft(StatContainer statContainer) {
        return ActionFactory.accelerate(ViewDirection.LEFT, statContainer);
    }

    public static AccelerateAction accelerateRight(StatContainer statContainer) {
        return ActionFactory.accelerate(ViewDirection.RIGHT, statContainer);
    }

    public static AddSpeedAction addSpeed(float x, float y) {
        AddSpeedAction a = Actions.action(AddSpeedAction.class);
        a.setXValue(x);
        a.setYValue(y);
        return a;
    }

    public static ClampSpeedAction clampSpeed(float xMin, float xMax,
                                              float yMin, float yMax) {
        ClampSpeedAction a = Actions.action(ClampSpeedAction.class);
        a.setXMin(xMin);
        a.setXMax(xMax);
        a.setYMin(yMin);
        a.setYMax(yMax);
        return a;
    }

    public static DamageAction damage(int amount) {
        DamageAction a = Actions.action(DamageAction.class);
        a.init(amount);
        return a;
    }

//    public static FollowAction follow(Body toFollow, float speed) {
//        FollowAction a = Actions.action(FollowAction.class);
//        a.init(toFollow, speed);
//        return a;
//    }

    public static FrictionAction friction(float amount) {
        FrictionAction a = Actions.action(FrictionAction.class);
        a.init(amount);
        return a;
    }

    // amount is positive for downward gravity
    public static GravityAction gravity(float amount, float maxSpeed) {
        GravityAction a = Actions.action(GravityAction.class);
        a.init(amount, maxSpeed);
        return a;
    }

//    public static JumpAction jump(float jumpSpeed) {
//        JumpAction a = Actions.action(JumpAction.class);
//        a.setYValue(jumpSpeed);
//        return a;
//    }

    public static AddIntegerStatAction pickupGold(int amount) {
        AddIntegerStatAction a = Actions.action(AddIntegerStatAction.class);
        a.setStat(Stat.GOLD);
        a.setValue(amount);
        return a;
    }

    public static PickupHeartAction pickupHeart(int amount) {
        PickupHeartAction a = Actions.action(PickupHeartAction.class);
        a.init(amount);
        return a;
    }

	public static AddIntegerStatAction pickupKey() {
        AddIntegerStatAction a = Actions.action(AddIntegerStatAction.class);
        a.setStat(Stat.KEYS);
        a.setValue(1);
        return a;
	}

	public static AddIntegerStatAction pickupBomb() {
        AddIntegerStatAction a = Actions.action(AddIntegerStatAction.class);
        a.setStat(Stat.BOMBS);
        a.setValue(1);
        return a;
	}

    public static PickupWeaponAction pickupWeapon(WeaponProperties weapon) {
        PickupWeaponAction a = Actions.action(PickupWeaponAction.class);
        a.init(weapon);
        return a;
    }

	public static PickupItemAction pickupItem(ItemProperties item) {
        PickupItemAction a = Actions.action(PickupItemAction.class);
        a.init(item);
        return a;
	}

    public static ScaleMaxSpeedAction scaleMaxSpeed(float duration, float factor) {
        ScaleMaxSpeedAction a = Actions.action(ScaleMaxSpeedAction.class);
        a.init(duration, factor);
        return a;
    }

    public static ScaleSpeedAction scaleSpeed(float scaleX, float scaleY) {
        ScaleSpeedAction a = Actions.action(ScaleSpeedAction.class);
        a.setXValue(scaleX);
        a.setYValue(scaleY);
        return a;
    }

    public static SetSpeedAction setSpeed(float x, float y) {
        SetSpeedAction a = Actions.action(SetSpeedAction.class);
        a.setXValue(x);
        a.setYValue(y);
        return a;
    }

    public static SetSpeedXAction setSpeedX(float xSpeed) {
        SetSpeedXAction a = Actions.action(SetSpeedXAction.class);
        a.setXValue(xSpeed);
        return a;
    }

    public static SetSpeedYAction setSpeedY(float ySpeed) {
        SetSpeedYAction a = Actions.action(SetSpeedYAction.class);
        a.setYValue(ySpeed);
        return a;
    }

    public static <V> SetStatAction<V> setStat(Stat stat, V value) {
        return new SetStatAction<V>(stat, value);
    }

    public static ShowInvincibilityAction showInvincibility(float duration, float minAlpha) {
        ShowInvincibilityAction a = Actions.action(ShowInvincibilityAction.class);
        a.init(duration, minAlpha);
        return a;
    }

    private static AccelerateAction accelerate(ViewDirection direction,
                                               StatContainer container) {
        AccelerateAction a = Actions.action(AccelerateAction.class);
        a.setDirection(direction);
        a.setStatContainer(container);
        return a;
    }
}
