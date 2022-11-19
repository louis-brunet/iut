//package fr.iutfbleau.zerotohero.entities;
//
//import fr.iutfbleau.zerotohero.physics.Body;
//import fr.iutfbleau.zerotohero.actors.PhysicsActor;
//
//public class TileEntity extends PhysicsActor {
//	private float speed;
//
//	public TileEntity(String name, float x, float y, float width, float height, String textureFilePath, float speed, Body body, boolean animated) {
//		super(name, body);
//		setPosition(x, y);
//		setBounds(x, y, width, height);
//		setName(name);
//		this.setSpeed(speed);
//
//		// Pas d'animation ajoutée. Normal si bugs (Louis)
//	}
//
//	public float getSpeed() {
//		return speed;
//	}
//
//	public void setSpeed(float speed) {
//		this.speed = speed;
//	}
//}
