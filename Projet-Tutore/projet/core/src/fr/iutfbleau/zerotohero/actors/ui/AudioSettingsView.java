package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.game.GameSettings;

public class AudioSettingsView extends Table {

    private static final CharSequence TITLE_TEXT = "Audio";
    private static final String SFX_TEXT = "Sound FX";
    private static final String MUSIC_TEXT = "Music";
    private static final LabelStyle HEADING_STYLE = new LabelStyle(ZeroToHero.getAssetManager().getBigFont(), Color.WHITE);

    private SettingSlider soundSlider, musicSlider;

    public AudioSettingsView(GameSettings settings, Drawable headingBackground) {
//        this.setDebug(true, true);

        this.initHeading(headingBackground);
        this.initContent(settings);
    }

    public void resetValues() {
        this.soundSlider.resetValue();
        this.musicSlider.resetValue();
    }

    private void initHeading(Drawable headingBackground) {
        Label titleLabel = new Label(AudioSettingsView.TITLE_TEXT, AudioSettingsView.HEADING_STYLE);
        titleLabel.setAlignment(Align.center);

        Table titleContainer = new Table();
        titleContainer.setBackground(headingBackground);
        titleContainer.add(titleLabel).expand().fill().pad(8f);
//        titleContainer.setDebug(true);
        this.add(titleContainer).expand().fill().colspan(2);
        this.row();
    }

    private void initContent(GameSettings settings) {
        this.soundSlider = new SettingSlider(settings,
                                             settings.getGeneralPreferences(),
                                             GameSettings.SOUND_VOLUME,
                                             AudioSettingsView.SFX_TEXT);
        this.musicSlider = new SettingSlider(settings,
                                             settings.getGeneralPreferences(),
                                             GameSettings.MUSIC_VOLUME,
                                             AudioSettingsView.MUSIC_TEXT,
                                             ZeroToHero.getAudioPlayer()::updateMusicVolume);

        this.add(soundSlider).fill();
        this.row();
        this.add(musicSlider).fill();
    }
}
