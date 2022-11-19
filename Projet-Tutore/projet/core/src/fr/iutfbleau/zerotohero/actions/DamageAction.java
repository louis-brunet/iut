package fr.iutfbleau.zerotohero.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import fr.iutfbleau.zerotohero.actors.Character;

/**
 * Added to an Actor that must be implement Character. Damages this character.
 */
public class DamageAction extends Action {
    private int amount;

    public DamageAction() {
        this.init(1);
    }

    /**
     * Initialize this action to deal the given amount of damage.
     * @param damage the amount to damage the Character to which this action was added
     */
    public void init(int damage) {
        this.amount = damage;
    }


    /**
     * Damage the character.
     *
     * @param delta seconds since last frame
     * @return true if action is finished
     */
    @Override
    public boolean act(float delta) {
        ((Character) this.actor).damage(this.amount);
        return true;
    }

    /**
     * Resets this action for reuse.
     */
    @Override
    public void reset() {
        super.reset();
        this.amount = 1;
    }
}
