package fr.iutfbleau.zerotohero.actions;

import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.actors.PhysicsActor;

/**
 * Adds speed to an actor's body.
 */
public class AddSpeedAction extends SetSpeedAction {

    public AddSpeedAction() {
        super(0f, 0f);
    }

    /**
     * Adds speed to the body.
     *
     * @param delta the time in seconds since last frame
     * @return true if action is over
     */
    @Override
    public boolean act(float delta) {
        Coordinates speed = ((PhysicsActor) this.getActor()).getBody()
                                                            .getSpeed();
        this.setXValue(this.getXValue() + speed.getX());
        this.setYValue(this.getYValue() + speed.getY());
        return super.act(delta);
    }
}
