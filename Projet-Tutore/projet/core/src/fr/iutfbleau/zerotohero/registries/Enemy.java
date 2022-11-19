package fr.iutfbleau.zerotohero.registries;

public class Enemy implements Registerable<EnemyProperties> {
	private EnemyProperties properties;
	private String id;
	
	public Enemy(String id) {
		this.id = id;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setProperties(EnemyProperties properties) {
		this.properties = properties;
	}

	@Override
	public EnemyProperties getProperties() {
		return this.properties;
	}
}
