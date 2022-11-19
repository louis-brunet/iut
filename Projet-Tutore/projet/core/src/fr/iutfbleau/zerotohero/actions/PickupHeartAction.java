package fr.iutfbleau.zerotohero.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import fr.iutfbleau.zerotohero.actors.Character;

/**
 * Heals a Character. The actor performing this action must implement Character.
 */
public class PickupHeartAction extends Action {
    /**
     * The amount to heal the Character.
     */
    private int amount;

    public PickupHeartAction() {
        this.init(1);
    }

    /**
     * Initializes this action to heal for the given amount.
     *
     * @param amount the ammount to heal
     */
    public void init(int amount) {
        this.amount = amount;
    }

    /**
     * Heals the character.
     * @param delta seconds since last frame
     * @return true
     */
    @Override
    public boolean act(float delta) {
        ((Character) this.actor).heal(this.amount, false);
        return true;
    }

    /**
     * resets this action for reuse.
     */
    @Override
    public void reset() {
        super.reset();
        this.amount = 1;
    }
}
