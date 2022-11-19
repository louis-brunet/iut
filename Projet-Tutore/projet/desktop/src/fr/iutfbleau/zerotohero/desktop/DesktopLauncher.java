package fr.iutfbleau.zerotohero.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import fr.iutfbleau.zerotohero.ZeroToHero;

public class DesktopLauncher {
	private static final int WIDTH = 1280, HEIGHT = 720;
	private static final String ICON_PATH = "icon.PNG";
	private static final String WINDOW_TITLE = "Zero to Hero";

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setResizable(false);
		config.setWindowedMode(WIDTH,HEIGHT);
		config.setWindowIcon(ICON_PATH);
		config.setTitle(WINDOW_TITLE);

		new Lwjgl3Application(new ZeroToHero(), config);
	}
}
