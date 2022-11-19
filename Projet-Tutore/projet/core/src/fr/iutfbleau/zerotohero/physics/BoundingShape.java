package fr.iutfbleau.zerotohero.physics;

import java.util.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import fr.iutfbleau.zerotohero.utils.Coordinates;

/**
 * The abstract class BoundingShape represents the bounds of a physics Body.
 * It is responsible for detecting collisions with other BoundingShapes.
 *
 * @author Louis Brunet
 */
public abstract class BoundingShape {

    /**
     * The BoundingShape.Type enum is used to represent all the possible
     * BoundingShape types. It determines the way collision detection is done.
     */
    public enum Type {
        AABB,
        // RECTANGLE,
        CIRCLE
    }

    /**
     * The center of the BoundingShape relative to the physics World.
     */
    protected Coordinates center;
    /**
     * The type of this BoundingShape. Determines the way collision detection is
     * done.
     */
    private BoundingShape.Type type;

    /**
     * Creates a BoundingShape of the specified type, placing it with its center
     * at the given Coordinates.
     *
     * @param type the type to assign to the given Bounding
     * @param center the world coordinates where the center of the shape should
     *               be placed
     */
    public BoundingShape(BoundingShape.Type type, Coordinates center) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(center);
        this.type = type;
        this.center = center;
    }

    /**
     * Checks if this and another BoundingBox overlap.
     *
     * @param other the other BoundingShape
     * @return true if the two shapes overlap, false otherwise
     */
    public abstract boolean overlaps(BoundingShape other);

    /**
     * Returns the half-width of this BoundingShape
     * @return the half-width of this BoundingShape
     */
    public abstract float getHalfWidth();

    /**
     * Returns the half-height of this BoundingShape
     * @return the half-height of this BoundingShape
     */
    public abstract float getHalfHeight() ;

    /**
     * Draw this BoundingShape's outline for debugging purposes.
     *
     * @param batch
     * @param color
     */
    public abstract void drawDebug(ShapeRenderer shapeRenderer, Color color);

    /**
     * Sets this BoundingShape's center to be at the given coordinates
     *
     * @param x the horizontal position to set
     * @param y the vertical position to set
     */
    public void setCenter(float x, float y) {
        this.center.setX(x);
        this.center.setY(y);
    }

    /**
     * Returns this BoundingShape's center as world coordinates.
     *
     * @return this BoundingShape's center as world coordinates
     */
    public Coordinates getCenter() {
        return  this.center;
    }

    /**
     * Returns this BoundingShape's type.
     *
     * @return this BoundingShape's type.
     */
    public BoundingShape.Type getType() {
        return this.type;
    }
}
