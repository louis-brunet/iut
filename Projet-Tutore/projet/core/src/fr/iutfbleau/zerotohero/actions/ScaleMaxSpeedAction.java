package fr.iutfbleau.zerotohero.actions;

import fr.iutfbleau.zerotohero.actors.Stat;

/**
 * Multiplies a Character's max speed for a period of time.
 */
public class ScaleMaxSpeedAction extends StatAction {

    /**
     * The factor by which to multiply the character's max speed
     */
    private float factor;

    public ScaleMaxSpeedAction() { }

    /**
     * Initializes this action to scale a character's max speed by `factor`, for `duration` seconds.
     * @param duration
     * @param factor
     */
    public void init(float duration, float factor) {
        super.init(Stat.MAX_SPEED_FACTOR, duration);

        this.factor = factor;
    }

    @Override
    protected void updateStat(float percent) {

    }

    /**
     * Called when this action is starting to be performed. Sets the character's max speed
     * modifier.
     */
    @Override
    protected void begin() {
        super.begin();

        float modifier = this.getStatContainer().getStatValue(this.getStat(),
                                                              Float.class);
        this.getStatContainer().setStatValue(this.getStat(),
                                             modifier * this.factor);
    }

    /**
     * Called when this action ends. Undoes the max speed modification.
     */
    @Override
    protected void end() {
        super.end();

        float modifier = this.getStatContainer().getStatValue(this.getStat(),
                                                              Float.class);
        this.getStatContainer().setStatValue(this.getStat(), modifier / this.factor);
    }

    /**
     * Resets this action for reuse.
     */
    @Override
    public void reset() {
        super.reset();
        this.factor = 1f;
    }
}
