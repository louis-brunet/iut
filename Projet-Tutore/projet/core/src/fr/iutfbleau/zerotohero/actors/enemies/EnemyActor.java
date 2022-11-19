package fr.iutfbleau.zerotohero.actors.enemies;

import fr.iutfbleau.zerotohero.actors.Character;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.registries.EnemyProperties;

/**
 * An enemy character.
 */
public class EnemyActor extends Character {
    /** Animation id for idle state */
    public static final short IDLE_ANIMATION_ID = 2;
    /** Animation id for running state */
    public static final short RUN_ANIMATION_ID = 0;
    /** Animation id for jumping or falling state */
    public static final short JUMP_ANIMATION_ID = 1;

    /** Default air and grounded horizontal friction  */
    private static final float DEFAULT_FRICTION = 1500f;
    /** Default horizontal acceleration */
    private static final float DEFAULT_ACCELERATION = 1500f;
    /** Default gravity */
    private static final float DEFAULT_GRAVITY = 1000f;
    /** Maximum vertical speed */
    private static final float DEFAULT_VERTICAL_SPEED = 600f;
    /** The enemy type */
    private final EnemyProperties properties;

    public EnemyActor(EnemyProperties properties, Body b, ICharacterAI ai){
        super(properties.getName(), b, (int) properties.getBaseHealth(), (int) properties.getMaxHealth(),
        		0, 0, new Coordinates(properties.getSpeed(), DEFAULT_VERTICAL_SPEED), DEFAULT_ACCELERATION, DEFAULT_FRICTION, DEFAULT_FRICTION,
        		properties.getJumpSpeed(), properties.isDoesFly() ? 0f : DEFAULT_GRAVITY,
				0f, properties.getColor(), ai);
        this.properties = properties;
    }

    /**
     * Returns the enemy's properties
     * @return the enemy's properties
     */
    public EnemyProperties getProperties() {
		return properties;
	}
}
