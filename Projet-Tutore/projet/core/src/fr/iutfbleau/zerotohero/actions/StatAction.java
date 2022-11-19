package fr.iutfbleau.zerotohero.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import fr.iutfbleau.zerotohero.actors.Stat;
import fr.iutfbleau.zerotohero.actors.StatContainer;

import java.util.Objects;

/**
 * Modifies a StatContainer's stat over time.
 */
public abstract class StatAction extends TemporalAction {
    /**
     * The StatContainer whose stat to set.
     */
    private StatContainer container;
    /**
     * The stat to set
     */
    private Stat stat;

    public StatAction() {

    }

    public StatAction (Stat s, float duration) {
        this(s, duration, null);
    }

    public StatAction (Stat s, float duration, Interpolation interpolation) {
        this.init(s, duration, interpolation);
    }

    /**
     * Update the stat.
     *
     * @param percent 0.0-1.0 this action's progress
     */
    protected abstract void updateStat(float percent);

    protected void init(Stat s, float duration) {
        this.init(s, duration, null);
    }

    protected void init(Stat s, float duration, Interpolation interpolation) {
        super.setDuration(duration);
        super.setInterpolation(interpolation);
        this.stat = s;
    }

    /**
     * Update the actor performing this action.
     *
     * @param percent this action's progress
     * @throws NullPointerException if no StatContainer set or no stat was set
     */
    @Override
    protected void update(float percent) {
        Objects.requireNonNull(this.container,
                               "Tried to update StatAction with no StatContainer set.");
        Objects.requireNonNull(this.stat,
                               "Tried to update StatAction with no Stat set.");
        this.updateStat(percent);
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }

    public Stat getStat() {
        return stat;
    }

    /**
     * Sets the actor performing this action. The actor must be a StatContainer.
     *
     * @param actor actor performing this action
     * @throws ClassCastException if actor is not a StatContainer
     */
    @Override
    public void setActor(Actor actor) {
        super.setActor(actor);

        if (actor instanceof StatContainer) {
            this.setStatContainer((StatContainer) actor);
        } else if (actor != null)
            throw new ClassCastException("Given actor is not a StatContainer");
    }

    /**
     * Sets the StatContainer whose stat to modify. Must be non null.
     * @param container the StatContainer whose stat to modify
     */
    private void setStatContainer(StatContainer container) {
        Objects.requireNonNull(container);
        this.container = container;
    }

    public StatContainer getStatContainer() {
        return container;
    }

    /**
     * Resets this action as if just initialized with default constructor.
     */
    @Override
    public void reset() {
        super.reset();
        this.stat = null;
        this.container = null;
    }
}
