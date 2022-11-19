package fr.iutfbleau.zerotohero.game;

import com.badlogic.gdx.Preferences;

public class FloatSettingModification extends SettingModification<Float> {
    protected FloatSettingModification(Preferences p, String setting, float value) {
        super(p, setting, value);
    }

    @Override
    public void apply() {
        this.preferences.putFloat(this.setting, this.newValue);
    }
}
