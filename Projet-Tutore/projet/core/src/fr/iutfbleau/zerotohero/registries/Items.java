package fr.iutfbleau.zerotohero.registries;

public class Items extends RegistryList<Item, ItemProperties> {
	public Items() {
		super(Item.class);
	}

	public static Item TEST_ITEM = new Item("test_item");
	public static Item GIFT = new Item("gift");
	public static Item GLUE = new Item("glue");
	public static Item FAST_PROJECTILES = new Item("fast_projectiles");
}
