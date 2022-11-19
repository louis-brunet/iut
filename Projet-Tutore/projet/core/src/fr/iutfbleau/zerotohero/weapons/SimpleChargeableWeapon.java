package fr.iutfbleau.zerotohero.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actions.ActionFactory;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.physics.BoundingShape;
import fr.iutfbleau.zerotohero.physics.CircleBounds;
import fr.iutfbleau.zerotohero.registries.WeaponProperties;

public class SimpleChargeableWeapon extends ChargeableWeapon {
//    private static final Body.Type[] NON_SOLID = { Body.Type.JUMP_THROUGH,
//                                                   Body.Type.PLAYER ,
////                                                   Body.Type.CONNECTION,
//                                                   Body.Type.PROJECTILE,
//                                                   Body.Type.NO_CLIP_TILE_ENTITY };
    private static final float GRAVITY = 500f;
    private static final float FRICTION = 10f;

    private final Texture projectileTexture;
    private final Projectile.Collision projectileCollision;
    private float minDamage, maxDamage,
                  minSpeed, maxSpeed,
                  projectileSizeMin, projectileSizeMax;
    private String projectileType;

    public SimpleChargeableWeapon(WeaponProperties properties, Projectile.Collision projectileCollision) {
        super(properties, properties.getChargeDuration());

        this.minDamage = properties.getDamagesMin();
        this.maxDamage = properties.getDamagesMax();
        this.projectileSizeMin = properties.getProjectileSizeMin();
        this.projectileSizeMax = properties.getProjectileSizeMax();
        this.minSpeed = properties.getProjectileSpeedMin();
        this.maxSpeed = properties.getProjectileSpeedMax();
        this.projectileType = properties.getProjectileType();
        this.projectileCollision = projectileCollision;

        ZeroToHero.getAssetManager().addAsset(properties.getProjectileImagePath(),
                                              Texture.class);
        this.projectileTexture =
                ZeroToHero.getAssetManager().getAsset(properties.getProjectileImagePath(),
                                                      Texture.class);
    }

    @Override
    protected Projectile fire(float chargePercent) {
        if (chargePercent < 0f || chargePercent > 1f)
            throw new IllegalArgumentException("chargePercent is not between 0 and 1");

        int dmg = MathUtils.floor(Interpolation.linear.apply(minDamage, maxDamage, chargePercent));
        float projectileSpeed = Interpolation.linear.apply(minSpeed, maxSpeed, chargePercent);

        float radius = Interpolation.linear.apply(this.projectileSizeMin, this.projectileSizeMax, chargePercent);
        BoundingShape shape = new CircleBounds(new Coordinates(this.getPosition()),
                                               radius * ZeroToHero.getTileWidth());

        Body body = new Body(Body.Type.PROJECTILE, true, shape,
                             new Coordinates(this.getPosition()));

        Projectile p = Pools.get(Projectile.class).obtain();
        p.reset();
        p.init(this.projectileType, body, this.projectileCollision, this.getRoom(),
               () -> ActionFactory.damage(dmg));


        p.addAnimation(Projectile.MOVING_ANIMATION_ID,
                       this.projectileTexture, 1, 1, 1f);
        p.setCurrentAnimation(Projectile.MOVING_ANIMATION_ID, false);

        p.setScale(ZeroToHero.getTileWidth() * radius / (this.projectileTexture.getWidth() / 2f));

        Vector2 speed = this.getDirectionToCursor().cpy().nor().scl(projectileSpeed);
        p.addAction(ActionFactory.setSpeed(speed.x, speed.y));
        p.addAction(ActionFactory.friction(SimpleChargeableWeapon.FRICTION));
        p.addAction(ActionFactory.gravity(SimpleChargeableWeapon.GRAVITY, projectileSpeed));
//        p.addAction(ActionFactory.follow(ZeroToHero.getPlayer().getBody(), 150f));

        this.getRoom().addActor(p);
        this.getRoom().getWorld().addBody(body);


        return p;
    }
}
