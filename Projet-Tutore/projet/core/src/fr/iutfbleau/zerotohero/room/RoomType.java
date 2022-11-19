package fr.iutfbleau.zerotohero.room;

import java.util.Arrays;

public enum RoomType {
	SPAWN, SMALL, TALL_TOP, TALL_BOTTOM, ITEM_SHOP("gui/moneyIcon.png"),
	WEAPON_SHOP("gui/moneyIcon.png"),
	CHALLENGE("gui/minimap/challenge.png"),
	SECRET,//("gui/minimap/secret.png"),
	BOSS("gui/minimap/boss.png");

	private final String mapIconPath;

	RoomType() { this(null); }

	RoomType(String mapIconPath) { this.mapIconPath = mapIconPath; }

	public boolean isTall() {
		return Arrays.asList(tallRooms()).contains(this);
	}
	
	public boolean isSmall() {
		return Arrays.asList(smallRooms()).contains(this);
	}
	
	public boolean isSpecial() {
		return Arrays.asList(specialRooms()).contains(this);
	}

	public String getMapIconPath() {
		return mapIconPath;
	}

	@Override
	public String toString() {
		switch (this) {
		case BOSS:
			return "E";
		case CHALLENGE:
			return "C";
		case ITEM_SHOP:
			return "I";
		case SECRET:
			return "S";
		case SMALL:
			return "N";
		case SPAWN:
			return "D";
		case TALL_BOTTOM:
			return "B";
		case TALL_TOP:
			return "T";
		case WEAPON_SHOP:
			return "W";
		default:
			return ".";
		}
	}

	public boolean countAsRoom() {
		switch (this) {
		case TALL_TOP:
			return false;
		default:
			return true;
		}
	}

	public static RoomType[] tallRooms() {
		return new RoomType[] {SPAWN, TALL_TOP, TALL_BOTTOM};
	}

	public static RoomType[] smallRooms() {
		return new RoomType[] {SMALL, ITEM_SHOP, WEAPON_SHOP, CHALLENGE, SECRET, BOSS};
	}

	public static RoomType[] specialRooms() {
		return new RoomType[] {BOSS, CHALLENGE, ITEM_SHOP, WEAPON_SHOP, SECRET};
	}

	public static String[] getNormalFiles() {
		return new String[] {
				"rooms/dungeonNormal/normal/normal1.tmx",
				"rooms/dungeonNormal/normal/normal2.tmx"
		};
	}
}
