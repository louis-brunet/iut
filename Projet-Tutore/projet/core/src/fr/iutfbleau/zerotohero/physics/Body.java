package fr.iutfbleau.zerotohero.physics;

import fr.iutfbleau.zerotohero.actors.PhysicsActor;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.utils.UnorderedPair;

import java.util.*;

/**
 * A Body represents an entity to use in the physics World. It is defined by its
 * position and a BoundingShape for its collision bounds.
 * ContactListeners can be added to a Body to trigger actions upon collision with another
 * Body.
 *
 * @author Louis Brunet
 */
public class Body {
    public enum Type {
        PLAYER,
        ENEMY,
        GROUND,
        JUMP_THROUGH,
        TILE_ENTITY,
        NO_CLIP_TILE_ENTITY,
        CONNECTION,
        DAMAGE,
        PROJECTILE
    }
    private final Type type;
    /**
     * The Body's current position.
     */
    protected Coordinates position;
    /**
     * The Body's last known position.
     */
    protected Coordinates previousPosition;
    /**
     * The Body's collision bounds.
     */
    protected BoundingShape bounds;
    /**
     * The speed of this Body.
     */
    private final Coordinates speed;
    /**
     * The speed to be added to this Body's speed (e.g. when on top of a moving
     * object).
     */
    private Coordinates addedSpeed;
    /**
     * The sum of this body's speed and added speed.
     */
    private Coordinates totalSpeed;
    /**
     * The list of ContactListeners to trigger upon collision with another Body.
     */
    private final List<ContactListener> contactListeners;

    private final boolean isMovable;
    private PhysicsActor actor;

//    /**
//     * The name associated to this Body
//     */
//    private String name;

    /**
     * Creates a Body with the specified bounds and position.
     *
     * @param type the Body's type
     * @param bounds the BoundingShape of the Body to create
     * @param position the position of the Body to create
     */
    public Body(Type type, boolean isMovable, BoundingShape bounds,
                Coordinates position) {
        this(type, isMovable, bounds, position, new Coordinates(0f,0f));
    }

    /**
     * Creates a Body with the specified bounds, position, and name.
     *
     * @param type the Body's type
     * @param bounds the BoundingShape of the Body to create
     * @param position the position of the Body to create
     * @param speed the speed of the Body to create
     */
    public Body(Type type, boolean isMovable, BoundingShape bounds,
                Coordinates position, Coordinates speed) {
        Objects.requireNonNull(type);
//        Objects.requireNonNull(type);
        Objects.requireNonNull(bounds);
        Objects.requireNonNull(position);
        Objects.requireNonNull(speed);

        this.type = type;
        this.isMovable = isMovable;
//        this.type = type;
        this.bounds = bounds;
        this.position = position;
        this.previousPosition = new Coordinates(position);
        this.speed = speed;
        this.addedSpeed = null;
        this.totalSpeed = new Coordinates();
        this.contactListeners = new ArrayList<ContactListener>();

        this.actor = null;
    }

    public void setActor(PhysicsActor actor) {
        this.actor = actor;
    }

    public PhysicsActor getActor() {
        return actor;
    }

    /**
     * Return the Body's current position.
     *
     * @return this Body's current position as Coordinates
     */
    public Coordinates getPosition() {
        return this.position;
    }

    /**
     * Return the Body's last known position.
     *
     * @return this Body's last known position as Coordinates
     */
    public Coordinates getPreviousPosition() {
        return this.previousPosition;
    }

    /**
     * Returns the Body's bounds as a BoundingShape.
     *
     * @return this Body's bounds as a BoundingShape
     */
    public BoundingShape getBounds() {
        return this.bounds;
    }

    /**
     * Sets this Body's current position to coordinates (x,y) in the plane.
     *
     * @param x the horizontal position to set
     * @param y the vertical position to set
     */
    public void setPosition(float x, float y) {
        this.previousPosition.setX(this.position.getX());
        this.previousPosition.setY(this.position.getY());

        this.position.setX(x);
        this.position.setY(y);
        this.bounds.setCenter(x, y);
    }

    public Type getType() {
        return this.type;
    }

