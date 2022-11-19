package fr.iutfbleau.zerotohero.physics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import fr.iutfbleau.zerotohero.utils.Coordinates;

import java.util.Objects;

/**
 * A CircleBounds is a circular BoundingShape.
 *
 * @author Louis Brunet
 */
public class CircleBounds extends BoundingShape {

    /**
     * The radius of this CircleBounds.
     */
    private float radius;

    /**
     * Creates a CircleBounds whose center is at the given world coordinates,
     * and with the given radius.
     *
     * @param center the world coordinates where the CircleBounds's center
     *               should be.
     * @param radius the radius to assign to the CircleBounds
     */
    public CircleBounds(Coordinates center, float radius) {
        super(Type.CIRCLE, center);
        this.radius = radius;
    }

    @Override
    public void drawDebug(ShapeRenderer renderer, Color color) {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(color);
        renderer.circle(this.center.getX(), this.center.getY(), this.radius);
        renderer.end();
    }

    @Override
    public float getHalfWidth() {
        return this.radius;
    }

    @Override
    public float getHalfHeight() {
        return this.radius;
    }

    /**
     * Checks if this and another BoundingBox overlap.
     *
     * @param other the other BoundingShape
     * @return true if the two shapes overlap, false otherwise
     */
    @Override
    public boolean overlaps(BoundingShape other) {
        Objects.requireNonNull(other);

        switch(other.getType()) {
            case AABB:
                return other.overlaps(this);
            case CIRCLE:
                return this.overlapsCircle((CircleBounds) other);
            default:
                throw new IllegalArgumentException("BoundingShape type not recognized.");
        }
    }

    /**
     * Checks if this and another CircleBounds overlap.
     *
     * @param other the other CircleBounds
     * @return true if the shapes overlap, false otherwise
     */
    private boolean overlapsCircle(CircleBounds other) {
        Objects.requireNonNull(other);

        return this.center.getDistanceSquared(other.center)
                <= (this.radius + other.radius) * (this.radius + other.radius);
    }

    /**
     * Returns the radius of this CircleBounds.
     *
     * @return the radius of this CircleBounds.
     */
    public float getRadius() {
        return this.radius;
    }

    /**
     * Sets the radius of this CircleBounds to the given float.
     *
     * @param radius the radius to set
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }
}
