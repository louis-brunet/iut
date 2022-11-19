package fr.iutfbleau.zerotohero.entities;

import java.util.Random;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.PhysicsActor;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.screens.GameplayScreen;

public class Crate extends PhysicsActor implements Destroyable {
    private static final short INTACT_ANIMATION_ID = 0;
    private static final short DAMAGED_ANIMATION_ID = 1;
    private boolean isDamaged = false;
    
	public Crate(String crateIntactImage, String crateDamagedImage, Body body) {
		super("crate", body);

        ZeroToHero.getAssetManager().addAsset(crateIntactImage, Texture.class);
        ZeroToHero.getAssetManager().addAsset(crateDamagedImage, Texture.class);

        this.addAnimation(Crate.INTACT_ANIMATION_ID,
                          ZeroToHero.getAssetManager().getAsset(crateIntactImage,
                                                                Texture.class),
                          1, 1, 0.1f);
        this.addAnimation(Crate.DAMAGED_ANIMATION_ID,
                          ZeroToHero.getAssetManager().getAsset(crateDamagedImage,
                                                                Texture.class),
                          1, 1, 0.1f);
//        for (short i = 0; i < textureFilePaths.length; i++) {
//            ZeroToHero.getAssetManager().addAsset(textureFilePaths[i], Texture.class);
//            this.addAnimation((short) (Chest.CLOSED_ANIMATION_ID + i),
//                              ZeroToHero.getAssetManager().getAsset(textureFilePaths[i], Texture.class),
//                              1, 1, 0.1f);
//        }
        this.setCurrentAnimation(Crate.INTACT_ANIMATION_ID, false);
	}

	@Override
	public void destroy(int amount) {
		if (!this.isDamaged) {
			this.setCurrentAnimation(Crate.DAMAGED_ANIMATION_ID, false);
			this.isDamaged = true;
		} else {
	        Screen screen = ZeroToHero.getCurrentScreen();
	        if (screen instanceof GameplayScreen) {
	        	Random random = new Random();
	        	if (random.nextInt(8) == 0) {
					if (random.nextInt(3) < 2 ) {
		            	System.out.println("Crate created gold");
						PickupableActor gold = Pickupables.gold(random.nextInt(10) + 1,
								this.getRight()-(this.getRight()-this.getX())/2,
								this.getTop()+(this.getTop()-this.getY()));
						gold.getBody().setSpeed(random.nextInt(151)-75, random.nextInt(150));
		            	this.getRoom().getWorld().addBody(gold.getBody());
		            	this.getRoom().addActor(gold);
		            }
		            if (random.nextInt(3) == 0) {
		            	System.out.println("Crate created hearts");
						PickupableActor hearts = Pickupables.hearts(random.nextInt(5) / 5 + 1,
								this.getRight()-(this.getRight()-this.getX())/2,
								this.getTop()+(this.getTop()-this.getY()));
						hearts.getBody().setSpeed(random.nextInt(151)-75, random.nextInt(150));
		            	this.getRoom().getWorld().addBody(hearts.getBody());
		            	this.getRoom().addActor(hearts);
		            }
		            if (random.nextInt(6) == 0) {
		            	System.out.println("Crate created key");
						PickupableActor key = Pickupables.key(
								this.getRight()-(this.getRight()-this.getX())/2,
								this.getTop()+(this.getTop()-this.getY()));
						key.getBody().setSpeed(random.nextInt(151)-75, random.nextInt(150));
		            	this.getRoom().getWorld().addBody(key.getBody());
		            	this.getRoom().addActor(key);
		            }
		            if (random.nextInt(6) == 0) {
		            	System.out.println("Crate created bomb");
						PickupableActor bomb = Pickupables.bomb(
								this.getRight()-(this.getRight()-this.getX())/2,
								this.getTop()+(this.getTop()-this.getY()));
						bomb.getBody().setSpeed(random.nextInt(151)-75, random.nextInt(150));
		            	this.getRoom().getWorld().addBody(bomb.getBody());
		            	this.getRoom().addActor(bomb);
		            }
	        	}
				this.getRoom().removeActor(this, this.body);
				this.remove();
	        }
		}
	}

	@Override
	public boolean canBeDestroyed() {
		return true;
	}
	
	@Override
	public boolean canBeDestroyedByProjectile() {
		return true;
	}
}
