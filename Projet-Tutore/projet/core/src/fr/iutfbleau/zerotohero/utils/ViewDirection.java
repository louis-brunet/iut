package fr.iutfbleau.zerotohero.utils;

/**
 * Horizontal directions.
 */
public enum ViewDirection {
    LEFT,
    RIGHT;

	/**
	 * Returns the opposite direction
	 * @return the opposite direction
	 */
	public ViewDirection getOpposite() {
		switch (this) {
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;

			default:
				throw new IllegalStateException("ViewDirection not recognized : " + this.name());
		}
	}
}
