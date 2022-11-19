package fr.iutfbleau.zerotohero.physics;

import java.util.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import fr.iutfbleau.zerotohero.utils.Coordinates;

/**
 * An AxisAlignedBoundingBox (AABB) is a rectangular BoundingShape whose sides
 * are all parallel to the x-axis or y-axis.
 *
 * @author Louis Brunet
 */
public class AxisAlignedBoundingBox extends BoundingShape {

//    /**
//     * Half of this AABB's width.
//     */
//    private float halfWidth;
//    /**
//     * Half of this AABB's width.
//     */
//    private float halfHeight;
    /**
     * Half of this BoundingShape's width.
     */
    private float halfWidth;
    /**
     * Half of this BoundingShape's height.
     */
    private float halfHeight;
    /**
     * The radius of the circle that contains this AABB's four corners. Used for
     * collision approximation.
     */
    private float outerRadius;
    /**
     * The radius of the largest circle that could be drawn inside this AABB.
     */
    private float innerRadius;

    /**
     * Creates an AxisAlignedBoundingBox (AABB) with the given half-width,
     * half-height, and whose center is at the given world coordinates.
     *
     * @param center the world coordinates where this AABB's center is
     * @param halfX the half-width to assign to this AABB
     * @param halfY the half-height to assign to this AABB
     */
    public AxisAlignedBoundingBox(Coordinates center,
                                  float halfX, float halfY) {
        super(Type.AABB, center);
        this.halfWidth = halfX;
        this.halfHeight = halfY;

        this.outerRadius = (float) Math.sqrt(halfX * halfX + halfY * halfY);
        this.innerRadius = Math.min(halfX, halfY);
    }

    /**
     * Creates an AxisAlignedBoundingBox (AABB) with the same position and
     * dimensions as the given Rectangle.
     *
     * @param r the Rectangle whose position and dimensions should be copied
     */
    public AxisAlignedBoundingBox(Rectangle r) {
        this(new Coordinates(r.x + r.width / 2f, r.y + r.height / 2f),
                r.width / 2f,
                r.height / 2f);
    }

    @Override
    public void drawDebug(ShapeRenderer renderer, Color color) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(color);
        renderer.rect(this.center.getX() - this.halfWidth,
                this.center.getY() - this.halfHeight,
                this.halfWidth * 2,
                this.halfHeight * 2);
        renderer.end();
    }

    @Override
    public float getHalfWidth() {
        return this.halfWidth;
    }

    @Override
    public float getHalfHeight() {
        return this.halfHeight;
    }

    /**
     * Check for collisions between this and another BoundingShape.
     *
     * @param other the other BoundingShape
     * @return true if the BoundingShapes overlap, false otherwise.
     */
    @Override
    public boolean overlaps(BoundingShape other) {
        Objects.requireNonNull(other);
        switch (other.getType()) {
            case AABB:
                return this.overlapsAABB((AxisAlignedBoundingBox) other);
            case CIRCLE:
                return this.overlapsCircle((CircleBounds) other);
            default:
                throw new IllegalArgumentException("BoundingShape type not recognized.");
        }
    }

    /**
     * Check for collisions between this and another AABB.
     *
     * @param other the other AABB
     * @return true if the AABBs overlap, false otherwise
     */
    private boolean overlapsAABB(AxisAlignedBoundingBox other) {
        Objects.requireNonNull(other);
        return Math.abs(this.center.getX() - other.center.getX())
                < this.halfWidth + other.halfWidth
                && Math.abs(this.center.getY() - other.center.getY())
                        <= this.halfHeight + other.halfHeight;
    }

    /**
     * Checks for collisions between this and a CircleBounds. The collision is
     * done in two phases. The broad phase uses less accurate, but faster,
     * computations, and determines if more accurate detection is necessary.
     * If so, the narrow phase accurately checks for a collision.
     *
     * @param circle the CircleBounds to test for collision
     * @return true if the BoundingShapes overlap, false otherwise
     */
    private boolean overlapsCircle(CircleBounds circle) {
        Objects.requireNonNull(circle);
        float distanceBetweenCentersSq =
                this.center.getDistanceSquared(circle.getCenter());

        float outerRadiusSumSq = (this.outerRadius + circle.getRadius())
                * (this.outerRadius + circle.getRadius());
        if (distanceBetweenCentersSq > outerRadiusSumSq) return false;

        float innerRadiusSumSq = (this.innerRadius + circle.getRadius())
                * (this.innerRadius + circle.getRadius());
        if (distanceBetweenCentersSq < innerRadiusSumSq) return true;

        return this.narrowCheck(circle);
    }

    /**
     * The narrow phase of collision detection between this AABB and a
     * CircleBounds.
     * Returns true if the closest point on this AABB to the given CircleBounds's
     * center is within that CircleBounds.
     * Returns false otherwise.
     *
     * @param circle the CircleBounds to test for overlap with this AABB
     * @return true this AABB and the given CircleBounds overlap, false
     *         otherwise
     */
    private boolean narrowCheck(CircleBounds circle) {
        Objects.requireNonNull(circle);

        return circle.getCenter()
                .getDistanceSquared(this.closestPointToCircle(circle))
                <= (circle.getRadius() * circle.getRadius());
    }

    /**
     * Finds the closest point to the given CircleBounds's center on this AABB.
     *
     * @param circle the CircleBounds to which the closest point on this
     *               AABB must be found
     * @return the closest point to the given CircleBounds's center on this AABB
     */
    // TODO optimize, don't need to create so many Coordinates ?
    private Coordinates closestPointToCircle(CircleBounds circle) {
        Objects.requireNonNull(circle);
        return this.center.add(
                this.center.getCoordinatesTo(circle.getCenter()).clamp(
                    -this.halfWidth,
                    -this.halfHeight,
                    this.halfWidth,
                    this.halfHeight));
    }

    /**
     * Checks if the given point is within this AABB.
     *
     * @param point are these Coordinates in this AABB ?
     * @return true if the given point is within this AABB, false otherwise
     */
    @SuppressWarnings("unused")
	private boolean pointInBounds(Coordinates point) {
        Objects.requireNonNull(point);
        return Math.abs(point.getX() - this.center.getX()) <= this.halfWidth
              && Math.abs(point.getY() - this.center.getY()) <= this.halfHeight;
    }
}
