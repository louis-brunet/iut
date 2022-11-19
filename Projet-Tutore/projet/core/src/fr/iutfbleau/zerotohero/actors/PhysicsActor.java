package fr.iutfbleau.zerotohero.actors;

import java.util.Objects;

import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.room.Room;

/**
 * A PhysicsActor is a textured actor whose position is automatically updated to match
 * the position of its associated Body.
 *
 * @author Louis Brunet
 */
public class PhysicsActor extends AnimatedActor {

    /**
     * The Body whose position should be copied.
     */
    protected Body body;
    private Room room;

    public PhysicsActor() {
        this((Body)null);
    }

    public PhysicsActor(String name) {
        this(name, null);
    }

    /**
     * TODO javadoc
     */
    public PhysicsActor(Body b) {
        this("physics_actor", b);
    }

    /**
     * TODO javadoc
     */
    public PhysicsActor(String name, Body b) {
        super(name);

        this.setName(name);
        this.setBody(b);
    }

    public void setBody(Body body) {
        this.body = body;
        if (body != null) {
            this.updatePosition();
            this.setSize(body.getBounds().getHalfWidth() * 2f,
                         body.getBounds().getHalfHeight() * 2f);

            body.setActor(this);
        }
    }

    /**
     * Returns the Body whose position this PhysicsActor copies.
     * @return the Body whose position this PhysicsActor copies
     */
    public Body getBody() {
        return this.body;
    }

    /**
     * Updates the position of this PhysicsActor to match the position of this
     * PhysicsActor's Body.
     *
     * @param delta the time in seconds since last frame
     */
    @Override
    public void act(float delta) {
        super.act(delta);

        this.updatePosition();
    }

    /**
     * Updates the position of this PhysicsActor to match the position of this
     * PhysicsActor's Body.
     */
    private void updatePosition() {
        Objects.requireNonNull(this.body, "Need to set a body before updating position.");

        this.setX(this.body.getPosition().getX() - this.body.getBounds().getHalfWidth());
        this.setY(this.body.getPosition().getY() - this.body.getBounds().getHalfHeight());
    }
    
    public Room getRoom() {
		return this.room;
	}
    
    public void setRoom(Room room) {
		this.room = room;
	}
}
