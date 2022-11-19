package fr.iutfbleau.zerotohero.registries;

import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.entities.shop.Currency;

public class WeaponProperties extends Properties {
	private String description, imagePath, chargingImagePath, projectileType, projectileImagePath;
	private Coordinates imageSize, chargingImageSize, projectileImageSize;
	private float damagesMin, damagesMax, projectileSizeMin, projectileSizeMax, projectileSpeedMin, projectileSpeedMax,
			cooldown, chargeDuration, rarity, width, height, size, price;
	private boolean isChargeable;
	private Currency currency;

	public WeaponProperties(String name, String id, String description, String imagePath, Coordinates imageSize,
			String chargingImagePath, Coordinates chargingImageSize, float damagesMin, float damagesMax,
			String projectileType, String projectileImagePath, Coordinates projectileImageSize, float projectileSizeMin,
			float projectileSizeMax, float projectileSpeedMin, float projectileSpeedMax, float cooldown,
			float chargeDuration, float rarity, float width, float height, float size, boolean isChargeable, Currency currency, float price) {
		super(name, id);
		this.description = description;
		this.imagePath = imagePath;
		this.imageSize = imageSize;
		this.chargingImagePath = chargingImagePath;
		this.chargingImageSize = chargingImageSize;
		this.damagesMin = damagesMin;
		this.damagesMax = damagesMax;
		this.projectileType = projectileType;
		this.projectileImagePath = projectileImagePath;
		this.projectileImageSize = projectileImageSize;
		this.projectileSizeMin = projectileSizeMin;
		this.projectileSizeMax = projectileSizeMax;
		this.projectileSpeedMin = projectileSpeedMin;
		this.projectileSpeedMax = projectileSpeedMax;
		this.cooldown = cooldown;
		this.chargeDuration = chargeDuration;
		this.rarity = rarity;
		this.width = width;
		this.height = height;
		this.size = size;
		this.isChargeable = isChargeable;
		this.currency = currency;
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public String getImagePath() {
		return imagePath;
	}
	
	public String getChargingImagePath() {
		return chargingImagePath;
	}
	
	public Coordinates getImageSize() {
		return imageSize;
	}
	
	public Coordinates getChargingImageSize() {
		return chargingImageSize;
	}
	
	public Coordinates getProjectileImageSize() {
		return projectileImageSize;
	}

	public float getDamagesMin() {
		return damagesMin;
	}

	public float getDamagesMax() {
		return damagesMax;
	}

	public String getProjectileType() {
		return projectileType;
	}

	public String getProjectileImagePath() {
		return projectileImagePath;
	}

	public float getProjectileSpeedMin() {
		return projectileSpeedMin;
	}

	public float getProjectileSpeedMax() {
		return projectileSpeedMax;
	}

	public float getProjectileSizeMin() {
		return projectileSizeMin;
	}
	
	public float getProjectileSizeMax() {
		return projectileSizeMax;
	}

	public float getCooldown() {
		return cooldown;
	}

	public float getChargeDuration() {
		return chargeDuration;
	}

	public float getRarity() {
		return rarity;
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

	public boolean isChargeable() {
		return isChargeable;
	}

	public Currency getCurrency() {
		return currency;
	}

	public float getPrice() {
		return price;
	}
}
