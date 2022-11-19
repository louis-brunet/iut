package fr.iutfbleau.zerotohero.items;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.weapons.Projectile;

public class TestItemAction implements IItemAction {
	@Override
	public void onPickup() {
		ZeroToHero.getPlayer().addShields(1);
		System.out.println("Received 1 shield");
	}

	@Override
	public void onEnemyDied(float x, float y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onShoot(Projectile fired) {
		// TODO Auto-generated method stub

		System.out.println("TestItemAction.onShoot() : Projectile fired - "+fired);

	}

	@Override
	public void onPickupItem() {
		// TODO Auto-generated method stub
		System.out.println("TestItemAction.onPickupItem()");

	}

	@Override
	public void onOpenChest() {
		// TODO Auto-generated method stub
		System.out.println("TestItemAction.onOpenChest()");

	}

}
