package fr.iutfbleau.zerotohero.physics;

import fr.iutfbleau.zerotohero.utils.UnorderedPair;

import java.util.*;

/**
 * A World represents the physics world in which all physics bodies live.
 * It is responsible for updating positions based on speed, and testing the
 * collisions of all moving bodies against all the bodies in the World.
 *
 * @author Louis Brunet
 */
public class World {
    /**
     * The list of all bodies added to this World.
     */
    private final List<Body> bodies;
    /**
     * The list of all moving bodies added to this World.
     */
    private final List<Body> movingBodies;

    private final Set<UnorderedPair<Body>> collisions;

    private final Set<Body> bodiesToRemove;
    private final Set<UnorderedPair<Body>> collisionsToRemove;

    /**
     * Creates an empty World with no bodies.
     */
    public World() {
        this.bodies = new ArrayList<Body>();
        this.movingBodies = new ArrayList<Body>();
        this.collisions = new HashSet<UnorderedPair<Body>>();
        this.collisionsToRemove = new HashSet<UnorderedPair<Body>>();
        this.bodiesToRemove = new HashSet<Body>();
    }

    /**
     * Add a body to the list of all bodies in this World. If the body should
     * be movable, also add it to the list of all moving bodies added to this
     * World.
     *
     * @param body the Body to add
     */
    public void addBody(Body body) {
        Objects.requireNonNull(body);
        if (this.bodies.contains(body))
            throw new IllegalArgumentException("body already added");

        this.bodies.add(body);
//        if (body.getType().equals(Body.Type.MOVING)) {
        if (body.isMovable())
            this.movingBodies.add(body);

//        this.collisions.put(body, new HashSet<Body>());
    }

    public void markForRemoval(Body b) {
        Objects.requireNonNull(b);
        this.bodiesToRemove.add(b);
//        if (b.getType() == Body.Type.PROJECTILE)
//            System.out.println("World.markforRemoval() : marked projectile for removal");
    }

    private void removeBodies() {
        this.bodiesToRemove.forEach(this::removeBody);
        this.bodiesToRemove.clear();
    }

    private void removeBody(Body body) {
        Objects.requireNonNull(body);
        this.movingBodies.remove(body);
        this.bodies.remove(body);

//        Set<UnorderedPair<Body>> toRemove = new HashSet<>();
        Body other;
        for(UnorderedPair<Body> collision : this.collisions) {
            if (collision.contains(body)) {
                other = collision.getOther(body);
//                System.out.println("World.removeBody() : collision found ["+body.getType()+", "+other.getType()+"]");
                other.fireContactEndedEvent(body);
                body.fireContactEndedEvent(other);
                this.collisionsToRemove.add(collision);
            }
        }
        this.collisions.removeIf(this.collisionsToRemove::contains);
        this.collisionsToRemove.clear();
    }

    /**
     * Updates the position of all moving bodies added to the World, then tests
     * all moving bodies for collision with any bodies added to this World.
     *
     * @param delta the time passed since last frame (s)
     */
    public void update(float delta) {
        this.updatePositions(delta);
        this.checkMovingBodyCollisions();

        this.removeBodies();
    }

    /**
     * Updates the position of all moving bodies added to the World.
     *
     * @param delta the time passed since last frame (s)
     */
    private void updatePositions(float delta) {
        for (int i = 0; i < this.movingBodies.size(); i++)
            this.movingBodies.get(i).updatePosition(delta);
    }

    /**
     * For each MovingBody added to the World, tests collisions with all bodies
     * added to the World. Triggers the ContactListeners of any MovingBody
     * colliding with another Body upon contact or on contact end.
     *
     * 
     */
    private void checkMovingBodyCollisions() {
        for (int i = 0; i < this.movingBodies.size(); i++) {
            Body b = this.movingBodies.get(i);
            b.checkCollisions(this.bodies, this.collisions, this.bodiesToRemove);
        }
    }

    /**
     * Returns the list of all bodies added to this World.
     *
     * @return the list of all bodies added to this World
     */
    public List<Body> getBodies()  {
        return this.bodies;
    }
}
