package fr.iutfbleau.zerotohero.actions;

import fr.iutfbleau.zerotohero.actors.PhysicsActor;
import fr.iutfbleau.zerotohero.utils.Coordinates;

/**
 * Scale an actor's body's speed.
 */
public class ScaleSpeedAction extends SetSpeedAction {
    /**
     * Set the body's speed.
     *
     * @param delta the time in seconds since last frame
     * @return true
     */
    @Override
    public boolean act(float delta) {
        Coordinates speed = ((PhysicsActor) this.getActor()).getBody()
                                                            .getSpeed();
        this.setXValue(this.getXValue() * speed.getX());
        this.setYValue(this.getYValue() * speed.getY());
        return super.act(delta);
    }
}
