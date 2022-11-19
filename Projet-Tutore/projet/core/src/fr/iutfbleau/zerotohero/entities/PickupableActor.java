package fr.iutfbleau.zerotohero.entities;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Action;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actions.ActionFactory;
import fr.iutfbleau.zerotohero.actors.SolidActor;
import fr.iutfbleau.zerotohero.physics.Body;

public class PickupableActor extends SolidActor implements Pickupable, Interactable {
    public static final short IDLE_ANIMATION_ID = 0;
    private static final float GRAVITY = 600f;
    private static final float MAX_SPEED = 300f;
    private static final float FRICTION = 60f;

    private boolean autoPickup;
    private final Supplier<Action> actionSupplier;

    public PickupableActor(String name, Body b, boolean autoPickup,
                           Supplier<Action> actionSupplier) {
        super(name, b);
        this.autoPickup = autoPickup;
        this.actionSupplier = actionSupplier;
        this.addAction(ActionFactory.gravity(PickupableActor.GRAVITY, PickupableActor.MAX_SPEED));
        this.addAction(ActionFactory.friction(PickupableActor.FRICTION));
    }

    @Override
    public boolean isAutoPickup() {
        return this.autoPickup;
    }

    @Override
    public Action getPickupAction() {
        return this.actionSupplier.get();
    }

    @Override
    public boolean canInteract() {
        return ! this.isAutoPickup();
    }

    @Override
    public String getInteractText() {
        return "Pick up";
    }

    @Override
    public void interact() {
        ZeroToHero.getPlayer().addAction(this.getPickupAction());
        this.getRoom().removeActor(this, this.body);
        this.remove();
    }

    /**
     * Add any actions to perform, particularly any speed- or position-related actions.
     * Called each frame.
     *
     * @param delta the time in seconds since last frame
     */
    @Override
    protected void addActions(float delta) {  }
}
