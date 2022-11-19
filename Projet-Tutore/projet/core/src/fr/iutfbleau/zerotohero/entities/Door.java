package fr.iutfbleau.zerotohero.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.PhysicsActor;
import fr.iutfbleau.zerotohero.actors.Player;
import fr.iutfbleau.zerotohero.entities.Door.DoorType.ItemType;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.screens.GameplayScreen;
import fr.iutfbleau.zerotohero.stages.GameplayUI;

public class Door extends PhysicsActor implements Interactable, Destroyable {
	private DoorType type;
    private static final short ANIMATION_ID = 0;
    private short animationId = 0;
    private static final String ACTOR_NAME = "door";
	
	public Door(String textureFilePath, Body b, DoorType type) {
		super(Door.ACTOR_NAME, b);
		this.type = type;

		ZeroToHero.getAssetManager().addAsset(textureFilePath, Texture.class);
        this.addAnimation(Door.ANIMATION_ID,
                          ZeroToHero.getAssetManager().getAsset(textureFilePath, Texture.class),
                          1, 1, 0.1f);
        this.setCurrentAnimation(Door.ANIMATION_ID, false);
	}
	
	public Door(String[] textureFilePaths, Body b, DoorType type) {
		super(Door.ACTOR_NAME, b);
		this.type = type;

		for (short i = 0; i < textureFilePaths.length; i++) {
			ZeroToHero.getAssetManager().addAsset(textureFilePaths[i], Texture.class);
	        this.addAnimation((short) (Door.ANIMATION_ID+i),
	                          ZeroToHero.getAssetManager().getAsset(textureFilePaths[i], Texture.class),
	                          1, 1, 0.1f);
		}
		this.setCurrentAnimation((short) (this.animationId), false);
	}
	
	public enum DoorType {
		LOCKED_ONE_KEY(1, ItemType.KEY),
		LOCKED_TWO_KEYS(2, ItemType.KEY, DoorType.LOCKED_ONE_KEY),
		STONE(1, ItemType.BOMB),
		ROCK(2, ItemType.BOMB, DoorType.STONE);
		
		private int amountOfItems;
		private ItemType itemType;
		private DoorType nextDoorType;
		
		DoorType(int amountOfItems, ItemType itemType) {
			this(amountOfItems, itemType, null);
		}
		
		DoorType(int amountOfItems, ItemType itemType, DoorType nextDoorType) {
			this.amountOfItems = amountOfItems;
			this.itemType = itemType;
			this.nextDoorType = nextDoorType;
		}
		
		public int getAmountOfItems() {
			return amountOfItems;
		}
		
		public ItemType getItemType() {
			return itemType;
		}
		
		public DoorType getNextDoorType() {
			return nextDoorType;
		}
		
		public enum ItemType {
			KEY, BOMB;
		}
	}
	
	@Override
	public void interact() {
		Player player = ZeroToHero.getPlayer();
		GameplayUI ui = ((GameplayScreen) ZeroToHero.getCurrentScreen()).getUI();
		if (player.getAmountOfKeys() > 0) {
//			ZeroToHero.getRoomRenderer().addInfoText("Porte ouverte !", Color.GREEN);
			player.removeKeys(1);
			if (this.type.getNextDoorType() != null) {
				this.type = this.type.getNextDoorType();
				this.animationId++;
				this.setCurrentAnimation(this.animationId, false);
			} else {
				ui.addMessage("Porte ouverte !", Color.GREEN, 2f);
				this.getRoom().removeActor(this, this.body);
				this.remove();
			}
		} else
			ui.addMessage("Pas assez de clés", Color.RED, 2f);
//		else
//			ZeroToHero.getRoomRenderer().addInfoText("Pas assez de clés", Color.RED);
	}
	
	@Override
	public boolean canInteract() {
		return (this.type.getItemType() == ItemType.KEY);
	}

	@Override
	public String getInteractText() {
		return "Unlock";
	}

	@Override
	public void destroy(int amount) {
		if (this.type.getNextDoorType() != null) {
			this.type = this.type.getNextDoorType();
			this.animationId++;
			this.setCurrentAnimation(this.animationId, false);
		} else {
			this.getRoom().removeActor(this, this.body);
			this.remove();
		}
	}

	@Override
	public boolean canBeDestroyed() {
		return (this.type.getItemType() == ItemType.BOMB);
	}

	@Override
	public boolean canBeDestroyedByProjectile() {
		return false;
	}
}
