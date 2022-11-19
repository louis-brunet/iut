package fr.iutfbleau.zerotohero.weapons;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import fr.iutfbleau.zerotohero.actors.PhysicsActor;
import fr.iutfbleau.zerotohero.actors.Character;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.physics.ContactListener;


public class ProjectileContactListener implements ContactListener {
    private final Projectile projectile;
    private final Body.Type[] bodiesToGoThrough;
    private final Pool<Projectile> pool;

    public ProjectileContactListener(Projectile p, Projectile.Collision collision) {
        this(p, collision.getNonSolidBodyTypes());
    }

    public ProjectileContactListener(Projectile p, Body.Type[] goThrough) {
        this.projectile = p;
        this.bodiesToGoThrough = goThrough;
        this.pool = Pools.get(Projectile.class);
    }

    @Override
    public void onContact(Body b) {
    }

    @Override
    public void onContactEnded(Body b) { }

    @Override
    public void onContactStarted(Body b) {
        for(Body.Type goThrough: this.bodiesToGoThrough)
            if (goThrough.equals(b.getType()))
                return;

        PhysicsActor actor = b.getActor();
        if (actor instanceof Character)
            actor.addAction(projectile.getEffect());

        this.pool.free(this.projectile);
    }
}
