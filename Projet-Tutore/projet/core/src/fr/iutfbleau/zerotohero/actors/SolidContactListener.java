package fr.iutfbleau.zerotohero.actors;

import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.physics.CollisionDirection;
import fr.iutfbleau.zerotohero.physics.ContactListener;

import java.util.Arrays;
import java.util.Objects;

/**
 * A ContactListener to be added to a SolidActor's Body.
 *
 * On contact (/at the end of contact) with a Body that is considered "solid" or
 * "jump-through" by the SolidActor, check where the contact happened. If necessary,
 * add (/remove) the colliding Body to (/from) the SolidActor's list of colliding bodies.
 *
 * @author Louis Brunet
 */
public class SolidContactListener implements ContactListener {

    /**
     * The SolidActor whose list of colliding bodies should be updated.
     */
    private final SolidActor actor;

    /**
     * Creates a SolidContactListener associated to the given SolidActor.
     *
     * @param sa the SolidActor whose list of colliding bodies should be updated by
     *           this ContactListener
     */
    public SolidContactListener(SolidActor sa) {
        this.actor = sa;
    }

    /**
     * If necessary, inform this SolidContactListener's SolidActor that is is colliding
     * with the given Body.
     *
     * If the Body is considered "solid" by the SolidActor, then the SolidActor is
     * informed of a collision and of its relative position to the SolidActor.
     * If the Body is considered "jump-through" by the SolidActor, then the SolidActor
     * will only be informed if the Body is directly below.
     *
     * @param b the Body colliding with the Body to which this listener was
     */
    @Override
    public void onContact(Body b) {
        boolean isSolid = Arrays.stream(this.actor.getSolidBodyTypes())
                                .anyMatch(type -> Objects.equals(type, b.getType()));
        boolean isJumpThrough = Arrays.stream(this.actor.getJumpThroughBodyTypes())
                                      .anyMatch(type -> Objects.equals(type, b.getType()));

        if (isSolid) {
            // prevents getting stuck when landing on corner of rectangle
            boolean canSidesBeBlocked = true;

            if(this.actor.isActuallyTouchingDown(b)) {
                this.actor.addTouching(CollisionDirection.DOWN, b);
                canSidesBeBlocked = false;
            }
            if(this.actor.isActuallyTouchingUp(b)) {
                this.actor.addTouching(CollisionDirection.UP, b);
                canSidesBeBlocked = false;
            }
            if(canSidesBeBlocked && this.actor.isActuallyTouchingRight(b)) {
                this.actor.addTouching(CollisionDirection.RIGHT, b);
            }
            if(canSidesBeBlocked && this.actor.isActuallyTouchingLeft(b)) {
                this.actor.addTouching(CollisionDirection.LEFT, b);
            }
        } else if (isJumpThrough) {
            if ( ! this.actor.shouldFallThroughJumpThroughCollisions()
                && this.actor.isActuallyTouchingDown(b)) {

                this.actor.addTouching(CollisionDirection.DOWN, b);
            }
        }
//        else {
//            System.out.println("Contact not handled: " + actor.getBody().getName() + " - " + b.getName());
//        }
    }

    /**
     * If necessary, remove the given Body from this SolidContactListner's SolidActor's
     * list of colliding bodies.
     *
     * If the Body is considered "solid" by the SolidActor, do this for all directions.
     * If it is considered "jump-through", do this only if it was touching the bottom
     * of the actor's Body.
     *
     * @param b the Body with which collision has ended
     */
    @Override
    public void onContactEnded(Body b) {
        boolean isSolid = Arrays.stream(this.actor.getSolidBodyTypes())
                .anyMatch(type -> Objects.equals(type, b.getType()));
        boolean isJumpThrough = Arrays.stream(this.actor.getJumpThroughBodyTypes())
                .anyMatch(type -> Objects.equals(type, b.getType()));

        if (isSolid) {
            for (CollisionDirection direction: CollisionDirection.values()) {
//                if (this.actor.isTouching(direction, b)
//                    && ! this.actor.isActuallyTouching(direction, b)) {
                    this.actor.removeTouching(direction, b);
//                }
            }
        } else if (isJumpThrough) {
            if (this.actor.isTouching(CollisionDirection.DOWN, b)
                && ! this.actor.isActuallyTouching(CollisionDirection.DOWN, b)) {
                this.actor.removeTouching(CollisionDirection.DOWN, b);
            }
        }
    }

    @Override
    public void onContactStarted(Body b) { }
}
