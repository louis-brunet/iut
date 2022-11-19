package fr.iutfbleau.zerotohero.actors;

import fr.iutfbleau.zerotohero.entities.Pickupable;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.physics.ContactListener;

public class PickupContactListener implements ContactListener {

    private final Player player;

    public PickupContactListener(Player player) {
        this.player = player;
    }

    @Override
    public void onContact(Body b) {
    }

    @Override
    public void onContactEnded(Body b) { }

    @Override
    public void onContactStarted(Body b) {
        PhysicsActor actor = b.getActor();
        if (actor instanceof Pickupable) {
            Pickupable pickupable = (Pickupable) actor;

            if (pickupable.isAutoPickup()) {
                this.player.addAction(pickupable.getPickupAction());

                player.getRoom().removeActor(actor, b);
                actor.remove();
            }
        }
    }
}
