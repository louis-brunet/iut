package fr.iutfbleau.zerotohero.physics;

/**
 * A ContactListener can be added to a Body, which will trigger it upon
 * collision with another Body.
 *
 * @author Louis Brunet
 */
public interface ContactListener {
    /**
     * This method is called by a Body to which this ContactListener was added
     * upon contact with another Body.
     *
     * @param b the Body colliding with the Body to which this listener was
     *          added
     */
    void onContact(Body b);

    /**
     * This method is called by a Body to which this ContactListener was added at the end
     * of contact with another Body.
     *
     * @param b the Body with which collision has ended
     */
    void onContactEnded(Body b);

    void onContactStarted(Body b);
}
