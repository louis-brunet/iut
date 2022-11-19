package fr.iutfbleau.zerotohero.actions;

import fr.iutfbleau.zerotohero.actors.Stat;
import fr.iutfbleau.zerotohero.actors.StatContainer;
import fr.iutfbleau.zerotohero.utils.ViewDirection;
import fr.iutfbleau.zerotohero.utils.Coordinates;

/**
 * Accelerates an actor's body in a direction.
 */
public class AccelerateAction extends AddSpeedAction {

    /**
     * The direction to accelerate
     */
    private ViewDirection direction;
    /**
     * The StatContainer whose acceleration value will be used.
     */
    private StatContainer statContainer;

    public AccelerateAction() {
        this(null, null);
    }

    public AccelerateAction(ViewDirection direction, StatContainer statContainer){
        this.statContainer = statContainer;
        this.direction = direction;
    }

    public void setDirection(ViewDirection d) {
        this.direction = d;
    }

    public void setStatContainer(StatContainer statContainer) {
        this.statContainer = statContainer;
    }

    /**
     * Accelerate the body.
     *
     * @param delta seconds since last frame
     * @throws IllegalStateException if no StatContainer was set
     */
    @Override
    public boolean act(float delta) {
        this.setXValue(this.getHorizontalSpeedIncrease(delta));
        this.setYValue(0f);
        return super.act(delta);
    }

    /**
     * Returns the value by which to increase the body's speed.
     *
     * @param delta seconds since last frame
     * @throws IllegalStateException if no StatContainer was set
     */
    private float getHorizontalSpeedIncrease(float delta) {
        if ( this.statContainer == null )
            throw new IllegalStateException("No StatContainer set.");

        Coordinates acceleration = this.statContainer.getStatValue(Stat.ACCELERATION,
                                                                   Coordinates.class);
        if (acceleration == null)
            throw new IllegalStateException("Given StatContainer does not have an acceleration set.");


        switch (this.direction) {
            case LEFT:
                return - acceleration.getX() * delta;
            case RIGHT:
                return acceleration.getX() * delta;
            default:
                throw new IllegalStateException("AccelerationDirection not recognized : "+this.direction);
        }
    }
}
