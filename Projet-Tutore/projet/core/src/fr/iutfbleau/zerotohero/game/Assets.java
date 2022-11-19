package fr.iutfbleau.zerotohero.game;


import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {
	private final AssetManager manager = new AssetManager();
	private final List<Texture> texturesToDispose = new LinkedList<Texture>();

	public Assets() {
		FileHandleResolver resolver = new InternalFileHandleResolver();
		this.addFonts(resolver);
		this.addAudio();

		this.manager.finishLoading();
	}
	
	private void addFonts(FileHandleResolver resolver) {
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		FreeTypeFontLoaderParameter arialNarrowInteract = new FreeTypeFontLoaderParameter();
		arialNarrowInteract.fontFileName = "fonts/arial_narrow_7_interact.ttf";
		arialNarrowInteract.fontParameters.size = 16;
		arialNarrowInteract.fontParameters.borderWidth = 2;
		arialNarrowInteract.fontParameters.borderColor = Color.BLACK;
		manager.load("fonts/arial_narrow_7_interact.ttf", BitmapFont.class, arialNarrowInteract);

		FreeTypeFontLoaderParameter arialNarrowNormal = new FreeTypeFontLoaderParameter();
		arialNarrowNormal.fontFileName = "fonts/arial_narrow_7.ttf";
		arialNarrowNormal.fontParameters.size = 24;
		arialNarrowNormal.fontParameters.borderWidth = 2;
		arialNarrowNormal.fontParameters.borderColor = Color.BLACK;
		manager.load("fonts/arial_narrow_7.ttf", BitmapFont.class, arialNarrowNormal);

		FreeTypeFontLoaderParameter arialNarrowBig = new FreeTypeFontLoaderParameter();
		arialNarrowBig.fontFileName = "fonts/arial_narrow_7_big.ttf";
		arialNarrowBig.fontParameters.size = 28;
		arialNarrowBig.fontParameters.borderWidth = 2;
		arialNarrowBig.fontParameters.borderColor = Color.BLACK;
		manager.load("fonts/arial_narrow_7_big.ttf", BitmapFont.class, arialNarrowBig);

		FreeTypeFontLoaderParameter arialNarrowBigger = new FreeTypeFontLoaderParameter();
		arialNarrowBigger.fontFileName = "fonts/arial_narrow_7_bigger.ttf";
		arialNarrowBigger.fontParameters.size = 32;
		arialNarrowBigger.fontParameters.borderWidth = 3;
		arialNarrowBigger.fontParameters.borderColor = Color.BLACK;
		manager.load("fonts/arial_narrow_7_bigger.ttf", BitmapFont.class, arialNarrowBigger);

		FreeTypeFontLoaderParameter arialNarrowTitle = new FreeTypeFontLoaderParameter();
		arialNarrowTitle.fontFileName = "fonts/arial_narrow_7_title.ttf";
		arialNarrowTitle.fontParameters.size = 142;
		arialNarrowTitle.fontParameters.borderWidth = 5;
		arialNarrowTitle.fontParameters.borderColor = Color.BLACK;
		manager.load("fonts/arial_narrow_7_title.ttf", BitmapFont.class, arialNarrowTitle);
	}

	private void addAudio() {
//		this.manager.setLoader(Sound.class, new SoundLoader(resolver));
//		this.manager.setLoader(Music.class, new MusicLoader(resolver));

		this.manager.load("audio/sounds/jump.wav", Sound.class);

		this.manager.load("audio/music/bgm.mp3", Music.class);
	}

	@Override
	public void dispose() {
		manager.dispose();
		for (Texture t : this.texturesToDispose) {
			t.dispose();
		}
	}
	
	public boolean isAssetLoaded(String filePath) {
		return manager.isLoaded(filePath);
	}
	
	public <T> T getAsset(String filePath, Class<T> type) {
		if (isAssetLoaded(filePath))
			return manager.get(filePath, type);
		else
			return null;
	}
	
	public void addAsset(String filePath, Class<?> type) {
		if (!isAssetLoaded(filePath)) {
			manager.load(filePath, type);
			manager.finishLoading();
			manager.update();
		}
	}
	
	public void removeAsset(String filePath) {
		manager.unload(filePath);
	}

	public BitmapFont getInteractFont() {
		return this.getAsset("fonts/arial_narrow_7_interact.ttf", BitmapFont.class);
	}

	public BitmapFont getDefaultFont() {
		return this.getAsset("fonts/arial_narrow_7.ttf", BitmapFont.class);
	}

	public BitmapFont getBigFont() {
		return this.getAsset("fonts/arial_narrow_7_big.ttf", BitmapFont.class);
	}

	public BitmapFont getBiggerFont() {
		return this.getAsset("fonts/arial_narrow_7_bigger.ttf", BitmapFont.class);
	}

	public BitmapFont getTitleFont() {
		return this.getAsset("fonts/arial_narrow_7_title.ttf", BitmapFont.class);
	}

	// file name (incl. extension), not file path
	public Sound getSound(String fileName) {
		return this.getAsset("audio/sounds/" + fileName, Sound.class);
	}

	// file name (incl. extension), not file path
	public Music getMusic(String fileName) {
		return this.getAsset("audio/music/" + fileName, Music.class);
	}

	public Drawable createColorDrawable(float r, float g, float b, float a) {
		return this.createColorDrawable(new Color(r, g, b, a));
	}

	public Drawable createColorDrawable(Color c) {
		return this.createColorDrawable(1,1,c);
	}

	public Drawable createColorDrawable(int w, int h, Color c) {
		if (c == null)
			return null;

		Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
		pixmap.setColor(c);
		pixmap.fill();
		Texture texture = new Texture(pixmap);
		TextureRegionDrawable drawable = new TextureRegionDrawable(texture);
		pixmap.dispose();

		this.texturesToDispose.add(texture);

		return drawable;
	}
}
