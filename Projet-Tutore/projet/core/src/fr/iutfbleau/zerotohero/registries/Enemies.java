package fr.iutfbleau.zerotohero.registries;

public class Enemies extends RegistryList<Enemy, EnemyProperties> {
	public Enemies() {
		super(Enemy.class);
	}

	public static Enemy TEST_ENEMY = new Enemy("test_enemy");
	public static Enemy SKELETON_ARCHER = new Enemy("skeleton_archer");
}
