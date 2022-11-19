package fr.iutfbleau.zerotohero.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.actors.PhysicsActor;

import java.util.Objects;

/**
 * Sets the speed of an actor's Body. This action's actor must be a PhysicsActor.
 */
public class SetSpeedAction extends Action {

    /**
     * The body whose speed to set.
     */
    private Body body;
    /**
     * The x and y speeds to set.
     */
    private float xValue, yValue;

    public SetSpeedAction() {
        this(0f, 0f);
    }

    public SetSpeedAction(float xValue, float yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
    }

    /**
     * Sets this action's actor, the body of which the speed will be set.
     *
     * @param actor the actor to which this action was added
     * @throws ClassCastException if actor is not a PhysicsActor
     */
    @Override
    public void setActor(Actor actor) {
        super.setActor(actor);
        if ( actor != null )
            this.body = ((PhysicsActor) actor).getBody();
    }

    /**
     * Set the body's speed.
     *
     * @param delta the time in seconds since last frame
     * @return true
     */
    @Override
    public boolean act(float delta) {
        Objects.requireNonNull(this.body);

        this.body.setSpeed(this.xValue, this.yValue);
        return true;
    }

    public void setXValue(float xValue) {
        this.xValue = xValue;
    }

    public void setYValue(float yValue) {
        this.yValue = yValue;
    }

    public float getXValue() {
        return xValue;
    }

    public float getYValue() {
        return yValue;
    }
}

