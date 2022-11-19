package fr.iutfbleau.zerotohero.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import fr.iutfbleau.zerotohero.utils.InputAction;

import java.util.LinkedList;
import java.util.List;

public class GameSettings {
    // setting names
    public static final String SOUND_VOLUME = "sound_volume";
    public static final String MUSIC_VOLUME = "music_volume";
    private static final String INITIALIZED_AUDIO = "initialized_audio";
    private static final String INITIALIZED_INPUT_SETTING = "initialized_input_mappings";

    /**
     * Used to separate values for settings that accept multiple values.
     */
    private static final String SEPARATOR = ":";
    /**
     * Maximum number of input mappings per input action.
     */
    private static final int MAX_INPUT_BINDING_COUNT = 3;

    /**
     * Converts a String setting value to the int[] it represents.
     *
     * @param pref the setting value
     * @return the parsed int values
     */
    private static int[] prefValueToIntArray(String pref) {
        String[] split = pref.split(GameSettings.SEPARATOR);
        int[] res = new int[split.length - 1];
        for (int i = 0; i < res.length; i++) {
            res[i] = Integer.parseInt(split[i + 1], 10);
        }
        return res;
    }

    private final Preferences general = Gdx.app.getPreferences("ZTH_General");
    private final Preferences keyInput = Gdx.app.getPreferences("ZTH_Input_Key");
    private final Preferences mouseButtonInput = Gdx.app.getPreferences("ZTH_Input_Mouse");
    private final List<SettingModification<?>> pendingModifications = new LinkedList<>();

    public GameSettings() {
        boolean updated = false;
        if ( ! this.general.getBoolean(INITIALIZED_INPUT_SETTING, false)) {
            this.initDefaultInputMappings();
            this.general.putBoolean(INITIALIZED_INPUT_SETTING, true);

            updated = true;
        }
        if (! this.general.getBoolean(INITIALIZED_AUDIO, false)) {
            this.initAudioSettings();
            this.general.putBoolean(INITIALIZED_AUDIO, true);
            updated = true;
        }

        if (updated)
            this.savePreferences();
    }

    private void initDefaultInputMappings() {
        this.addInputKey(InputAction.MOVE_LEFT, Input.Keys.A);
        this.addInputKey(InputAction.MOVE_RIGHT, Input.Keys.D);
        this.addInputKey(InputAction.JUMP, Input.Keys.SPACE);
        this.addInputKey(InputAction.JUMP, Input.Keys.W);
        this.addInputKey(InputAction.PAUSE, Input.Keys.ESCAPE);
        this.addInputKey(InputAction.PAUSE, Input.Keys.P);
        this.addInputKey(InputAction.FALL_THROUGH, Input.Keys.S);
        this.addInputKey(InputAction.INTERACT, Input.Keys.E);
        this.addInputKey(InputAction.DROP_BOMB, Input.Keys.F);

        // DEBUG
        this.addInputKey(InputAction.GIVE_KEY, Input.Keys.NUM_1);
        this.addInputKey(InputAction.GIVE_KEY, Input.Keys.NUMPAD_1);
        this.addInputKey(InputAction.GIVE_BOMB, Input.Keys.NUM_2);
        this.addInputKey(InputAction.GIVE_BOMB, Input.Keys.NUMPAD_2);
        this.addInputKey(InputAction.GIVE_MONEY, Input.Keys.NUM_3);
        this.addInputKey(InputAction.GIVE_MONEY, Input.Keys.NUMPAD_3);
        this.addInputKey(InputAction.HEAL, Input.Keys.NUM_4);
        this.addInputKey(InputAction.HEAL, Input.Keys.NUMPAD_4);
        this.addInputKey(InputAction.DAMAGE, Input.Keys.NUM_5);
        this.addInputKey(InputAction.DAMAGE, Input.Keys.NUMPAD_5);
        this.addInputKey(InputAction.GIVE_SHIELD, Input.Keys.NUM_7);
        this.addInputKey(InputAction.GIVE_SHIELD, Input.Keys.NUMPAD_7);
        this.addInputKey(InputAction.REMOVE_SHIELD, Input.Keys.NUM_8);
        this.addInputKey(InputAction.REMOVE_SHIELD, Input.Keys.NUMPAD_8);
        this.addInputKey(InputAction.SLOWER, Input.Keys.DOWN);
        this.addInputKey(InputAction.FASTER, Input.Keys.UP);
        this.addInputKey(InputAction.RESET_TIME, Input.Keys.SHIFT_RIGHT);

        // MOUSE
        this.addInputMouseButton(InputAction.USE_WEAPON, Input.Buttons.LEFT);
        this.addInputMouseButton(InputAction.INTERACT, Input.Buttons.RIGHT);
    }

