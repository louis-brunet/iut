package fr.iutfbleau.zerotohero.items;

import fr.iutfbleau.zerotohero.actions.ActionFactory;
import fr.iutfbleau.zerotohero.weapons.Projectile;

public class FastProjectilesItemAction implements IItemAction {
    /**
     * Called when picking up the item
     */
    @Override
    public void onPickup() {

    }

    /**
     * Called when an enemy dies
     *
     * @param x
     * @param y
     */
    @Override
    public void onEnemyDied(float x, float y) {

    }

    /**
     * Called when the player shoots the given projectile.
     *
     * @param fired the Projectile that was fired
     */
    @Override
    public void onShoot(Projectile fired) {
        fired.addAction(ActionFactory.scaleSpeed(2f,2f));
    }

    /**
     * Called when the player pickups something
     */
    @Override
    public void onPickupItem() {

    }

    /**
     * Called when the player opens a chest
     */
    @Override
    public void onOpenChest() {

    }
}
