package fr.iutfbleau.zerotohero.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actions.ActionFactory;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.physics.BoundingShape;
import fr.iutfbleau.zerotohero.physics.CircleBounds;
import fr.iutfbleau.zerotohero.registries.WeaponProperties;

public class SimpleWeapon extends WeaponActor {
    private final Texture projectileTexture;
    private float projectileSpeed, projectileSize, maxCooldown, cooldown;
	int damages;
    private String projectileType;
    private final Projectile.Collision projectileCollision;

    private final Pool<Projectile> projectiles;

    public SimpleWeapon(WeaponProperties properties, Projectile.Collision projectileCollision) {
        super(properties);

        this.projectileSpeed = properties.getProjectileSpeedMax();
        this.projectileSize = properties.getProjectileSizeMax();
        this.maxCooldown = properties.getCooldown();
        this.projectileType = properties.getProjectileType();
        this.damages = (int) properties.getDamagesMax();
        this.projectileCollision = projectileCollision;
        this.cooldown = 0f;

        ZeroToHero.getAssetManager().addAsset(properties.getProjectileImagePath(),
                                              Texture.class);
        this.projectileTexture =
                ZeroToHero.getAssetManager().getAsset(properties.getProjectileImagePath(),
                                                      Texture.class);

        this.projectiles = Pools.get(Projectile.class);
    }

    public Projectile fire() {

        float radius = this.projectileSize;// in nb of tiles

        BoundingShape shape = new CircleBounds(new Coordinates(this.getPosition()),
                                               radius * ZeroToHero.getTileWidth());

        Body body = new Body(Body.Type.PROJECTILE, true, shape,
                             new Coordinates(this.getPosition()));

        Projectile p = this.projectiles.obtain();
        p.reset();
		p.init(projectileType, body, this.projectileCollision, this.getRoom(),
               () -> ActionFactory.damage(this.damages));


        p.addAnimation(Projectile.MOVING_ANIMATION_ID,
                       this.projectileTexture, 1, 1, 1f);
        p.setCurrentAnimation(Projectile.MOVING_ANIMATION_ID, false);

        p.setScale(ZeroToHero.getTileWidth() * radius / (this.projectileTexture.getWidth() / 2f));

        Vector2 speed = this.getDirectionToCursor().cpy().nor().scl(this.projectileSpeed);
        p.addAction(ActionFactory.setSpeed(speed.x, speed.y));

        this.getRoom().addActor(p);
        this.getRoom().getWorld().addBody(body);

        return p;
    }

    @Override
    public Projectile update(float delta, float cursorX, float cursorY, boolean use) {
        this.aim(cursorX, cursorY);
        this.cooldown -= delta;
        if (use && this.cooldown <= 0f) {
            this.cooldown = this.maxCooldown;
            return this.fire();
        }

        return null;
    }
}
