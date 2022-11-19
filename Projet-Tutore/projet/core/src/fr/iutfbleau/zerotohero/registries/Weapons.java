package fr.iutfbleau.zerotohero.registries;

public class Weapons extends RegistryList<Weapon, WeaponProperties> {
	public Weapons() {
		super(Weapon.class);
	}

	public static Weapon SIMPLE_GUN = new Weapon("simple_gun");
	public static Weapon SIMPLE_BOW = new Weapon("simple_bow");
	public static Weapon SMG = new Weapon("smg");
}