    //    /**
//     * Returns the name of this Body.
//     *
//     * @return the name of this Body
//     */
//    public String getName() {
//        return  this.name;
//    }

//    /**
//     * Returns the type of this Body (STATIC or MOVING).
//     *
//     * @return the type of this Body (STATIC or MOVING)
//     */
//    public Type getType() {
//        return this.type;


    public boolean isMovable() {
        return this.isMovable;
    }

    /**
     * Adds a ContactListener to the list of ContactListeners to trigger when
     * this Body collides with another Body.
     *
     * @param listener the ContactListener to add
     */
    public void addContactListener(ContactListener listener) {
        Objects.requireNonNull(listener);
        this.contactListeners.add(listener);
    }

    public void removeContactListeners() {
        this.contactListeners.clear();
    }

    /**
     * Checks for collisions with all the given bodies. Triggers all the
     * ContactListeners added to this Body for each collision found.
     * If any of the given bodies are this Body, do not check for collision.
     * Updates the current collisions known by the World.
     *
     * @param bodies the list of bodies to check for collision
     * @param collisions the collisions currently known by the physics World
     */
    public void checkCollisions(List<Body> bodies, Set<UnorderedPair<Body>> collisions,
                                Set<Body> toSkip) {
                                //Map<Body, Set<Body>> collisions) {
        Objects.requireNonNull(bodies);
        Objects.requireNonNull(collisions);
        Objects.requireNonNull(toSkip);
//        if (toSkip.contains(this))
//            return;

        Body b;
        UnorderedPair<Body> pair, previousCollision;
        for (int i = 0; i < bodies.size(); i++) {
            b = bodies.get(i);
            if (this.equals(b) || toSkip.contains(this) || toSkip.contains(b))
                continue;

            pair = new UnorderedPair<Body>(this, b);

            previousCollision = null;
            for(UnorderedPair<Body> collision: collisions)
                if (collision.equals(pair))
                    previousCollision = collision;

            if (this.isCollidingWith(b)) {
                if (previousCollision == null) {
                    collisions.add(pair);

                    this.fireContactStartedEvent(b);
//                    if ( ! b.isMovable())
                        b.fireContactStartedEvent(this);
                }
                this.fireContactEvent(b);
                if ( ! b.isMovable())
                    b.fireContactEvent(this);

            } else if (previousCollision != null) {
                this.fireContactEndedEvent(b);
                b.fireContactEndedEvent(this);
                collisions.remove(previousCollision);
            }
        }
    }

    /**
     * Alerts all ContactListeners associated to this Body of the contact with
     * the given Body.
     *
     * @param b the colliding Body
     */
    private void fireContactEvent(Body b) {
        Objects.requireNonNull(b);
        for (ContactListener listener: this.contactListeners) {
            listener.onContact(b);
        }
    }

    private void fireContactStartedEvent(Body b) {
        Objects.requireNonNull(b);
        for (ContactListener listener: this.contactListeners) {
            listener.onContactStarted(b);
        }
    }

    /**
     * Alerts all ContactListeners associated to this Body of the end of contact
     * with the given Body.
     *
     * @param b the Body with which contact has ended
     */
    public void fireContactEndedEvent(Body b) {
//        if (this.getType().equals(Type.PLAYER))
//            System.out.println(type + ": contact ended with "+b.getType());

        Objects.requireNonNull(b);
        for (ContactListener listener: this.contactListeners) {
            listener.onContactEnded(b);
        }
    }

    /**
     * Checks for collision between this and another Body's BoundingShapes.
     *
     * @param b the Body to test for collision
     * @return true if the two BoundingShapes overlap
     */
    private boolean isCollidingWith(Body b) {
        Objects.requireNonNull(b);
        return this.bounds.overlaps(b.bounds);
    }

    /**
     * Returns this Body's current speed.
     *
     * @return this Body's current speed.
     */
    public Coordinates getSpeed() {
        return this.speed;
    }

    /**
     * Sets this Body's current speed to the given horizontal and vertical
     * speeds.
     *
     * @param x the horizontal speed to set
     * @param y the vertical speed to set
     */
    public void setSpeed(float x, float y) {
        this.speed.setX(x);
        this.speed.setY(y);
    }

    /**
     * Returns the speed to be added to this Body's current speed.
     *
     * @return the speed to add to this Body's current speed.
     */
    public Coordinates getAddedSpeed() {
        return this.addedSpeed;
    }

