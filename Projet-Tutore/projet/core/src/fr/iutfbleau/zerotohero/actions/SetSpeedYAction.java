package fr.iutfbleau.zerotohero.actions;

import fr.iutfbleau.zerotohero.actors.PhysicsActor;

/**
 *  Set a PhysicsActor's vertical speed.
 */
public class SetSpeedYAction extends SetSpeedAction {

    /**
     * Sets the vertical speed of the body of the actor performing this action.
     * @param delta the time in seconds since last frame
     * @return true
     */
    @Override
    public boolean act(float delta) {
        float xSpeed = ((PhysicsActor)this.getActor()).getBody()
                                                      .getSpeed()
                                                      .getX();
        this.setXValue(xSpeed);
        return super.act(delta);
    }
}