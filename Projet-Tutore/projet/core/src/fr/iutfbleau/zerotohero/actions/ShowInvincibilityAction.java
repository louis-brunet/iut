package fr.iutfbleau.zerotohero.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

/**
 * Changes an actor's color alpha value to appear as if the actor is blinking in and out.
 * Applies for a duration to show a character's invincibility after taking damage.
 */
public class ShowInvincibilityAction extends TemporalAction {
    /**
     * The bounds between which to interpolate the alpha value of this action's actor's color.
     */
    private float minAlpha, maxAlpha;

    public ShowInvincibilityAction() {
        this(1f, 0f);
    }

    public ShowInvincibilityAction(float duration, float minAlpha) {
        super(duration);

        this.init(duration, minAlpha);
    }

    /**
     * Initializes this action with the given duration and minimum alpha value.
     * @param duration the initial duration of this action
     * @param minAlpha the minimum alpha value to use for interpolation
     */
    public void init(float duration, float minAlpha) {
        super.setDuration(duration);
        this.minAlpha = minAlpha;
    }

    /**
     * Sets the actor performing this action.
     * Sets the maximum alpha value to the actor's color's alpha value.
     *
     * @param actor the actor performing this action
     */
    @Override
    public void setActor(Actor actor) {
        super.setActor(actor);
        if (actor != null) {
            this.maxAlpha = actor.getColor().a;
        } else {
            this.maxAlpha = 1f;
        }
    }

    /**
     * Interpolates the actor's color's alpha value to make the actor disappear and reappear quickly.
     * @param percent this action's progress
     */
    @Override
    protected void update(float percent) {
        if (percent == 1f) {
            this.actor.getColor().a = this.maxAlpha;
        } else {
            float progress = Interpolation.fastSlow.apply(Interpolation.bounceIn.apply(percent));
            this.actor.getColor().a = Interpolation.bounceOut.apply(this.minAlpha,
                                                                    this.maxAlpha,
                                                                    progress);
        }
    }

    /**
     * Resets this action for reuse
     */
    @Override
    public void reset() {
        super.reset();
        this.maxAlpha = 1f;
        this.minAlpha = 0f;
    }
}
