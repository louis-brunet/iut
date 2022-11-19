package fr.iutfbleau.zerotohero.game;


import com.badlogic.gdx.Preferences;

public abstract class SettingModification<T> {

    protected Preferences preferences;
    protected String setting;
    protected T newValue;

    // If value is null, remove setting
    protected SettingModification(Preferences p, String setting, T value) {
        this.preferences = p;
        this.newValue = value;
        this.setting = setting;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public String getSetting() {
        return setting;
    }

    public T getNewValue() {
        return newValue;
    }

    //    public abstract T (T oldValue);

    public abstract void apply();
}
