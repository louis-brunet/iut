package fr.iutfbleau.zerotohero.actors;

import com.badlogic.gdx.math.MathUtils;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.physics.BoundingShape;
import fr.iutfbleau.zerotohero.physics.CollisionDirection;

import java.util.*;

/**
 * A SolidActor is a particular PhysicsActor whose Body should be blocked in certain
 * directions by certain Body types.
 *
 * There are two categories of referenced Body types :
 * "solid" bodies should block this actor when making contact from any direction,
 * "jump-through" bodies should only block this actor when making contact from below.
 *
 * When the Body should be blocked, its speed is set to zero along the corresponding
 * axis. Since it may have clipped into the colliding Body, its position is then "snapped"
 * to where it should have been blocked.
 *
 * @author Louis Brunet
 */
public abstract class SolidActor extends PhysicsActor {

    /**
     * The default body types to use if no solid body types are given.
     */
    private static final Body.Type[] DEFAULT_SOLID_BODY_TYPES = {
            Body.Type.GROUND,
            Body.Type.TILE_ENTITY};

    /**
     * The default body types to use if no jump-through body types are given.
     */
    private static final Body.Type[] DEFAULT_JUMP_THROUGH_BODY_TYPES = {
            Body.Type.JUMP_THROUGH};

    /**
     * The order used when this actor's Body's position is snapped to undo clipping into
     * colliding bodies in all directions.
     */
    private static final CollisionDirection[] SNAP_ORDER = {
            CollisionDirection.LEFT,
            CollisionDirection.RIGHT,
            CollisionDirection.UP,
            CollisionDirection.DOWN};

    private final static float WALK_OVER_PROPORTION = 1f / 3f;

    /**
     * The types of bodies that should be considered solid bodies by this actor.
     */
    private final Body.Type[] solidBodyTypes;

    /**
     * The types of bodies that should be considered jump-through bodies by this actor.
     */
    private final Body.Type[] jumpThroughBodyTypes;

    private boolean fallThrough;

    /**
     * For each collision direction, true is this actor is blocked, false otherwise.
     */
    private final Map<CollisionDirection, Boolean> blocked;

    /**
     * For each collision direction, a set of all bodies making contact.
     */
    private final Map<CollisionDirection, Set<Body>> touching;

    /**
     * For each collision direction, the value of the x or y coordinate where this actor
     * should have been blocked.
     */
    private final Map<CollisionDirection, Float> whereToSnap;

    /**
     * Creates a SolidActor given the given name, texture, and body. The given arrays are
     * used to detect whether a Body should be considered "solid" or "jump-through".
     *
     * @param name the name of this actor
     * @param b the Body whose position this actor should copy
     * @param solidBodyTypes the body types that should be considered solid
     * @param jumpTroughBodyTypes the body types that should be considered jump-through
     */
    public SolidActor(String name, Body b,
                      Body.Type[] solidBodyTypes, Body.Type[] jumpTroughBodyTypes) {
        super(name, b);
        Objects.requireNonNull(solidBodyTypes);

        this.solidBodyTypes = solidBodyTypes;
        this.jumpThroughBodyTypes = jumpTroughBodyTypes;
        this.blocked =
                new EnumMap<CollisionDirection, Boolean>(CollisionDirection.class);
        this.whereToSnap =
                new EnumMap<CollisionDirection, Float>(CollisionDirection.class);
        this.touching =
                new EnumMap<CollisionDirection, Set<Body>>(CollisionDirection.class);
        this.fallThrough = false;

        for (CollisionDirection direction: CollisionDirection.values()) {
            this.touching.put(direction, new HashSet<Body>());
            this.blocked.put(direction, false);
            this.whereToSnap.put(direction, null);
        }

        this.body.addContactListener(new SolidContactListener(this));
    }

    /**
     * Creates a SolidActor given the given name, texture, and body. The default
     * "solid" and "jump-through" body types are used.
     *
     * @param name the name of this actor
     * @param b the Body whose position this actor should copy
     */
    public SolidActor(String name, Body b ) {
        this(name, b,
             SolidActor.DEFAULT_SOLID_BODY_TYPES,
             SolidActor.DEFAULT_JUMP_THROUGH_BODY_TYPES);
    }

    /**
     * Add any actions to perform, particularly any speed- or position-related actions.
     * Called each frame.
     * @param delta the time in seconds since last frame
     */
    protected abstract void addActions(float delta);

