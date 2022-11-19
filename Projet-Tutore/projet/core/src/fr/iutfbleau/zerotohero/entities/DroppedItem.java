//package fr.iutfbleau.zerotohero.entities;
//
//import com.badlogic.gdx.scenes.scene2d.Action;
//
//import fr.iutfbleau.zerotohero.actors.PhysicsActor;
//import fr.iutfbleau.zerotohero.physics.Body;
//import fr.iutfbleau.zerotohero.registries.Item;
//
//public class DroppedItem extends PhysicsActor implements Pickupable, Interactable {
//	private Item baseItem;
//
//	public DroppedItem(Item baseItem, Body b) {
//		super(baseItem.getProperties().getName(), b);
//		this.baseItem = baseItem;
//	}
//
//	@Override
//	public void interact() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public boolean canInteract() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public Action getPickupAction() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean isAutoPickup() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	public Item getBaseItem() {
//		return baseItem;
//	}
//}
