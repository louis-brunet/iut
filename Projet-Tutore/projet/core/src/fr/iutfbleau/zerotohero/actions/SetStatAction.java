package fr.iutfbleau.zerotohero.actions;

import java.util.Objects;

import fr.iutfbleau.zerotohero.actors.Stat;

/**
 * Set stat containing actor's stat value.
 * @param <V> the value's class
 */
public class SetStatAction<V> extends StatAction {

    /**
     * The value to set.
     */
    private V value;

    public SetStatAction() {
        this(null, null);
    }

    public SetStatAction(Stat stat, V value) {
        super(stat, 0f);
        this.value = value;
    }

    /**
     * Sets the StatContainer's stat.
     * @param percent 0.0-1.0 this action's progress
     */
    @Override
    protected void updateStat(float percent) {
        Objects.requireNonNull(this.getStat());
        Objects.requireNonNull(this.getStatContainer());

        if (percent >= 1)
            this.getStatContainer().setStatValue(this.getStat(), this.value);
    }

    public void setValue(V value) {
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    /**
     * Resets this action as if just initialized with default constructor.
     */
    @Override
    public void reset() {
        super.reset();
        this.value = null;
    }
}
