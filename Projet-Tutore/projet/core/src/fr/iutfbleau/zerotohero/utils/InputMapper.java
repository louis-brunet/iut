package fr.iutfbleau.zerotohero.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

import fr.iutfbleau.zerotohero.game.GameSettings;

/**
 * Maps keys and mouse buttons to InputActions. Detects if certain InputActions are triggered.
 */
public class InputMapper {
    /**
     * The interface with this game's persistent data.
     */
    private final GameSettings settings;
    /**
     * The Camera to use when mapping cursor screen coordinates to world coordinates.
     */
    private Camera camera;

    /**
     * Creates an InputMapper that uses the given GameSettings.
     * @param s the persistent settings interface
     */
    public InputMapper(GameSettings s) {
        this.settings = s;
    }

    /**
     * Sets the Camera to use when mapping cursor screen coordinates to world coordinates.
     * @param camera the camera to set
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Coordinates getCursorWorldCoordinates() {
        Vector3 unprojected =
                this.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f));
        return new Coordinates(unprojected.x, unprojected.y);
    }

    /**
     * @param action action to test if is triggered
     * @return true if the action is triggered, false otherwise
     */
    public boolean isTriggered(InputAction action) {
        int[] keys = this.settings.getInputKeys(action);
        int[] buttons = this.settings.getInputMouseButtons(action);
        if (action.isRepeating()) {
            return this.isAnyKeyPressed(keys) || this.isAnyButtonPressed(buttons);
        }

        return this.isAnyKeyJustPressed(keys) || this.isAnyButtonJustPressed(buttons);
    }

    /**
     * @param keys keyboard keys to test
     * @return true if any key was pressed, false otherwise
     */
    private boolean isAnyKeyPressed(int[] keys) {
        for (int key: keys)
            if (Gdx.input.isKeyPressed(key)) return true;

        return false;
    }

    /**
     * @param keys keyboard keys to test
     * @return true if any key was just pressed, false otherwise
     */
    private boolean isAnyKeyJustPressed(int[] keys) {
        for (int key: keys)
            if (Gdx.input.isKeyJustPressed(key)) return true;

        return false;
    }

    /**
     * @param buttons mouse buttons
     * @return true if any mouse button was pressed, false otherwise
     */
    private boolean isAnyButtonPressed(int[] buttons) {
        for (int button: buttons)
            if (Gdx.input.isButtonPressed(button)) return true;

        return false;
    }

    /**
     * @param buttons mouse buttons
     * @return true if any mouse button was just pressed, false otherwise
     */
    private boolean isAnyButtonJustPressed(int[] buttons) {
        for (int button: buttons)
            if (Gdx.input.isButtonJustPressed(button)) return true;

        return false;
    }
}
