package fr.iutfbleau.zerotohero.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

import fr.iutfbleau.zerotohero.actors.PhysicsActor;

/**
 * Applies friction to an actor. The actor mut be a PhysicsActor. A FrictionAction should
 * only be added once and affect the actor until it is removed.
 */
public class FrictionAction extends Action {
    /**
     * The friction to apply per second.
     */
    private float friction;

    public FrictionAction() {}

    /**
     * Initilize this action to apply the given amount of friction.
     * @param friction the friction to apply per second.
     */
    public void init(float friction) {
        this.friction = friction;
    }

    /**
     * Applies friction to the actor. Stops the actor's vertical speed the actor is moving
     * too slowly.
     *
     * @param delta seconds since last frame
     * @return false
     */
    @Override
    public boolean act(float delta) {
        Action a;
        float xSpeed = ((PhysicsActor) this.actor).getBody().getSpeed().getX();
        if (xSpeed == 0f) {
            return false;
        } else if (xSpeed <= - this.friction * delta ) {
            a = ActionFactory.addSpeed(this.friction * delta, 0f);
        } else if (xSpeed >= this.friction * delta) {
            a = ActionFactory.addSpeed(- this.friction * delta, 0f);
        } else {
            a = ActionFactory.setSpeedX(0f);
        }
        this.actor.addAction(a);

        return false;
    }

    /**
     * Resets this action for reuse.
     */
    @Override
    public void reset() {
        super.reset();
        this.friction = 0f;
    }
}
