package fr.iutfbleau.zerotohero.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;

import fr.iutfbleau.zerotohero.room.Room.RoomSize;
import fr.iutfbleau.zerotohero.room.connections.Connection;
import fr.iutfbleau.zerotohero.room.connections.ConnectionPosition;
import fr.iutfbleau.zerotohero.screens.GameplayScreen;
import fr.iutfbleau.zerotohero.utils.Tuple2;

public class Level {
	private RoomType[][] roomTypes;
	private Room[][] rooms;
	private int minRooms, maxRooms;
	private List<Connection> connections;
	private Color[] colors;
	private Random random;
	private GameplayScreen screen;
//	private float chances;
	
	public Level(GameplayScreen screen, Random random, int width, int height, int minRooms, int maxRooms, Color color1, Color color2, Color color3, Color color4) {
//		Room spawnRoom = new Room(screen, "rooms/dungeonNormal/normal/normal1.tmx", Room.RoomSize.NORMAL, Color.BLACK, Color.BLACK, Color.GRAY, Color.GRAY);
		if (width%2 == 0)
			width++;
		if (height%2 == 1)
			height++;
		
		this.random = random;
		this.screen = screen;
		
		colors = new Color[4];
		colors[0] = color1;
		colors[1] = color2;
		colors[2] = color3;
		colors[3] = color4;
		
//		chances = 1;
//		spawnRoom.setRenderingColor(color1, color2, color3, color4);
//		rooms.add(spawnRoom);
//		generate(random, spawnRoom, screen);
		this.minRooms = minRooms;
		this.maxRooms = maxRooms;
		roomTypes = new RoomType[width][height];
		rooms = new Room[width][height];
		connections = new ArrayList<Connection>();
		
		createDungeon();
		
		System.out.println("Level() : nb rooms = " + roomCount());
	}

	private void createDungeon() {
		
		roomTypes[Math.round(roomTypes.length/2f)-1][Math.round(roomTypes[0].length/2f)-1] = RoomType.TALL_BOTTOM;
		roomTypes[Math.round(roomTypes.length/2f)-1][Math.round(roomTypes[0].length/2f)] = RoomType.TALL_TOP;
		
//		Room spawnRoom = new Room(screen, "rooms/dungeonNormal/normal/normal1.tmx", Room.RoomSize.NORMAL, Color.BLACK, Color.BLACK, Color.GRAY, Color.GRAY);
//		spawnRoom.setRenderingColor(colors[0], colors[1], colors[2], colors[3]);

		int count = 0;
		while (roomCount() < minRooms) {
			if (roomCount() > maxRooms)
				break;
			for (int i = 0; i < roomTypes.length; i++) {
				for (int j = 0; j < roomTypes[i].length; j++) {
					if (roomTypes[i][j] != null && roomTypes[i][j] == RoomType.TALL_BOTTOM && random.nextBoolean()) {
						count++;
						System.out.println(count);
						generate(i, j);
					}
				}
			}
			if (count > 10)
				break;
		}
		generateSpecialRooms();
		createRooms();
        System.out.println(this);
		createConnections();
	}
	
