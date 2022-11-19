package fr.iutfbleau.zerotohero.game;

import com.badlogic.gdx.Preferences;

public class BooleanSettingModification extends SettingModification<Boolean>{
    protected BooleanSettingModification(Preferences p, String setting, Boolean value) {
        super(p, setting, value);
    }

    @Override
    public void apply() {
        if (this.newValue == null)
            this.preferences.remove(this.setting);
        else
            this.preferences.putBoolean(this.setting, this.newValue);
    }
}
