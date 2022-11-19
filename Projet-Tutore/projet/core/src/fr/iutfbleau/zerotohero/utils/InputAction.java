package fr.iutfbleau.zerotohero.utils;

/**
 * An InputAction is used with an InputMapper to detect and react to user input.
 */
public enum InputAction {
    MOVE_LEFT(true),
    MOVE_RIGHT(true),
    JUMP,
    PAUSE,
    USE_WEAPON(true),
    FALL_THROUGH(true),
    INTERACT,
    DROP_BOMB,
    
    // DEBUG
    GIVE_KEY,
    GIVE_BOMB,
    GIVE_MONEY(true),
    HEAL,
    DAMAGE,
    GIVE_SHIELD,
    REMOVE_SHIELD,
    SLOWER,
    FASTER,
    RESET_TIME;

    /**
     * true if the action can be triggered by the user holding down a key or mouse button
     */
    private final boolean repeating;

    InputAction() {
        this(false);
    }

    InputAction(boolean repeating) {
        this.repeating = repeating;
    }

    /**
     * @return true if the action can be triggered by the user holding down a key or mouse button, false otherwise.
     */
    public boolean isRepeating() {
        return repeating;
    }
}