    /**
     * Sets the speed to be added to this Body's current speed.
     * For example, could be used when a solid body is on a moving platform: the
     * platform's speed should be added to the body's.
//     * @param x the horizontal speed to add each frame
//     * @param y the vertical speed to add each frame
     */
    public void setAddedSpeed(Coordinates speed ) {
//        if (Objects.isNull(speed)) {
//            this.addedSpeed.setX(0f);
//            this.addedSpeed.setY(0f);
//        } else {
//            this.addedSpeed.setX(speed.getX());
//            this.addedSpeed.setY(speed.getY());
//        }
        this.addedSpeed = speed;
    }

    public Coordinates getTotalSpeed() {
        float addedX = this.addedSpeed == null ? 0f : this.addedSpeed.getX();
        float addedY = this.addedSpeed == null ? 0f : this.addedSpeed.getY();
        this.totalSpeed.setX(this.speed.getX() + addedX);
        this.totalSpeed.setY(this.speed.getY() + addedY);
        return this.totalSpeed;
    }

    /**
     * Adds the given values to the horizontal and vertical components of this
     * Body's speed.
     *
     * @param x the horizontal speed to add
     * @param y the vertical speed to add
     */
    public void addSpeed(float x, float y) {
        this.speed.setX(this.speed.getX() + x);
        this.speed.setY(this.speed.getY() + y);
    }

    /**
     * Update this Body's position (including its BoundingShape's position)
     * based on its current speed.
     *
     * @param delta the amount of time passed since the last frame
     */
    public void updatePosition(float delta) {
        if (this.isMovable) {
            Coordinates totalSpeed = this.getTotalSpeed();
            this.setPosition(
                    this.position.getX() + totalSpeed.getX() * delta,
                    this.position.getY() + totalSpeed.getY() * delta);
        }
    }

    public float getXLeft() {
        return this.position.getX() - this.getBounds().getHalfWidth();
    }

    public float getXRight() {
        return this.position.getX() + this.getBounds().getHalfWidth();
    }

    public float getYTop() {
        return this.position.getY() + this.getBounds().getHalfHeight();
    }

    public float getYBottom() {
        return this.position.getY() - this.getBounds().getHalfHeight();
    }

    public float getPreviousXLeft() {
        return this.previousPosition.getX() - this.getBounds().getHalfWidth();
    }

    public float getPreviousXRight() {
        return this.previousPosition.getX() + this.getBounds().getHalfWidth();
    }

    public float getPreviousYTop() {
        return this.previousPosition.getY() + this.getBounds().getHalfHeight();
    }

    public float getPreviousYBottom() {
        return this.previousPosition.getY() - this.getBounds().getHalfHeight();
    }



    public static int compareYTop(Body b1, Body b2) {
        float yTop1 = b1.getYTop();
        float yTop2 = b2.getYTop();
        if (b1.equals(b2))
            return 0;
        else if (yTop1 > yTop2)
            return 1;
        else if (yTop1 < yTop2)
            return -1;
        else
            return Integer.compare(b1.hashCode(), b2.hashCode());
    }

    public static int compareYBottom(Body b1, Body b2) {
        float yBottom1 = b1.getYBottom();
        float yBottom2 = b2.getYBottom();
        if (b1.equals(b2))
            return 0;
        else if (yBottom1 > yBottom2)
            return 1;
        else if (yBottom1 < yBottom2)
            return -1;
        else
            return Integer.compare(b1.hashCode(), b2.hashCode());
    }

    public static int compareXLeft(Body b1, Body b2) {
        float xLeft1 = b1.getXLeft();
        float xLeft2 = b2.getXLeft();
        if (b1.equals(b2))
            return 0;
        else if (xLeft1 > xLeft2)
            return 1;
        else if (xLeft1 < xLeft2)
            return -1;
        else
            return Integer.compare(b1.hashCode(), b2.hashCode());
    }

    public static int compareXRight(Body b1, Body b2) {

        float xRight1 = b1.getXRight();
        float xRight2 = b2.getXRight();
        if (b1.equals(b2))
            return 0;
        else if (xRight1 > xRight2)
            return 1;
        else if (xRight1 < xRight2)
            return -1;
        else
            return Integer.compare(b1.hashCode(), b2.hashCode());
    }

}
