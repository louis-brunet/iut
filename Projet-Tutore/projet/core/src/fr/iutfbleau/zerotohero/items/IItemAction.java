package fr.iutfbleau.zerotohero.items;

import fr.iutfbleau.zerotohero.weapons.Projectile;

public interface IItemAction {
	/**
	 * Called when picking up the item
	 */
	void onPickup();
	
	/**
	 * Called when an enemy dies
	 */
	void onEnemyDied(float x, float y);
	
	/**
	 * Called when the player shoots the given projectile.
	 *
	 * @param fired the Projectile that was fired
	 */
	void onShoot(Projectile fired);
	
	/**
	 * Called when the player pickups something
	 */
	void onPickupItem();
	
	/**
	 * Called when the player opens a chest
	 */
	void onOpenChest();
}
