package fr.iutfbleau.zerotohero.registries;

import java.util.Map;

import com.badlogic.gdx.graphics.Color;

import fr.iutfbleau.zerotohero.actors.enemies.ICharacterAI;
import fr.iutfbleau.zerotohero.utils.Coordinates;

public class EnemyProperties extends Properties {
	private String desc, idleImagePath, moveImagePath, jumpImagePath;
	private Coordinates idleImageSize, moveImageSize, jumpImageSize;
	private float baseHealth, maxHealth, baseDamages, speed, jumpSpeed, width, height, size, focusRange;
	private Color color;
	private boolean doesFly;
	private Map<String, Float> weapons;
	private Class<? extends ICharacterAI> ai;

	public EnemyProperties(String name, String id, String desc, Float baseHealth, Float maxHealth, Float baseDamages,
			Float focusRange, Float speed, Float jumpSpeed, Float width, Float height, Float size, Boolean doesFly,
			String idleImagePath, Coordinates idleImageSize, String moveImagePath, Coordinates moveImageSize,
			String jumpImagePath, Coordinates jumpImageSize, Map<String, Float> weapons,
			Class<? extends ICharacterAI> ai, Color color) {
		super(name, id);
		this.desc = desc;
		this.baseHealth = baseHealth;
		this.maxHealth = maxHealth;
		this.baseDamages = baseDamages;
		this.focusRange = focusRange;
		this.speed = speed;
		this.jumpSpeed = jumpSpeed;
		this.width = width;
		this.height = height;
		this.size = size;
		this.doesFly = doesFly;
		this.idleImagePath = idleImagePath;
		this.idleImageSize = idleImageSize;
		this.moveImagePath = moveImagePath;
		this.moveImageSize = moveImageSize;
		this.jumpImagePath = jumpImagePath;
		this.jumpImageSize = jumpImageSize;
		this.weapons = weapons;
		this.ai = ai;
		this.color = color;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getIdleImagePath() {
		return idleImagePath;
	}
	
	public String getMoveImagePath() {
		return moveImagePath;
	}
	
	public String getJumpImagePath() {
		return jumpImagePath;
	}

	public Coordinates getIdleImageSize() {
		return idleImageSize;
	}

	public Coordinates getMoveImageSize() {
		return moveImageSize;
	}

	public Coordinates getJumpImageSize() {
		return jumpImageSize;
	}
	
	public float getBaseHealth() {
		return baseHealth;
	}
	
	public float getMaxHealth() {
		return maxHealth;
	}
	
	public float getBaseDamages() {
		return baseDamages;
	}
	
	public float getFocusRange() {
		return focusRange;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public float getJumpSpeed() {
		return jumpSpeed;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getSize() {
		return size;
	}
	
	public boolean isDoesFly() {
		return doesFly;
	}
	
	public Map<String, Float> getWeapons() {
		return weapons;
	}
	
	public Class<? extends ICharacterAI> getAI() {
		return ai;
	}

	public Color getColor() {
		return this.color;
	}
}
