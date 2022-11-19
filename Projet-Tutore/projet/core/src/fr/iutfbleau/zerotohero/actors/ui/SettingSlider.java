package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.game.GameSettings;

public class SettingSlider extends Table {
    private static final SliderStyle SLIDER_STYLE = new SliderStyle(
            ZeroToHero.getAssetManager().createColorDrawable(1, 10, new Color(0,0,0,0.1f)),
            ZeroToHero.getAssetManager().createColorDrawable(16, 16, Color.RED));
    private static final LabelStyle LABEL_STYLE = new LabelStyle(
            ZeroToHero.getAssetManager().getDefaultFont(), Color.WHITE);

    private static String format(float value) {
        return String.format("%3d", MathUtils.floor(100f * value));
    }

    private final Preferences preferences;
    private final String setting;
    private final Slider slider;
    private final Label valueLabel;

    public SettingSlider(GameSettings settings, Preferences prefs, String setting, String label) {
        this(settings, prefs, setting, label, SettingSlider.LABEL_STYLE, null);
    }

    public SettingSlider(GameSettings settings, Preferences prefs, String setting, String label, Runnable onChange) {
        this(settings, prefs, setting, label, SettingSlider.LABEL_STYLE, onChange);
    }

    public SettingSlider(GameSettings settings, Preferences prefs, String setting, String label, LabelStyle ls, Runnable onChange) {
        this(settings, prefs, setting, label, ls, 0f, 1f, 0.01f, false, SettingSlider.SLIDER_STYLE, onChange);
    }

    public SettingSlider(GameSettings settings, Preferences preferences, String setting, String label, LabelStyle ls,
                         float min, float max, float stepSize, boolean vertical,
                         SliderStyle sliderStyle, Runnable onChange) {
        this.preferences = preferences;
        this.setting = setting;

//        float currentValue = preferences.getFloat(setting);

        Label nameLabel = new Label(label, ls);
        nameLabel.setAlignment(Align.right);
        this.valueLabel = new Label(""/*SettingSlider.format(currentValue)*/, ls);
        this.valueLabel.setAlignment(Align.right);

        this.slider = new Slider(min, max, stepSize, vertical, sliderStyle);
//        this.slider.setValue(currentValue);
        this.slider.addListener(new SettingSliderChangeListener(this.valueLabel, settings, preferences, setting, onChange));


        this.add(nameLabel).expand().fill().pad(8);
        this.add(this.slider).width(300).pad(8);
        this.add(this.valueLabel).width(40).pad(8);


        this.resetValue();
//        this.setDebug(true, true);
    }

    public void resetValue() {
        float currentValue = this.preferences.getFloat(this.setting);
        this.slider.setValue(currentValue);
        this.valueLabel.setText(SettingSlider.format(currentValue));
    }

    private static class SettingSliderChangeListener extends ChangeListener {
        private final Label valueLabel;
        private final GameSettings settings;
        private final Preferences preferences;
        private final String setting;
        private final Runnable onChange;

        public SettingSliderChangeListener(Label value, GameSettings settings, Preferences prefs, String setting, Runnable onChange) {
            this.valueLabel = value;
            this.settings = settings;
            this.setting = setting;
            this.preferences = prefs;
            this.onChange = onChange;
        }

        @Override
        public void changed(ChangeEvent event, Actor actor) {
            float value = ((Slider) actor).getValue();
            this.valueLabel.setText(SettingSlider.format(value));

            this.settings.setFloatSetting(this.preferences, this.setting, value);

            if (this.onChange != null)
                this.onChange.run();
        }

        @Override
        public boolean handle(Event event) {
            super.handle(event);
            event.stop();
            return true;
        }
    }
}
