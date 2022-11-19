package fr.iutfbleau.zerotohero.room.connections;

import fr.iutfbleau.zerotohero.room.Room;

public class Connection {
	private Room roomA, roomB;
	private ConnectionPosition posA, posB;
	
	public Connection(Room roomA, ConnectionPosition posA, Room roomB, ConnectionPosition posB) {
		this.roomA = roomA;
		this.posA = posA;
		this.roomB = roomB;
		this.posB = posB;
		this.roomA.setConnection(posA, this);
		this.roomB.setConnection(posB, this);
	}
	
	public Room getRoomA() {
		return roomA;
	}
	
	public ConnectionPosition getPosA() {
		return posA;
	}
	
	public Room getRoomB() {
		return roomB;
	}
	
	public ConnectionPosition getPosB() {
		return posB;
	}
	
	public ConnectionPosition getConnectionPositionOfRoom(Room room) {
		if (room == roomA)
			return posA;
		else if (room == roomB)
			return posB;
		return null;
	}
	
	public Room getOtherRoom(Room room) {
		if (room == roomA)
			return roomB;
		else if (room == roomB)
			return roomA;
		else
			return null;
	}

	public ConnectionPosition getOtherPosition(Room room) {
		if (room == roomA)
			return posB;
		else if (room == roomB)
			return posA;
		else
			return null;
	}
}
