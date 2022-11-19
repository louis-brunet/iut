package fr.iutfbleau.zerotohero.actions;

import fr.iutfbleau.zerotohero.actors.PhysicsActor;

/**
 *  Set a PhysicsActor's horizontal speed.
 */
public class SetSpeedXAction extends SetSpeedAction {

    /**
     * Sets the horizontal  speed of the body of the actor performing this action.
     * @param delta the time in seconds since last frame
     * @return true
     */
    @Override
    public boolean act(float delta) {
        float ySpeed = ((PhysicsActor)this.getActor()).getBody()
                                                      .getSpeed()
                                                      .getY();
        this.setYValue(ySpeed);
        return super.act(delta);
    }
}
