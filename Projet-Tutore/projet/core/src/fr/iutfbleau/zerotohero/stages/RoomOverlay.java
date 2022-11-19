package fr.iutfbleau.zerotohero.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.entities.Interactable;

/**
 * Used to draw unscaled text above a RoomRenderer.
 */
public class RoomOverlay extends Stage {
    /**
     * y offset of price text compared to interact text.
     */
    private static final float PRICE_Y_OFFSET = 20f; // pixels
    /**
     * width and height to display currency icons.
     */
    private static final float PRICE_ICON_SIZE = 20f; // pixels

    /**
     * Font used to draw interact text.
     */
    private final BitmapFont interactFont;
    /**
     * The Viewport to use to convert world to screen coordinates.
     */
    private final Viewport worldViewport;

    /**
     * Creates a RoomOverlay using the given world viewport.
     * @param worldViewport viewport used to convert world to screen coordinates
     */
    public RoomOverlay(Viewport worldViewport) {
        super(new ScreenViewport());
        this.interactFont = ZeroToHero.getAssetManager().getInteractFont();
        this.worldViewport = worldViewport;
    }

    /**
     * Draws the interact text of the Interactable at the given world coordinates.
     *
     * @param interactable interactable whose text to draw
     * @param worldX world x coordinate to draw text
     * @param worldY world y coordinate to draw text
     * @param worldWidth interactable width
     */
    public void drawInteractText(Interactable interactable, float worldX, float worldY, float worldWidth) {
        String text = interactable.getInteractText();
        Coordinates screen = this.worldToScreenCoordinates(worldX, worldY);
        float screenWidth = this.worldToScreenWidth(worldWidth);

        this.getBatch().setProjectionMatrix(this.getCamera().combined);
        this.getBatch().begin();

        this.interactFont.draw(this.getBatch(), text, screen.getX() - screenWidth / 2f, screen.getY(), screenWidth, Align.center, false);

        this.getBatch().end();

    }

    /**
     * Draw the given price text and icon at the given position.
     * Text will be green if affordable, red otherwise.
     *
     * @param priceText the price text to draw
     * @param currencyIcon the currency icon to draw
     * @param affordable true if product is affordable
     * @param actorWorldPosition the actor's center.
     */
    public void drawPrice(String priceText, Texture currencyIcon, boolean affordable, Coordinates actorWorldPosition) {
        Coordinates screenPos = this.worldToScreenCoordinates(actorWorldPosition.getX(), actorWorldPosition.getY());
        screenPos.setY(screenPos.getY() + RoomOverlay.PRICE_Y_OFFSET);

        int textAlign = ( currencyIcon == null ? Align.center : Align.right );
        Color oldTextColor = this.interactFont.getColor().cpy();
        this.interactFont.setColor( affordable ? Color.GREEN : Color.RED );

        this.getBatch().setProjectionMatrix(this.getCamera().combined);
        this.getBatch().begin();

        this.interactFont.draw(this.getBatch(), priceText,
                               screenPos.getX(),
                               screenPos.getY() + interactFont.getLineHeight() / 2f,
                               0f, textAlign, false);

        if (currencyIcon != null) {
            float iconX = screenPos.getX();
            float iconY = screenPos.getY()- RoomOverlay.PRICE_ICON_SIZE / 2f;
            this.getBatch().draw(currencyIcon, iconX, iconY, RoomOverlay.PRICE_ICON_SIZE, RoomOverlay.PRICE_ICON_SIZE);
        }

        this.getBatch().end();

        this.interactFont.setColor(oldTextColor);
    }

    /**
     * Converts the given world coordinates to screen coordinates.
     *
     * @param worldX the world x position
     * @param worldY the world y position
     * @return the converted screen coordinates
     */
    private Coordinates worldToScreenCoordinates(float worldX, float worldY) {
        Vector2 projected = this.worldViewport.project(new Vector2(worldX, worldY));
        return new Coordinates(projected.x, projected.y);
    }

    /**
     * Converts a world width to screen width.
     *
     * @param w world width
     * @return the converted screen width.
     */
    private float worldToScreenWidth(float w) {
        return w * this.getViewport().getScreenWidth() / this.worldViewport.getWorldWidth();
    }

}
