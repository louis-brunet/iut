package fr.iutfbleau.zerotohero.physics;

import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.room.Room;
import fr.iutfbleau.zerotohero.room.connections.ConnectionPosition;

public class ConnectionBody extends Body {
	private Room room;
	private ConnectionPosition connectionPosition;
	
	public ConnectionBody(Type type, boolean isMovable, BoundingShape bounds, Coordinates position, Room room, ConnectionPosition pos) {
		super(type, isMovable, bounds, position);
		this.room = room;
		this.connectionPosition = pos;
	}

	public Room getRoom() {
		return room;
	}
	
	public ConnectionPosition getConnectionPosition() {
		return connectionPosition;
	}
}
