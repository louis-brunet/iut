package fr.iutfbleau.zerotohero.registries;

public class Weapon implements Registerable<WeaponProperties> {
	private WeaponProperties properties;
	private String id;
	
	public Weapon(String id) {
		this.id = id;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setProperties(WeaponProperties properties) {
		this.properties = properties;
	}

	@Override
	public WeaponProperties getProperties() {
		return this.properties;
	}
}