    /**
     * Checks if this actor should be blocked in all directions, updates all actions
     * added to this actor, snaps position to avoid clipping, updates speed if necessary.
     * @param delta the time in seconds since last frame
     */
    @Override
    public void act(float delta) {

        this.updateBlocked();
        this.addActions(delta);
        super.act(delta); // updates actions

        this.updateSnappedPosition();

        for (CollisionDirection direction: CollisionDirection.values()) {
            this.handleBlocked(direction);
        }

        for (CollisionDirection direction: SolidActor.SNAP_ORDER) {
            this.snapPosition(direction, this.whereToSnap.get(direction));
        }


        this.updateTouching();

//        // TODO remove
//        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
//            System.out.println("SolidActor added speed : " + body.getAddedSpeed() +
//                               "; touching down : " +
//                               this.touching.get(CollisionDirection.DOWN) +
//                               "; isBlocked down : " +
//                               blocked.get(CollisionDirection.DOWN));
//            System.out.println();
//        }
    }

    /**
     * Updates this actor's blocked map.
     * If this actor is colliding with any bodies in a direction, set to true, set to
     * false otherwise.
     */
    private void updateBlocked() {
        this.blocked.put(CollisionDirection.LEFT,
                         this.touching.get(CollisionDirection.LEFT)
                                      .stream()
                                      .anyMatch(body -> ! this.shouldWalkOver(body)));
        this.blocked.put(CollisionDirection.RIGHT,
                         this.touching.get(CollisionDirection.RIGHT)
                                      .stream()
                                      .anyMatch(body -> ! this.shouldWalkOver(body)));

        this.blocked.put(CollisionDirection.UP,
                         ! this.touching.get(CollisionDirection.UP).isEmpty());
        this.blocked.put(CollisionDirection.DOWN,
                         ! this.touching.get(CollisionDirection.DOWN).isEmpty());

//        for (CollisionDirection direction: CollisionDirection.values()) {
////            boolean isEmpty = this.touching.get(direction).isEmpty();
////            this.blocked.put(direction, ! isEmpty);
//
//            this.blocked.put(direction, this.touching.get(direction)
//                                                     .stream()
//                                                     .anyMatch(body -> ! this.isWalkOver(body)));
//        }
    }

    /**
     * Updates the coordinates to where this actor's Body should be snapped.
     * For each collision direction, finds the closest colliding Body.
     * If it exists, uses its position and dimension to update the position to snap to in
     * that direction.
     * If not, indicate that the position should not be snapped in that direction.
     */
    private void updateSnappedPosition() {
        Body colliding = this.getClosestBlockingBody(CollisionDirection.LEFT);
        Float rightmostCollidingXRight =
                colliding == null ? null : colliding.getXRight();
        this.whereToSnap.put(CollisionDirection.LEFT, rightmostCollidingXRight);

        colliding = this.getClosestBlockingBody(CollisionDirection.RIGHT);
        Float leftmostCollidingXLeft =
                colliding == null ? null : colliding.getXLeft();
        this.whereToSnap.put(CollisionDirection.RIGHT, leftmostCollidingXLeft);

        colliding = this.getClosestBlockingBody(CollisionDirection.UP);
        Float lowestCollidingYBottom =
                colliding == null ? null : colliding.getYBottom();
        this.whereToSnap.put(CollisionDirection.UP, lowestCollidingYBottom);

        colliding = this.getClosestBlockingBody(CollisionDirection.DOWN);
        Float highestCollidingYTop =
                colliding == null ? null : colliding.getYTop();
        this.whereToSnap.put(CollisionDirection.DOWN, highestCollidingYTop);

        Body walkOver = this.getBodyToWalkOver();
        if (walkOver != null)
            this.whereToSnap.put(CollisionDirection.DOWN, walkOver.getYTop());

    }

    private Body getBodyToWalkOver() {

        Body walkOverLeft = this.touching.get(CollisionDirection.LEFT)
                                         .stream()
                                         .filter(this::shouldWalkOver)
                                         .max(Body::compareYTop)
                                         .orElse(null);

        Body walkOverRight = this.touching.get(CollisionDirection.RIGHT)
                                          .stream()
                                          .filter(this::shouldWalkOver)
                                          .max(Body::compareYTop)
                                          .orElse(null);

        if (walkOverLeft == null && walkOverRight == null)
            return null;
        if (walkOverLeft == null)
            return walkOverRight;
        if (walkOverRight == null) {
            return walkOverLeft;
        }
        if (walkOverLeft.getYTop() > walkOverRight.getYTop())
            return walkOverLeft;

        return walkOverRight;
    }

