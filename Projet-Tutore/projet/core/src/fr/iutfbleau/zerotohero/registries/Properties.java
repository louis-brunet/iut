package fr.iutfbleau.zerotohero.registries;

public abstract class Properties {
	private String name, id;
	
	public Properties(String name, String id) {
		this.name = name;
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}
}
