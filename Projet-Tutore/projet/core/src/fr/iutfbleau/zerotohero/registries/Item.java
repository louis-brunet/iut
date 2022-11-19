package fr.iutfbleau.zerotohero.registries;

public class Item implements Registerable<ItemProperties> {
	private ItemProperties properties;
	private String id;
	
	public Item(String id) {
		this.id = id;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setProperties(ItemProperties properties) {
		this.properties = properties;
	}

	@Override
	public ItemProperties getProperties() {
		return this.properties;
	}
}
