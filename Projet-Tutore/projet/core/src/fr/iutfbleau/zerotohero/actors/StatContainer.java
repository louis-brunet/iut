package fr.iutfbleau.zerotohero.actors;

/**
 * A StatContainer maps stats to values.
 */
public interface StatContainer {
    /**
     * Sets the value addociated to the given stat.
     *
     * @param stat the stat whose value to modify
     * @param value the value to set
     * @param <V> the value's class
     * @throws ClassCastException if value is of incompatible class
     */
    <V> void setStatValue(Stat stat, V value);

    /**
     * Returns the value associated to the given stat
     * @param stat the stat whose value to return
     * @param valueClass the expected value's class
     * @param <V> the expected value's class
     * @return the stat value
     * @throws ClassCastException if value is of incompatible class
     */
    <V> V getStatValue(Stat stat, Class<V> valueClass);

    /**
     * Returns true if this StatContainer has a value for the given Stat (null included),
     * false otherwise.
     *
     * @param stat the Stat whose existence to test
     * @return true if this StatContainer has this stat, false otherwise
     */
    boolean containsStat(Stat stat);

    default boolean isStatValueSet(Stat stat) {
        return this.getStatValue(stat, stat.getValueClass()) != null;
    }
}
