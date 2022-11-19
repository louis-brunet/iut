package fr.iutfbleau.zerotohero.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.*;

public class AudioPlayer {

    private final GameSettings settings;
    private final Assets assets;
    private final Set<Music> musicBeingPlayed;

    public AudioPlayer(GameSettings s, Assets a) {
        this.settings = s;
        this.assets = a;
        this.musicBeingPlayed = new HashSet<>();
    }

    public void playSound(String fileName) {
        Sound s = this.assets.getSound(fileName);
        s.play(this.settings.getSoundVolume());
    }

    public void playMusic(String fileName, boolean looping) {
        Music m = this.assets.getMusic(fileName);
        m.setLooping(looping);
        m.setVolume(this.settings.getMusicVolume());
        m.setOnCompletionListener(new MusicCompletionListener(this));
        m.play();
        this.musicBeingPlayed.add(m);
    }

    /**
     *
     * @param fileName
     * @throws NullPointerException
     */
    public void stopMusic(String fileName) {
        Music music = this.assets.getMusic(fileName);
        music.stop();
        this.musicBeingPlayed.remove(music);
    }

    public void updateMusicVolume() {
        float volume = this.settings.getMusicVolume();
        for (Music m: this.musicBeingPlayed) {
            m.setVolume(volume);
        }
    }

    private static class MusicCompletionListener implements Music.OnCompletionListener {
        private final AudioPlayer player;
        public MusicCompletionListener(AudioPlayer p) { this.player = p; }

        /**
         * Called when the end of a media source is reached during playback.
         *
         * @param music the Music that reached the end of the file
         */
        @Override
        public void onCompletion(Music music) {
            if ( !music.isLooping()) {
                this.player.musicBeingPlayed.remove(music);
                music.setOnCompletionListener(null);
            }
        }
    }
}
