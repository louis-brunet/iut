package fr.iutfbleau.zerotohero.game;

import com.badlogic.gdx.Preferences;

public class StringSettingModification extends SettingModification<String> {
    protected StringSettingModification(Preferences p, String setting, String value) {
        super(p, setting, value);
    }

    @Override
    public void apply() {
        if (this.newValue == null)
            this.preferences.remove(this.setting);
        else
            this.preferences.putString(this.setting, this.newValue);
    }
}
