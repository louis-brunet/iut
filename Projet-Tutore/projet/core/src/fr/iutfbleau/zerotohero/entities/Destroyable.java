package fr.iutfbleau.zerotohero.entities;

public interface Destroyable {
	public void destroy(int amount);
	
	public boolean canBeDestroyed();
	
	public boolean canBeDestroyedByProjectile();
}
