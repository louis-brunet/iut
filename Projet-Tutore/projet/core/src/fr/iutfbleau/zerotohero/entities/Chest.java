package fr.iutfbleau.zerotohero.entities;

import java.util.Random;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.PhysicsActor;
import fr.iutfbleau.zerotohero.actors.Player;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.screens.GameplayScreen;
import fr.iutfbleau.zerotohero.stages.GameplayUI;

public class Chest extends PhysicsActor implements Interactable {
    private static final short CLOSED_ANIMATION_ID = 0;
    private static final short OPEN_ANIMATION_ID = 1;
    
    private ChestType type;
    private boolean isOpen;

    public Chest(String closedTextureFilePath, String openTextureFilePath, Body b, ChestType type) {
        super("chest_" + type.name().toLowerCase(), b);
        this.type = type;
        this.isOpen = false;

        ZeroToHero.getAssetManager().addAsset(closedTextureFilePath, Texture.class);
        ZeroToHero.getAssetManager().addAsset(openTextureFilePath, Texture.class);

        this.addAnimation(Chest.CLOSED_ANIMATION_ID,
                          ZeroToHero.getAssetManager().getAsset(closedTextureFilePath,
                                                                Texture.class),
                          1, 1, 0.1f);
        this.addAnimation(Chest.OPEN_ANIMATION_ID,
                          ZeroToHero.getAssetManager().getAsset(openTextureFilePath,
                                                                Texture.class),
                          1, 1, 0.1f);
//        for (short i = 0; i < textureFilePaths.length; i++) {
//            ZeroToHero.getAssetManager().addAsset(textureFilePaths[i], Texture.class);
//            this.addAnimation((short) (Chest.CLOSED_ANIMATION_ID + i),
//                              ZeroToHero.getAssetManager().getAsset(textureFilePaths[i], Texture.class),
//                              1, 1, 0.1f);
//        }
        this.setCurrentAnimation(Chest.CLOSED_ANIMATION_ID, false);
    }

    @Override
    public void interact() {
        // TODO
        switch (this.type) {
            case NORMAL:
                this.open();
                break;
            case LOCKED_KEY:
                this.interactKey();
                break;
            default:
                throw new IllegalStateException("ChestType not recognized : "+type.name());
        }
    }

    @Override
    public boolean canInteract() {
        return ! this.isOpen;
    }

    @Override
    public String getInteractText() {
        return this.type.getInteractText();
    }

    private void interactKey() {
        Player player = ZeroToHero.getPlayer();
        GameplayUI ui = ((GameplayScreen) ZeroToHero.getCurrentScreen()).getUI();
        if (player.getAmountOfKeys() > 0) {
            player.removeKeys(1);
            this.open();
        } else
            ui.addMessage("Pas assez de clés", Color.RED, 2f);
    }

    private void open() {
        Screen screen = ZeroToHero.getCurrentScreen();
        if (screen instanceof GameplayScreen) {
        	Random random = new Random();
    		ZeroToHero.getPlayer().getItems().forEach((item) -> {
    			item.getProperties().getAction().onOpenChest();
    		});
            this.isOpen = true;
            this.setCurrentAnimation(Chest.OPEN_ANIMATION_ID, false);
            if (random.nextInt(3) < 2 ) {
            	System.out.println("Chest created gold");
				PickupableActor gold = Pickupables.gold(random.nextInt(10) + 1,
						this.getRight()-(this.getRight()-this.getX())/2,
						this.getTop()+(this.getTop()-this.getY()));
				gold.getBody().setSpeed(random.nextInt(151)-75, random.nextInt(150));
            	this.getRoom().getWorld().addBody(gold.getBody());
            	this.getRoom().addActor(gold);
            }
            if (random.nextInt(3) == 0) {
            	System.out.println("Chest created hearts");
				PickupableActor hearts = Pickupables.hearts(random.nextInt(5) / 5 + 1,
						this.getRight()-(this.getRight()-this.getX())/2,
						this.getTop()+(this.getTop()-this.getY()));
				hearts.getBody().setSpeed(random.nextInt(151)-75, random.nextInt(150));
            	this.getRoom().getWorld().addBody(hearts.getBody());
            	this.getRoom().addActor(hearts);
            }
            if (random.nextInt(6) == 0) {
            	System.out.println("Chest created key");
				PickupableActor key = Pickupables.key(
						this.getRight()-(this.getRight()-this.getX())/2,
						this.getTop()+(this.getTop()-this.getY()));
				key.getBody().setSpeed(random.nextInt(151)-75, random.nextInt(150));
            	this.getRoom().getWorld().addBody(key.getBody());
            	this.getRoom().addActor(key);
            }
            if (random.nextInt(6) == 0) {
            	System.out.println("Chest created bomb");
				PickupableActor bomb = Pickupables.bomb(
						this.getRight()-(this.getRight()-this.getX())/2,
						this.getTop()+(this.getTop()-this.getY()));
				bomb.getBody().setSpeed(random.nextInt(151)-75, random.nextInt(150));
            	this.getRoom().getWorld().addBody(bomb.getBody());
            	this.getRoom().addActor(bomb);
            }
        }
        // TODO drop item
//        throw new UnsupportedOperationException();
    }
}
