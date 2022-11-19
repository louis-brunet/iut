package fr.iutfbleau.zerotohero.utils;

import com.badlogic.gdx.math.Vector2;

/**
 * Coordinates are a duo of float variables names x and y
 * @author Baptiste L.
 */
public class Coordinates {
	private float x, y;
	
	/**
	 * Creates a new Coordinates with the position (x ; y)
	 * @param x X position of the Coordinates
	 * @param y Y position of the Coordinates
	 */
	public Coordinates(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates a new Coordinates with the position (0 ; 0)
	 */
	public Coordinates() {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * Creates a new Coordinates from a Vector2
	 * @param vector2
	 */
	public Coordinates(Vector2 vector2) {
		this.x = vector2.x;
		this.y = vector2.y;
	}

	/**
	 * Creates copy of the given Coordinates
	 * @param toCopy the Coordinates to copy
	 */
	public Coordinates(Coordinates toCopy) {
		this.x = toCopy.getX();
		this.y = toCopy.getY();
	}

	/**
	 * @return Returns the X position of the Coordinates
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Changes the X position of the Coordinates
	 * @param x The new X position
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return Returns the Y position of the Coordinates
	 */
	public float getY() {
		return y;
	}

	/**
	 * Changes the Y position of the Coordinates
	 * @param y The new Y position
	 */
	public void setY(float y) {
		this.y = y;
	}
	
	/**
	 * @return Returns a Vector2 version of the Coordinates
	 */
	public Vector2 toVector2() {
		return new Vector2(x, y);
	}
	
	/**
	 * Moves the Coordinates by a certain offset on the X axis
	 * @param offset
	 */
	public void moveX(float offset) {
		this.x += offset;
	}
	
	/**
	 * Moves the Coordinates by a certain offset on the Y axis
	 * @param offset
	 */
	public void moveY(float offset) {
		this.y += offset;
	}
	
	/**
	 * Moves the Coordinates by a certain offset on both axis
	 * @param offsetX The offset on the X axis
	 * @param offsetY The offset on the Y axis
	 */
	public void move(float offsetX, float offsetY) {
		moveX(offsetX);
		moveY(offsetY);
	}
	
	/**
	 * Creates a shifted Coordinates from the current Coordinates
	 * @param offsetX The offset on the X axis
	 * @param offsetY The offset on the Y axis
	 * @return Returns the new Coordinates (x+offsetX ; y+offsetY)
	 */
	public Coordinates add(float offsetX, float offsetY) {
		return new Coordinates(this.x + offsetX, this.y + offsetY);
	}

	/**
	 * Creates a new Coordinates equal to the addition of the current Coordinates with the parameter Coordinates
	 * @param coordinates The Coordinates to add to the returned Coordinates
	 * @return Returns the new Coordinates (x1+x2 ; y1+y2)
	 */
	public Coordinates add(Coordinates coordinates) {
		return this.add(coordinates.getX(), coordinates.getY());
	}

	public Coordinates multiply(float factor) {
		return new Coordinates(this.x * factor,
							   this.y * factor);
	}
	
	/**
	 * Creates a Coordinates equal to the substraction of the parameter Coordinates to the current Coordinates
	 * @param coordinates the Coordinates to substract to the returned Coordinates
	 * @return Returns the Coordinates (x2-x1 ; y2-y1)
	 */
	public Coordinates getCoordinatesTo(Coordinates coordinates) {
		float x = coordinates.x-this.x;
		float y = coordinates.y-this.y;
		return new Coordinates(x, y);
	}
	
	/**
	 * Creates a Vector2 equal to the substraction of the parameter Coordinates to the current Coordinates
	 * @param coordinates the Coordinates to substract to the returned Vector2
	 * @return Returns the Vector2 (x2-x1 ; y2-y1)
	 */
	public Vector2 getVectorTo(Coordinates coordinates) {
		float x = coordinates.x-this.x;
		float y = coordinates.y-this.y;
		return new Vector2(x, y);
	}
	
	/**
	 * Calculates the distance between the current Coordinates and the parameter Coordinates
	 * @param coordinates The Coordinates to calculate the distance from
	 * @return Returns the calculated distance
	 */
	public float getDistance(Coordinates coordinates) {
		return (float) Math.sqrt(this.getDistanceSquared(coordinates));
	}

	/**
	 * Calculates the distance^2 between the current Coordinates and the parameter Coordinates
	 * @param coordinates The Coordinates to calculate the distance^2 from
	 * @return Returns the calculated distance^2
	 */
	public float getDistanceSquared(Coordinates coordinates) {
		return (coordinates.x-this.x)*(coordinates.x-this.x)
				+ (coordinates.y-this.y)*(coordinates.y-this.y);
	}
	
	/**
	 * Copies the paramater Coordinates into a new variable
	 * @param coordinates The Coordinates to copy
	 * @return Returns a copy of the Coordinates
	 */
	public static Coordinates copyOf(Coordinates coordinates) {
		return new Coordinates(coordinates.getX(), coordinates.getY());
	}
	
	/**
	 * Returns a clamped version of the current Coordinates 
	 * @param minX minimum X position of the Coordinates
	 * @param minY maximum X position of the Coordinates
	 * @param maxX minimum Y position of the Coordinates
	 * @param maxY maximum Y position of the Coordinates
	 * @return Returns a new Coordinates (minX < x < maxX ; minY < y < maxY)
	 */
	public Coordinates clamp(float minX, float minY, float maxX, float maxY) {
		float x = this.x, y = this.y;
		if (this.x < minX)
			x = minX;
		else if (this.x > maxX)
			x = maxX;
		if (this.y < minY)
			y = minY;
		else if (this.y > maxY)
			y = maxY;
		return new Coordinates(x, y);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Coordinates) {
			Coordinates coordinates = (Coordinates) obj;
			return (this.getX() == coordinates.getX() && this.getY() == coordinates.getY());
		} else if (obj instanceof Vector2) {
			Vector2 vector = (Vector2) obj;
			return (this.getX() == vector.x && this.getY() == vector.y);
		} else
			return false;
	}
	
	@Override
	public String toString() {
		return "("+this.getX()+" ; "+this.getY()+")";
	}

	/**
	 * @param point the point to check this pos from
	 * @return returns true if this is further than point
	 */
	public boolean furtherThan(Coordinates point, boolean xGoBack, boolean yGoBack) {
		boolean check1;
		boolean check2;
		
		if (!xGoBack)
			check1 = this.getX() >= point.getX();
		else
			check1 = this.getX() <= point.getX();
		
		if (!yGoBack)
			check2 = this.getY() >= point.getY();
		else
			check2 = this.getY() <= point.getY();
		
		return check1 && check2;
	}

	public Coordinates clone() {
		return new Coordinates(this);
	}
}
