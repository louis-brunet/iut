package fr.iutfbleau.zerotohero.registries;

import fr.iutfbleau.zerotohero.entities.shop.Currency;
import fr.iutfbleau.zerotohero.items.IItemAction;

public class ItemProperties extends Properties {
	private final float width, height, size, price;
	private final Currency currency;
	private final String description, imagePath;
	private final Class<? extends IItemAction> clazz;
	private final Boolean autoPickup;
	
	public ItemProperties(String name, String id, String description, String imagePath, Float width, Float height, Float size, Boolean autoPickup, Class<? extends IItemAction> clazz, Currency currency, float price) {
		super(name, id);
		this.description = description;
		this.width = width;
		this.height = height;
		this.size = size;
		this.imagePath = imagePath;
		this.clazz = clazz;
		this.autoPickup = autoPickup;
		this.currency = currency;
		this.price = price;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public Class<? extends IItemAction> getActionClass() {
		return clazz;
	}
	
	public IItemAction getAction() {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Boolean isAutoPickup() {
		return autoPickup;
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

	public float getPrice() {
		return price;
	}

	public Currency getCurrency() {
		return currency;
	}
}
