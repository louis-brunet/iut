package fr.iutfbleau.zerotohero.actions;

import com.badlogic.gdx.math.MathUtils;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.actors.PhysicsActor;

/**
 * Clamps an actor's body's speed. The actor must be a PhysicsActor.
 */
public class ClampSpeedAction extends SetSpeedAction {

    private float xMin, xMax, yMin, yMax;

    public ClampSpeedAction() {
        super(0f,0f);
    }

    public void setXMin(float xMin) {
        this.xMin = xMin;
    }

    public void setYMin(float yMin) {
        this.yMin = yMin;
    }

    public void setXMax(float xMax) {
        this.xMax = xMax;
    }

    public void setYMax(float yMax) {
        this.yMax = yMax;
    }

    /**
     * Clamps the actor's body's speed within the bounds described by xMin, xMax, yMin, yMax.
     *
     * @param delta the time in seconds since last frame
     * @return true if action is finished
     */
    @Override
    public boolean act(float delta) {
        Coordinates speed = ((PhysicsActor)this.getActor()).getBody().getSpeed();
        this.setXValue(MathUtils.clamp(speed.getX(), this.xMin, this.xMax));
        this.setYValue(MathUtils.clamp(speed.getY(), this.yMin, this.yMax));
        return super.act(delta);
    }
}
