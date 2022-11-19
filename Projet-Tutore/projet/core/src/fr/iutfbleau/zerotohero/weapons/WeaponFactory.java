package fr.iutfbleau.zerotohero.weapons;

import com.badlogic.gdx.graphics.Texture;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.registries.WeaponProperties;

public class WeaponFactory {
    public static WeaponActor create(WeaponProperties type, Projectile.Collision projectileCollision) {
        WeaponActor weapon;

        if (type.isChargeable())
        	weapon = new SimpleChargeableWeapon(type, projectileCollision);
        else
        	weapon = new SimpleWeapon(type, projectileCollision);
        
        ZeroToHero.getAssetManager().addAsset(type.getImagePath(), Texture.class);
        Texture texture = ZeroToHero.getAssetManager().getAsset(type.getImagePath(),
                                                                Texture.class);

        weapon.addAnimation(WeaponActor.IDLE_ANIMATION_ID, texture,
                            1, 1, 1f);
        weapon.setCurrentAnimation(WeaponActor.IDLE_ANIMATION_ID, false);
        weapon.setScale(type.getSize());

        return weapon;
    }
}
