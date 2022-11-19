package fr.iutfbleau.zerotohero.room.connections;

public enum ConnectionPosition {
	TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;

//	public static List<ConnectionPosition> getValues() {
//		return Arrays.asList(TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT);
//	}

	public ConnectionPosition getOpposite() {
		switch (this) {
		case BOTTOM_LEFT:
			return BOTTOM_RIGHT;
		case BOTTOM_RIGHT:
			return BOTTOM_LEFT;
		case TOP_LEFT:
			return TOP_RIGHT;
		case TOP_RIGHT:
			return TOP_LEFT;
		default:
			return this;
		}
	}
}
