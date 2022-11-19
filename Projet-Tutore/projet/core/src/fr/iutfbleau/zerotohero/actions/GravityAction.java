package fr.iutfbleau.zerotohero.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Applies gravity to an actor. The actor mut be a PhysicsActor. A GravityAction should
 * only be added once and affect the actor until it is removed.
 */
public class GravityAction extends Action {
    /**
     * The vertical speed to substract from the actor's speed per second.
     */
    private float gravity;
    /**
     * The actor's maximum vertical speed. Its speed will be clamped to this value.
     */
    private float maxSpeed;

    public GravityAction() {}

    /**
     * Initiliaze this action with the given gravity and maximum vetical speed.
     * @param gravity the gravity to apply (should be negative for downwards gravity)
     * @param maxSpeed the actor's maximum vertical speed
     */
    public void init(float gravity, float maxSpeed) {
        this.gravity = gravity;
        this.maxSpeed = maxSpeed;
    }


    /**
     * Applies gravity to the actor.
     *
     * @param delta seconds since last frame
     * @return false
     */
    @Override
    public boolean act(float delta) {
        Action a = Actions.sequence(ActionFactory.addSpeed(0, -this.gravity * delta),
                                    ActionFactory.clampSpeed(-Float.MAX_VALUE, Float.MAX_VALUE,
                                                             - this.maxSpeed, this.maxSpeed));
        this.actor.addAction(a);
        return false;
    }

    /**
     * Resets this action for reuse.
     */
    @Override
    public void reset() {
        super.reset();

        this.maxSpeed = 0f;
        this.gravity = 0f;
    }
}