    /**
     * Using this actor's list of colliding bodies in the given direction, returns the
     * Body that should have first blocked this actor.
     *
     * For example, if CollisionDirection.DOWN is given, the returned Body should have
     * the highest y coordinate of all bodies colliding with this actor's Body from below.
     *
     * @param direction the direction in which to check for the closest colliding Body
     * @return the closest colliding Body in the given direction
     */
    private Body getClosestBlockingBody(CollisionDirection direction) {
        switch(direction) {
            case LEFT:
                return this.touching.get(direction)
                                    .stream()
                                    .filter(body -> ! this.shouldWalkOver(body))
                                    .max(Body::compareXRight)
                                    .orElse(null);
            case RIGHT:
                return this.touching.get(direction)
                                    .stream()
                                    .filter(body -> ! this.shouldWalkOver(body))
                                    .min(Body::compareXLeft)
                                    .orElse(null);
            case UP:
                return this.touching.get(direction)
                        .stream()
                        .min(Body::compareYBottom)
                        .orElse(null);
            case DOWN:
                return this.touching.get(direction)
                        .stream()
                        .max(Body::compareYTop)
                        .orElse(null);
            default:
                throw new IllegalArgumentException("CollisionDirection not recognized:"+direction.name());
        }
    }

    private boolean shouldWalkOver(Body colliding) {
        if( ! this.isBlocked(CollisionDirection.DOWN) )
            return false;

        float heightToClimb = colliding.getYTop() - this.body.getYBottom();

        return heightToClimb <= SolidActor.WALK_OVER_PROPORTION * this.body.getBounds().getHalfHeight() * 2f;
    }


    /**
     * Sets this actor's speed along the corresponding axis to zero if necessary.
     * Unblocks this actor's movement in the given direction if necessary.
     *
     * If CollisionDirection.DOWN is given, also sets this actor's Body's "added
     * speed" to the speed of the highest colliding body.
     *
     * @param direction the direction in which this actor should be blocked or
     *                  unblocked if necessary
     */
    private void handleBlocked(CollisionDirection direction) {
        Body closestCollidingBody = this.getClosestBlockingBody(direction);
        if (closestCollidingBody == null)
            return;

        if (this.shouldBlockSpeed(direction, closestCollidingBody))
            this.blockSpeed(direction);
        else if (this.shouldUnblockSpeed(direction, closestCollidingBody))
            this.unblock(direction);

        if(direction.equals(CollisionDirection.DOWN)) {
            if (this.isBlocked(direction))
                this.body.setAddedSpeed(closestCollidingBody.getSpeed());
            else
                this.body.setAddedSpeed(null);
        }
    }

    /**
     * Checks whether this actor's Body's speed should be blocked in the given
     * direction by the given colliding Body.
     *
     * @param direction the direction in which to check
     * @param collidingBody the colliding Body that should maybe be blocking this actor's
     *                      Body's speed
     * @return true if the given Body should block this actor in the given direction,
     *         false otherwise
     */
    private boolean shouldBlockSpeed(CollisionDirection direction, Body collidingBody) {
        Objects.requireNonNull(direction);
        Objects.requireNonNull(collidingBody);
        if ( ! this.blocked.get(direction))
            return false;

        switch(direction) {
            case LEFT:
                return this.body.getTotalSpeed().getX() < collidingBody.getTotalSpeed().getX();// 0f;
            case RIGHT:
                return this.body.getTotalSpeed().getX() > collidingBody.getTotalSpeed().getX();
            case UP:
                return this.body.getTotalSpeed().getY() > collidingBody.getTotalSpeed().getY();
            case DOWN:
                return this.body.getTotalSpeed().getY() < collidingBody.getTotalSpeed().getY();
            default:
                throw new IllegalArgumentException("CollisionDirection not recognized: "+direction.name());
        }
    }

    /**
     * Checks whether this actor's Body's speed should be unblocked in the given
     * direction by the given colliding Body.
     *
     * @param direction the direction in which to check
     * @param collidingBody the closest colliding Body in the given direction
     * @return true this actor's Body's speed should be unblocked in the given direction,
     *         false otherwise
     */
    private boolean shouldUnblockSpeed(CollisionDirection direction, Body collidingBody) {
        Objects.requireNonNull(direction);
        Objects.requireNonNull(collidingBody);
        if ( ! this.blocked.get(direction))
            return false;

        switch(direction) {
            case LEFT:
                return this.body.getTotalSpeed().getX() > collidingBody.getTotalSpeed().getX();
            case RIGHT:
                return this.body.getTotalSpeed().getX() < collidingBody.getTotalSpeed().getX();
            case UP:
                return this.body.getTotalSpeed().getY() < collidingBody.getTotalSpeed().getY();
            case DOWN:
                return this.body.getTotalSpeed().getY() > collidingBody.getTotalSpeed().getY();
            default:
                throw new IllegalArgumentException("CollisionDirection not recognized: "+direction.name());
        }
    }