    private void initAudioSettings() {
        this.setSoundVolume(0.5f);
        this.setMusicVolume(0.5f);
    }

    public Preferences getGeneralPreferences() {
        return general;
    }

//    public Preferences getKeyInputPreferences() {
//        return keyInput;
//    }
//
//    public Preferences getMouseButtonInputPreferences() {
//        return mouseButtonInput;
//    }

    public void savePreferences() {
        for (SettingModification<?> modification: this.pendingModifications) {
            modification.apply();
        }
        this.clearModifications();

        this.general.flush();
        this.keyInput.flush();
        this.mouseButtonInput.flush();

        System.out.println("Saved all settings to local storage.");
    }

    public void setSoundVolume(float volume) {
        this.setFloatSetting(this.general, GameSettings.SOUND_VOLUME, volume);
    }

    public float getSoundVolume() {
        return this.getLatestModifiedFloat(this.general, GameSettings.SOUND_VOLUME);
    }

    public void setMusicVolume(float volume) {
        this.setFloatSetting(this.general, GameSettings.MUSIC_VOLUME, volume);
    }

    public float getMusicVolume() {
        return this.getLatestModifiedFloat(this.general, GameSettings.MUSIC_VOLUME);
    }

    public boolean canAddInputMapping(InputAction action) {
        return this.getMappingCount(action) < GameSettings.MAX_INPUT_BINDING_COUNT;
    }

    public int getMappingCount(InputAction action) {
        return this.getInputKeys(action).length + this.getInputMouseButtons(action).length;
    }

    public int[] getInputKeys(InputAction action) {
        String pref = this.getLatestModifiedString(this.keyInput, action.name());

        if (pref == null) return new int[0];

        return GameSettings.prefValueToIntArray(pref);
    }

    public int[] getInputMouseButtons(InputAction action) {
        String pref = this.getLatestModifiedString(this.mouseButtonInput, action.name());

        if (pref == null) return new int[0];

        return GameSettings.prefValueToIntArray(pref);
    }

    public void addInputKey(InputAction action, int key) {
        this.addInputBinding(this.keyInput, action, key);
    }

    public void addInputMouseButton(InputAction action, int button) {
        this.addInputBinding(this.mouseButtonInput, action, button);
    }

    private void addInputBinding(Preferences prefs, InputAction action, int value) {
        String setting = action.name();
        String pref = this.getLatestModifiedString(prefs, setting);
        if (pref == null) pref = "";

        pref += GameSettings.SEPARATOR + value;

        this.pendingModifications.add(new StringSettingModification(prefs, setting, pref));
    }

    public void setFloatSetting(Preferences prefs, String setting, float value) {
        this.pendingModifications.add(new FloatSettingModification(prefs, setting, value));
    }

    private void removeSetting(Preferences prefs, String setting) {
        this.pendingModifications.add(new StringSettingModification(prefs, setting, null));
    }

    public void removeInputMappings(InputAction action) {
        this.removeSetting(this.keyInput, action.name());
        this.removeSetting(this.mouseButtonInput, action.name());
    }

    public String getLatestModifiedString(Preferences prefs, String setting) {
        Object value = prefs.getString(setting);

        for (SettingModification<?> modification: this.pendingModifications) {
            if (modification.getSetting().equals(setting) && modification.getPreferences().equals(prefs)) {

                value = modification.getNewValue();
            }
        }

        return (String) value;
    }

    public Float getLatestModifiedFloat(Preferences prefs, String setting) {
        Object value = prefs.getFloat(setting);

        for (SettingModification<?> modification: this.pendingModifications) {
            if (modification.getSetting().equals(setting) && modification.getPreferences().equals(prefs)) {
                value = modification.getNewValue();
            }
        }

        return (Float) value;
    }

    public void clearModifications() {
        this.pendingModifications.clear();
    }
}
