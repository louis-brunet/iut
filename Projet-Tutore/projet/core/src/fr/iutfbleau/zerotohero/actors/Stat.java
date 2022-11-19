package fr.iutfbleau.zerotohero.actors;

import fr.iutfbleau.zerotohero.utils.Coordinates;

/**
 * The keys to use in a StatContainer's map of key-value entries.
 */
public enum Stat {
    HEALTH(Integer.class),
    MAX_HEARTS(Integer.class),
    MAX_HEALTH(Integer.class),
    SHIELDS(Integer.class),
    MAX_SHIELDS(Integer.class),
    GOLD(Integer.class),
    KEYS(Integer.class),
    BOMBS(Integer.class),
    MAX_SPEED(Coordinates.class),
    MAX_SPEED_FACTOR(Float.class),
    ACCELERATION(Coordinates.class),
    FRICTION_GROUND(Float.class),
    FRICTION_AIR(Float.class),
    JUMP_SPEED(Float.class),
    GRAVITY(Float.class);

    /** The class of the stat's value.  */
    private final Class<?> valueClass;

    Stat(Class<?> valueClass) {
        this.valueClass = valueClass;
    }

    /** Return the stat's value class. */
    public Class<?> getValueClass() {
        return this.valueClass;
    }
}
