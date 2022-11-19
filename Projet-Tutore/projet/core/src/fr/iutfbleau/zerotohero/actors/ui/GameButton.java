package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import fr.iutfbleau.zerotohero.ZeroToHero;

public class GameButton extends TextButton {
    public static final float DEFAULT_PADDING = 12f;

    private static final Color UP_TINT = Color.WHITE;
    private static final Color DOWN_TINT = new Color(0x444444ff);
    private static final Color CHECKED_TINT = Color.WHITE;//new Color(0x888888ff);
    private static final String TEXTURE_PATH = "gui/button.png";
    private static final float DEFAULT_MIN_WIDTH = 160f;
    private static final float DEFAULT_MIN_HEIGHT = 42f;

    private static TextButtonStyle defaultStyle(float width, float height) {
        ZeroToHero.getAssetManager().addAsset(GameButton.TEXTURE_PATH, Texture.class);
        Texture t = ZeroToHero.getAssetManager().getAsset(GameButton.TEXTURE_PATH, Texture.class);
        TextureRegionDrawable d = new TextureRegionDrawable(t);

        d.setMinWidth(width);
        d.setMinHeight(height);

        return new TextButtonStyle(
                d.tint(GameButton.UP_TINT),
                d.tint(GameButton.DOWN_TINT),
                d.tint(GameButton.CHECKED_TINT),
                ZeroToHero.getAssetManager().getDefaultFont());
    }

    public GameButton(String text, EventListener clickListener) {
        this(text, clickListener, GameButton.DEFAULT_MIN_WIDTH, GameButton.DEFAULT_MIN_HEIGHT);
    }

    public GameButton(String text, EventListener clickListener, float width) {
        this(text, clickListener, width, GameButton.DEFAULT_MIN_HEIGHT);
    }

    public GameButton(String text, EventListener clickListener, float width, float height) {
        super(text, GameButton.defaultStyle(width, height));
        this.addListener(new SystemCursorChangeListener(Cursor.SystemCursor.Hand, true));
        if (clickListener != null)
            this.addListener(clickListener);
    }


}