	private void generate(int baseX, int baseY) {
		for (ConnectionPosition position : ConnectionPosition.values()) {
			int generatorPosX = baseX, generatorPosY = baseY;
			if (random.nextBoolean()) {
				if (roomTypes[generatorPosX][generatorPosY] != null) {
					int amountOfRooms = 0,
						checkingPosX = generatorPosX, 
						checkingPosY = generatorPosY;
					switch (position) {
					case TOP_LEFT:
						if (roomTypes[generatorPosX][generatorPosY].isTall())
							checkingPosY++;
					case BOTTOM_LEFT:
						checkingPosX--;
						try {
							while (roomTypes[checkingPosX][checkingPosY] == null) {
								amountOfRooms++;
								checkingPosX--;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						break;
					case TOP_RIGHT:
						if (roomTypes[generatorPosX][generatorPosY].isTall())
							checkingPosY++;
					case BOTTOM_RIGHT:
						checkingPosX++;
						try {
							while (roomTypes[checkingPosX][checkingPosY] == null) {
								amountOfRooms++;
								checkingPosX++;
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
						break;
					}
					if (amountOfRooms > 1) {
						int roomsToGenerate = random.nextInt(amountOfRooms);
						switch (position) {
						case TOP_LEFT:
							generatorPosY++;
						case BOTTOM_LEFT:
							generatorPosX--;
							for (int i = 0; i < roomsToGenerate; i++) {
								roomTypes[generatorPosX][generatorPosY] = RoomType.SMALL;
								generatorPosX--;
							}
							if (position == ConnectionPosition.TOP_LEFT) {
								if (generatorPosY + 1 < roomTypes[generatorPosX].length) {
									roomTypes[generatorPosX][generatorPosY] = RoomType.TALL_BOTTOM;
									roomTypes[generatorPosX][generatorPosY+1] = RoomType.TALL_TOP;
								} else {
									roomTypes[generatorPosX][generatorPosY] = RoomType.SMALL;
								}
							} else {
								if ((generatorPosY - 1) >= 0) {
									roomTypes[generatorPosX][generatorPosY-1] = RoomType.TALL_BOTTOM;
									roomTypes[generatorPosX][generatorPosY] = RoomType.TALL_TOP;
								} else {
									roomTypes[generatorPosX][generatorPosY] = RoomType.SMALL;
								}
							}
							break;
						case TOP_RIGHT:
							generatorPosY++;
						case BOTTOM_RIGHT:
							generatorPosX++;
							for (int i = 0; i < roomsToGenerate; i++) {
								roomTypes[generatorPosX][generatorPosY] = RoomType.SMALL;
								generatorPosX++;
							}
							if (position == ConnectionPosition.TOP_RIGHT) {
								if (generatorPosY+1 < roomTypes[generatorPosX].length) {
									roomTypes[generatorPosX][generatorPosY] = RoomType.TALL_BOTTOM;
									roomTypes[generatorPosX][generatorPosY+1] = RoomType.TALL_TOP;
								} else {
									roomTypes[generatorPosX][generatorPosY] = RoomType.SMALL;
								}
							} else {
								if ((generatorPosY - 1) >= 0) {
									roomTypes[generatorPosX][generatorPosY-1] = RoomType.TALL_BOTTOM;
									roomTypes[generatorPosX][generatorPosY] = RoomType.TALL_TOP;
								} else {
									roomTypes[generatorPosX][generatorPosY] = RoomType.SMALL;
								}
							}
							break;
						}
					}
				} else {
					roomTypes[generatorPosX][generatorPosY] = RoomType.SMALL;
				}
			}
		}
	}
	
	private void generateSpecialRooms() {
		List<Tuple2<Integer, Integer>> availableRooms = new ArrayList<Tuple2<Integer, Integer>>();
		for (int i = 0; i < roomTypes.length; i++) {
			for (int j = 0; j < roomTypes[i].length; j++) {
				try {
					if (roomTypes[i][j] == null && ((roomTypes[i-1][j] != null) ^ (roomTypes[i+1][j] != null))) {
						availableRooms.add(new Tuple2<Integer, Integer>(i,j));
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
		}
		if (availableRooms.size() >= RoomType.specialRooms().length) {
			for (RoomType type : RoomType.specialRooms()) {
				int randomRoom = random.nextInt(availableRooms.size());
				Tuple2<Integer, Integer> pos = availableRooms.get(randomRoom);
				availableRooms.remove(randomRoom);
				roomTypes[pos.a][pos.b] = type;
			}
		} else {
			roomTypes = new RoomType[roomTypes.length][roomTypes[0].length];
			rooms = new Room[rooms.length][rooms[0].length];
			connections = new ArrayList<Connection>();
			createDungeon();
		}
	}
	
	private int roomCount() {
		int amountOfRooms = 0;
		for (RoomType[] row : roomTypes) {
			for (RoomType roomType : row) {
				if (roomType != null && roomType.countAsRoom())
					amountOfRooms++;
			}
		}
		return amountOfRooms;
	}
	
	private void createRooms() {
		Color[] shopColors = new Color[] {Color.FOREST, Color.FOREST, Color.OLIVE, Color.OLIVE};
		Color[] bossColors = new Color[] {Color.RED, Color.RED, Color.MAROON, Color.MAROON};
		Color[] secretColors = new Color[] {Color.DARK_GRAY, Color.DARK_GRAY, Color.GRAY, Color.GRAY};
		Color[] challengeColors = new Color[] {Color.GOLDENROD, Color.GOLDENROD, Color.GOLD, Color.GOLD};
		for (int i = 0; i < roomTypes.length; i++) {
			for (int j = 0; j < roomTypes[i].length; j++) {
				RoomType roomType = roomTypes[i][j];
				if (roomType != null && roomType.countAsRoom()) {
					switch (roomType) {
					case BOSS:
						rooms[i][j] = new Room(screen, "rooms/dungeonNormal/boss/boss.tmx", roomType.getMapIconPath(), RoomSize.NORMAL,
								bossColors[0], bossColors[1], bossColors[2], bossColors[3]);
						rooms[i][j].setRenderingColor(bossColors[0], bossColors[1], bossColors[2], bossColors[3]);
						break;
					case CHALLENGE:
						rooms[i][j] = new Room(screen, "rooms/dungeonNormal/challenge/challenge.tmx", roomType.getMapIconPath(), RoomSize.NORMAL,
								challengeColors[0], challengeColors[1], challengeColors[2], challengeColors[3]);
						rooms[i][j].setRenderingColor(challengeColors[0], challengeColors[1], challengeColors[2], challengeColors[3]);
						break;
					case ITEM_SHOP:
						rooms[i][j] = new Room(screen, "rooms/dungeonNormal/shop/itemShop.tmx", roomType.getMapIconPath(), RoomSize.NORMAL,
								shopColors[0], shopColors[1], shopColors[2], shopColors[3]);
						rooms[i][j].setRenderingColor(shopColors[0], shopColors[1],
								shopColors[2], shopColors[3]);
						break;
					case SECRET:
						rooms[i][j] = new Room(screen, "rooms/dungeonNormal/normal/normal1.tmx", roomType.getMapIconPath(), RoomSize.NORMAL,
								secretColors[0], secretColors[1], secretColors[2], secretColors[3]);
						rooms[i][j].setRenderingColor(secretColors[0], secretColors[1], secretColors[2], secretColors[3]);
						break;
					case SMALL:
						String[] allNormals = RoomType.getNormalFiles();
						
						String path = allNormals[random.nextInt(allNormals.length)];
						System.out.println(path);
				        
						rooms[i][j] = new Room(screen, path, roomType.getMapIconPath(), RoomSize.NORMAL,
								colors[0], colors[1], colors[2], colors[3]);
						rooms[i][j].setRenderingColor(colors[0], colors[1], colors[2], colors[3]);
						break;
					case SPAWN:
					case TALL_BOTTOM:
						rooms[i][j] = new Room(screen, "rooms/dungeonNormal/tall/tall1.tmx", roomType.getMapIconPath(),
											   RoomSize.TALL, colors[0], colors[1], colors[2], colors[3]);
						rooms[i][j].setRenderingColor(colors[0], colors[1], colors[2], colors[3]);
						break;
					case WEAPON_SHOP:
						rooms[i][j] = new Room(screen, "rooms/dungeonNormal/shop/weaponShop.tmx", roomType.getMapIconPath(), RoomSize.NORMAL,
								shopColors[0], shopColors[1], shopColors[2], shopColors[3]);
						rooms[i][j].setRenderingColor(shopColors[0], shopColors[1],
								shopColors[2], shopColors[3]);
						break;
					default:
						break;
					}
				}
			}
		}
	}
	
	private void createConnections() {
		for (int i = 0; i < roomTypes.length; i++) {
			for (int j = 0; j < roomTypes[i].length; j++) {
				if (rooms[i][j] != null) {
					ConnectionPosition position = ConnectionPosition.TOP_RIGHT;
					if (rooms[i][j].isTall()) {
						position = ConnectionPosition.BOTTOM_RIGHT;
						try {
							if (roomTypes[i+1][j+1] != null) {
								if (roomTypes[i+1][j+1].isTall()) {
									if (roomTypes[i+1][j+1] == RoomType.TALL_TOP)
										connections.add(new Connection(rooms[i][j], ConnectionPosition.TOP_RIGHT, rooms[i+1][j], ConnectionPosition.TOP_LEFT));
									else
										connections.add(new Connection(rooms[i][j], ConnectionPosition.TOP_RIGHT, rooms[i+1][j+1], ConnectionPosition.BOTTOM_LEFT));
								} else {
									connections.add(new Connection(rooms[i][j], ConnectionPosition.TOP_RIGHT, rooms[i+1][j+1], ConnectionPosition.TOP_LEFT));
								}
							}
						} catch (ArrayIndexOutOfBoundsException e) {}
					}
					try {
						if (roomTypes[i+1][j] != null) {
							if (!(roomTypes[i][j].isSpecial() && roomTypes[i+1][j].isSpecial())) {
								if (roomTypes[i+1][j].isTall()) {
									if (roomTypes[i+1][j] == RoomType.TALL_TOP)
										connections.add(new Connection(rooms[i][j], position, rooms[i+1][j-1], ConnectionPosition.TOP_LEFT));
									else
										connections.add(new Connection(rooms[i][j], position, rooms[i+1][j], ConnectionPosition.BOTTOM_LEFT));
								} else {
									connections.add(new Connection(rooms[i][j], position, rooms[i+1][j], ConnectionPosition.TOP_LEFT));
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
				}
			}
		}
	}

	public RoomType[][] getRoomTypes() {
		return this.roomTypes;
	}

	public Room[][] getRooms() {
		return this.rooms;
	}
	
	public List<Connection> getConnections() {
		return connections;
	}
	
	@Override
	public String toString() {
		String string = " ";
		for (int i = 0; i < roomTypes.length; i++) {
			string += i+1;
		}
		string += "\n";
		for (int i = roomTypes[0].length-1; i >= 0; i--) {
			string += i;
			for (int j = 0; j < roomTypes.length; j++) {
				RoomType type = roomTypes[j][i];
				if (type != null)
					string += type;
				else
					string += ".";
			}
			string += "\n";
		}
//		for (RoomType[] row : roomTypes) {
//			for (RoomType type : row) {
//				if (type != null)
//					string += type;
//				else
//					string += ".";
//			}
//			string += "\n";
//		}
		return string;
	}
	
//	/**
//	 * Génère les salles du donjon à partir des paramètres donnés
//	 * @param random Aléatoire à utiliser (aléatoire avec graine)
//	 * @param screen "Ecran" du jeu sur lequel le donjon sera ajouté
//	 * @param baseRoom Salle à partir de laquelle les prochaines salle seront générées
//	 * @param maxRoomCount Nombre maximum de salles
//	 */
//	private void generate(Random random, GameplayScreen screen, Room baseRoom, int maxRoomCount, int minRoomCount) {
//		if (this.rooms.size() >= maxRoomCount)
//			return; // Annulation si nombre de salle maximum trop faible (plus petit ou égal à 1)
//
//		
//		// Nombre de passages max de la salle
//		int maxRooms;
//		if (baseRoom.isTall())
//			maxRooms = 4;
//		else
//			maxRooms = 2;
//		
//		
//		// Récupération des passages pas encore utilisés
//		List<ConnectionPosition> available =
//				new ArrayList<>(Arrays.asList(ConnectionPosition.values()));
//		available.removeIf(position -> baseRoom.getConnections().containsKey(position));
//
//		/**
//		 * Nombre de passage utilisé
//		 * Entre 1 et 4 (inclus) si le nombre de salle minimum à générer à été atteint
//		 * Sinon entre 2 et 4, pour au moins générer une salle afin d'avoir un donjon d'au moins [minRoomCount] salles
//		 */
//		int connectionCount = 1;
//		if (this.rooms.size() >= minRoomCount) {
//			connectionCount += random.nextInt(maxRooms);
//		} else {
//			connectionCount += random.nextInt(maxRooms-1)+1;
//		}
//		System.out.println("Level.generate() : maxRooms = "+maxRooms+"; connectionCount = "+connectionCount);
//
//		// Génération de [connectionCount] salles
//		for (int i = 0; i < connectionCount; i++) {
//			// Annulation si le nombre de passages disponible est trop petit
//			// ou que le nombre de salles générées est trop grand que le nombre maximum de salles du donjon)
//			if (available.size() <= 0 || this.rooms.size() >= maxRoomCount)
//				return;
//
//			Room.RoomSize size; // Taille de la salle
//			String file = "rooms/dungeonNormal/"; // Fichier des assets à utiliser
//
//			// Si la salle de base n'est pas une grande salle, et que random donne un booléen
//			if (!baseRoom.isTall() && random.nextBoolean()) {
//				size = Room.RoomSize.TALL;
//				file += "tall/tall1.tmx";
//			} else { // Sinon
//				size = Room.RoomSize.NORMAL;
//				file += "normal/normal1.tmx";
//			}
//			
//			// Création d'une nouvelle salle et ajout à la liste des salles du niveau
//			Room newRoom = new Room(screen, file, size);
//			newRoom.setRenderingColor(this.colors[0], this.colors[1],
//									  this.colors[2], this.colors[3]);
//			this.rooms.add(newRoom);
//
//			// Choix du passage auquel lier cette salle
//			int randomPosition = random.nextInt(available.size());
//			ConnectionPosition position = available.get(randomPosition);
//			available.remove(randomPosition);
//
//			// Création du passage et ajout à la liste des passages
//			Connection connection = new Connection(baseRoom, position, newRoom, position.getOpposite());
//			connections.add(connection);
//
//			// Récursion avec comme salle de base la salle que nous venons de générer
//			generate(random, screen, newRoom, maxRoomCount, minRoomCount);
//		}
//	}

	/**
	 * Ancienne méthode
	 * @return
	 */
//	private void generate(Random random, Room baseRoom, GameplayScreen screen) {
//		boolean isTallRoom = baseRoom.isTall();
//		int maxRooms;
//		if (isTallRoom)
//			maxRooms = 4;
//		else
//			maxRooms = 2;
//		if (chances > 0) {
//			List<ConnectionPosition> positions =Arrays.asList(ConnectionPosition.values());
//			connections.forEach((connection) -> {
//				if (connection.getRoomA() == baseRoom || connection.getRoomB() == baseRoom) {
//					positions.remove(connection.getConnectionPositionOfRoom(baseRoom));
//				}
//			});
//			int amountOfRooms = random.nextInt(maxRooms+Math.round(chances));
//			System.out.println(maxRooms+" ; "+amountOfRooms);
//			for (int i = 0; i < amountOfRooms; i++) {
//				Room room = null;
//				if (!isTallRoom && random.nextBoolean())
//					room = new Room(screen, "rooms/dungeonNormal/tall/tall1.tmx", Room.RoomSize.TALL, Color.BLACK, Color.BLACK, Color.GRAY, Color.GRAY);
//				else
//					room = new Room(screen, "rooms/dungeonNormal/normal/normal1.tmx", Room.RoomSize.NORMAL, Color.BLACK, Color.BLACK, Color.GRAY, Color.GRAY);
//				room.setRenderingColor(colors[0], colors[1], colors[2], colors[3]);
//				rooms.add(room);
//				int randomPosition = random.nextInt(positions.size());
//				ConnectionPosition position = positions.get(randomPosition);
//				positions.remove(randomPosition);
//				Connection connection = new Connection(baseRoom, position, room, position.getOpposite());
//				connections.add(connection);
//				chances -= random.nextFloat()/10;
//				System.out.println(chances);
//				generate(random, room, screen);
//			}
//		}
//	}
	
//	public List<Room> getRooms() {
//		return rooms;
//	}
}