    /**
     * If this actor's Body is going towards the given direction, sets its speed along the
     * axis corresponding to the given direction to zero.
     *
     * @param direction the direction in which this actor cannot move
     */
    private void blockSpeed(CollisionDirection direction) {
        Objects.requireNonNull(direction);

        switch (direction) {
            case LEFT:
                this.body.setSpeed(
                        Math.max(0f, this.body.getSpeed().getX()),
                        this.body.getSpeed().getY());
                break;
            case RIGHT:
                this.body.setSpeed(
                        Math.min(0f, this.body.getSpeed().getX()),
                        this.body.getSpeed().getY());
                break;
            case UP:
                this.body.setSpeed(
                        this.body.getSpeed().getX(),
                        Math.min(0f, this.body.getSpeed().getY()));
                break;
            case DOWN:
                this.body.setSpeed(
                        this.body.getSpeed().getX(),
                        Math.max(0f, this.body.getSpeed().getY()));
                break;
        }
    }

    /**
     * Puts the value false in this actor's blocked Map for this given position.
     * @param direction the direction in which to unblock movement
     */
    private void unblock(CollisionDirection direction) {
        Objects.requireNonNull(direction);
        this.blocked.put(direction, false);
    }

    /**
     * Snaps this actor's Body's position after a collision in the given direction.
     * Sets the appropriate coordinate as the Float value given.
     *
     * If the given Float is null or this actor is not blocked in the given direction,
     * does nothing.
     *
     * @param direction the direction in which a collision happened
     * @param value the value of the coordinate where a collision happened
     */
    private void snapPosition(CollisionDirection direction, Float value) {
        Objects.requireNonNull(direction);
        if (Objects.isNull(value) || ! this.isBlocked(direction))
            return;

        switch(direction) {
            case DOWN:
                this.body.setPosition(
                        this.body.getPosition().getX(),
                        value + this.body.getBounds().getHalfHeight());
                break;
            case UP:
                this.body.setPosition(
                        this.body.getPosition().getX(),
                        value - this.body.getBounds().getHalfHeight());
                break;
            case LEFT:
                this.body.setPosition(
                        value + this.body.getBounds().getHalfWidth(),
                        this.body.getPosition().getY());
                break;
            case RIGHT:
                this.body.setPosition(
                        value - this.body.getBounds().getHalfWidth(),
                        this.body.getPosition().getY());
                break;
            default:
                throw new IllegalArgumentException("CollisionDirection not recognized: "+direction.name());

        }
    }

    /**
     * Returns the types of bodies that should be considered solid bodies by this actor.
     * @return the types of bodies that should be considered solid bodies by this actor
     */
    public Body.Type[] getSolidBodyTypes() {
        return this.solidBodyTypes;
    }

    /**
     * Returns the types of bodies that should be considered jump-through bodies by this
     * actor.
     * @return the types of bodies that should be considered jump-through bodies by this
     * actor
     */
    public Body.Type[] getJumpThroughBodyTypes() {
        return this.jumpThroughBodyTypes;
    }

    /**
     * Returns true if this actor is blocked in the given direction, false otherwise.
     * @param direction the direction in which to return whether this actor is blocked
     * @return true if this actor is blocked in the given direction, false otherwise
     */
    public boolean isBlocked(CollisionDirection direction) {
        return this.blocked.get(direction);
    }

    public void addTouching(CollisionDirection direction, Body b) {
        Objects.requireNonNull(direction);
        Objects.requireNonNull(b);
        this.touching.get(direction).add(b);
    }

    public void removeTouching(Body b) {
        for(CollisionDirection direction: CollisionDirection.values()) {
            this.removeTouching(direction, b);
        }
    }

    public void removeTouching(CollisionDirection direction, Body b) {
        Objects.requireNonNull(direction);
        Objects.requireNonNull(b);
        this.touching.get(direction).remove(b);
    }

    public boolean isTouching(CollisionDirection direction, Body b) {
        Objects.requireNonNull(direction);
        Objects.requireNonNull(b);
        return this.touching.get(direction).contains(b);
    }

    private void updateTouching() {
        for (CollisionDirection direction: CollisionDirection.values()) {
            this.updateTouching(direction);
        }
    }

