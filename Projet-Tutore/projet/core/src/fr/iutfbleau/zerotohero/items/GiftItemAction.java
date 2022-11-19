package fr.iutfbleau.zerotohero.items;

import java.util.Random;

import com.badlogic.gdx.Screen;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.room.Room;
import fr.iutfbleau.zerotohero.screens.GameplayScreen;
import fr.iutfbleau.zerotohero.weapons.Projectile;

public class GiftItemAction implements IItemAction {
	@Override
	public void onPickup() {
		float x = ZeroToHero.getPlayer().getBody().getPosition().getX();
		float y = ZeroToHero.getPlayer().getBody().getPosition().getY();
		generateRandomPickupable(x, y);
	}

	@Override
	public void onEnemyDied(float x, float y) {
		generateRandomPickupable(x, y);
	}
	
	private void generateRandomPickupable(float x, float y) {
		Screen screen = ZeroToHero.getCurrentScreen();
		if (screen instanceof GameplayScreen) {
			Random random = ((GameplayScreen) screen).getSeededRandom();
			Room room = ZeroToHero.getPlayer().getRoom();
			switch (random.nextInt(4)) {
			case 0:
				room.spawnGold(random.nextInt(5)+1, x, y);
				break;
			case 1:
				room.spawnHeart((random.nextInt(5)/5)+1, x, y);
				break;
			case 2:
				room.spawnKey(x, y);
				break;
			case 3:
				room.spawnBomb(x, y);
				break;
			}
		}
	}

	@Override
	public void onShoot(Projectile fired) {
	}

	@Override
	public void onPickupItem() {
	}

	@Override
	public void onOpenChest() {
		// TODO Auto-generated method stub
		System.out.println("GiftItemAction.onOpenChest()");

	}

}