    /**
     * Removes any bodies that are in the set of bodies that should be touching this
     * actor's Body, but that are not actually touching this actor's Body.
     * @param direction the direction in which to update the set of touching bodies
     */
    private void updateTouching(CollisionDirection direction) {
        this.touching.get(direction)
                     .removeIf(b -> ! this.isActuallyTouching(direction, b));
    }

    /**
     * Returns true if this actor's Body is actually touching the given body in the
     * given direction, false otherwise.
     * @param direction the direction in which to check if the bodies are touching
     * @param b the body to be checked for collision in the given direction
     * @return true if this actor's Body is actually touching the given body in the
     *         given direction, false otherwise.
     */
    public boolean isActuallyTouching (CollisionDirection direction, Body b) {
        Objects.requireNonNull(direction);
        Objects.requireNonNull(b);

        switch (direction) {
            case LEFT:
                return this.isActuallyTouchingLeft(b);
            case RIGHT:
                return this.isActuallyTouchingRight(b);
            case UP:
                return this.isActuallyTouchingUp(b);
            case DOWN:
                return this.isActuallyTouchingDown(b);

            default:
                throw new IllegalArgumentException("CollisionDirection not recognized.");
        }
    }

    public boolean isActuallyTouchingLeft(Body b) {
        float xRight = b.getXRight();
        float xRightPrevious = b.getPreviousXRight();
        float yTop = b.getYTop();
        float yBottom = b.getYBottom();

        return (yBottom <= this.body.getYTop() && yTop >= this.body.getYBottom())
               && (MathUtils.isEqual(this.body.getXLeft(), xRight, 0.005f)
                   || (this.body.getPreviousXLeft() >= xRightPrevious && this.body.getXLeft() < xRight));
    }

    public boolean isActuallyTouchingRight(Body b) {
        float xLeft = b.getXLeft();
        float xLeftPrevious = b.getPreviousXLeft();
        float yTop = b.getYTop();
        float yBottom = b.getYBottom();

        return (yBottom <= this.body.getYTop() && yTop >= this.body.getYBottom())
               && ( MathUtils.isEqual(this.body.getXRight(), xLeft, 0.005f)
                    || (this.body.getPreviousXRight() <= xLeftPrevious && this.body.getXRight() > xLeft));
    }

    public boolean isActuallyTouchingUp(Body b) {
        if(b.getBounds().getType().equals(BoundingShape.Type.CIRCLE))
            throw new UnsupportedOperationException("solid circle collisions not implemented");

        float yBottomPrevious = b.getPreviousYBottom();
        float yBottom = b.getYBottom();
        float xLeft = b.getXLeft();
        float xRight = b.getXRight();

        return (xLeft <= this.body.getXRight() && xRight >= this.body.getXLeft())
               && (MathUtils.isEqual(this.body.getYTop(), yBottom, 0.005f)
                   || (this.body.getPreviousYTop() < yBottomPrevious && this.body.getYTop() >= yBottom));
    }

    public boolean isActuallyTouchingDown(Body b) {
        if(b.getBounds().getType().equals(BoundingShape.Type.CIRCLE))
            throw new UnsupportedOperationException("solid circle collisions not implemented");

        float yTop = b.getYTop();
        float yTopPrevious = b.getPreviousYTop();
        float xLeft = b.getXLeft();
        float xRight = b.getXRight();

        if (xLeft > body.getXRight() || xRight < body.getXLeft()
            || (body.getPreviousYBottom() < yTopPrevious && body.getYBottom() > yTop)) {
            return false;
        }

        return MathUtils.isEqual(this.body.getYBottom(), yTop, 0.005f)
                || (body.getPreviousYBottom() > yTopPrevious && body.getYBottom() <= yTop);

//        return (xLeft <= this.body.getXRight() && xRight >= this.body.getXLeft())
//               && (MathUtils.isEqual(this.body.getYBottom(), yTop, 0.005f)
//                   || (this.body.getPreviousYBottom() > yTopPrevious && this.body.getYBottom() <= yTop)
//                   || ! (this.body.getPreviousYBottom() < yTop ) );
    }

    public void setFallThrough(boolean fall) {
        if ( fall ) {
            this.touching.get(CollisionDirection.DOWN)
                         .removeIf(b -> Arrays.stream(this.jumpThroughBodyTypes).anyMatch(type->type.equals(b.getType())));
//            for(Body b : this.touching.get(CollisionDirection.DOWN)) {
//                if (this.jumpThroughBodyTypes.stream)
//            }
//            this.touching.get(CollisionDirection.DOWN).removeAll(toRemove);
        }

        this.fallThrough = fall;
    }

    public boolean shouldFallThroughJumpThroughCollisions() {
        return this.fallThrough;
    }

}
