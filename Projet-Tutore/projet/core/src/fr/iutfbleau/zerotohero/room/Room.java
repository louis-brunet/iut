package fr.iutfbleau.zerotohero.room;

import java.util.*;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.MovingPlatform;
import fr.iutfbleau.zerotohero.actors.PhysicsActor;
import fr.iutfbleau.zerotohero.actors.Player;
import fr.iutfbleau.zerotohero.actors.enemies.EnemyActor;
import fr.iutfbleau.zerotohero.actors.enemies.EnemyFactory;
import fr.iutfbleau.zerotohero.actors.enemies.EnemySpawn;
import fr.iutfbleau.zerotohero.entities.Bomb;
import fr.iutfbleau.zerotohero.entities.Bomb.BombType;
import fr.iutfbleau.zerotohero.entities.Chest;
import fr.iutfbleau.zerotohero.entities.ChestType;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.entities.Crate;
import fr.iutfbleau.zerotohero.entities.Door;
import fr.iutfbleau.zerotohero.entities.Door.DoorType;
import fr.iutfbleau.zerotohero.entities.Interactable;
import fr.iutfbleau.zerotohero.entities.PickupableActor;
import fr.iutfbleau.zerotohero.entities.Pickupables;
import fr.iutfbleau.zerotohero.entities.shop.Product;
import fr.iutfbleau.zerotohero.entities.shop.ShopSpawner;
import fr.iutfbleau.zerotohero.entities.shop.ShopType;
import fr.iutfbleau.zerotohero.physics.AxisAlignedBoundingBox;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.physics.ConnectionBody;
import fr.iutfbleau.zerotohero.physics.World;
import fr.iutfbleau.zerotohero.registries.Item;
import fr.iutfbleau.zerotohero.registries.ItemProperties;
import fr.iutfbleau.zerotohero.registries.Weapon;
import fr.iutfbleau.zerotohero.registries.WeaponProperties;
import fr.iutfbleau.zerotohero.room.connections.Connection;
import fr.iutfbleau.zerotohero.room.connections.ConnectionPosition;
import fr.iutfbleau.zerotohero.screens.GameplayScreen;
import fr.iutfbleau.zerotohero.utils.Tuple3;
import fr.iutfbleau.zerotohero.utils.Tuple5;
import fr.iutfbleau.zerotohero.utils.ViewDirection;
import fr.iutfbleau.zerotohero.weapons.WeaponActor;

public class Room implements Disposable {

//    public static final String PLAYER_BODY_NAME = "player_body";
//    public static final String GROUND_BODY_NAME = "ground_body";
//    public static final String MOVING_PLATFORM_BODY_NAME = "moving_platform_body";
//    public static final String JUMP_THROUGH_PLATFORM_BODY_NAME = "jump_through_platform_body";

    private static final String SOLID_COLLISIONS_LAYER = "SolidCollisions";
    private static final String JUMP_THROUGH_COLLISIONS_LAYER = "JumpThroughCollisions";
    private static final String DAMAGE_COLLISIONS_LAYER = "DamageCollisions";
    private static final String SPAWN_POINTS_LAYER = "SpawnPoints";
    private static final String MOVING_PLATFORMS_LAYER = "MovingPlatforms";
    private static final String LIGHTS_LAYER = "Lights";
    private static final String CONNECTIONS_LAYER = "Connections";
    private static final String OBJECTS_LAYER = "Objects";

    private static final String PLAYER_SPAWN_POINT_NAME = "PlayerSpawn";
    private static final String ENEMY_SPAWN_POINT_NAME = "EnemySpawn";

    private static final String ORDER_ID_PROPERTY = "orderID";
    private static final String WIDTH_PROPERTY = "width";
    private static final String HEIGHT_PROPERTY = "height";
    private static final String IMAGE_PATH_PROPERTY = "imagePath";
    private static final String SPEED_PROPERTY = "speed";
    private static final String WAIT_TIME_PROPERTY = "waitTime";
    private static final String BOTH_WAYS_PROPERTY = "bothWays";
    
    private static final String LIGHT_COLOR_PROPERTY = "color";
    private static final String LIGHT_RADIUS_PROPERTY = "radius";
    
    private final String mapFile;
    private TiledMap tiledMap;
    private Coordinates playerSpawnPoint;
    private final List<TiledMapTileLayer> layersToRender = new ArrayList<TiledMapTileLayer>();
//    private final List<Coordinates> enemySpawnPointList = new ArrayList<Coordinates>();
    private final List<EnemySpawn> enemySpawnPoints = new LinkedList<>();
    private final List<Rectangle> solidCollisions = new ArrayList<Rectangle>();
    private final List<Rectangle> jumpThroughCollisions = new ArrayList<Rectangle>();
    private final List<Rectangle> damageCollisions = new ArrayList<Rectangle>();
    private final Set<Actor> newActors = new LinkedHashSet<Actor>();
    private final Set<Actor> actors = new LinkedHashSet<>();
    private Map<ConnectionPosition, Rectangle> connectionZones = new HashMap<ConnectionPosition, Rectangle>();
    private Map<ConnectionPosition, Connection> connections = new HashMap<ConnectionPosition, Connection>();
    private List<Door> doors = new ArrayList<Door>();
    private List<Chest> chests = new ArrayList<Chest>();
    private List<PhysicsActor> tileEntities = new ArrayList<PhysicsActor>();
    private World world;
    private RoomSize roomSize;
    private final List<Color> backgroundColors = new ArrayList<Color>(4);
    private List<Color> renderingColors = new ArrayList<Color>(4);
    private boolean renderWithColor;
//    private List<Actor> enemies = ArrayList<Actor>();
//    private final List<MovingPlatformPhysicsActor> movingPlatforms =
//        new ArrayList<MovingPlatformPhysicsActor>();
    private final List<Tuple3<Coordinates, Color, Float>> lights =
            new ArrayList<Tuple3<Coordinates, Color, Float>>();

    private boolean isVisited;
    private GameplayScreen screen;
    private final String mapIconPath;

    /**
     * Creates a Map from the .tmx file
     * @param file The map file
     */
    public Room(GameplayScreen screen, String file, String mapIconPath, RoomSize roomSize, Color color1, Color color2, Color color3, Color color4) {
        this.screen = screen;
    	this.mapFile = file;
        this.roomSize = roomSize;
        this.mapIconPath = mapIconPath;
        backgroundColors.add(color1);
        backgroundColors.add(color2);
        backgroundColors.add(color3);
        backgroundColors.add(color4);
        this.renderWithColor = false;
        this.isVisited = false;
        create();
    }

    public Room(GameplayScreen screen, String file, RoomSize roomSize) {
        this(screen, file, null, roomSize, Color.BLACK, Color.BLACK, Color.GRAY, Color.GRAY);
    }

	public void setRenderingColor(Color color1, Color color2, Color color3, Color color4) {
		renderingColors.add(color1);
        renderingColors.add(color2);
        renderingColors.add(color3);
        renderingColors.add(color4);
        this.renderWithColor = true;
	}
	
	public void resetRenderingColor() {
		this.renderingColors.clear();
		this.renderWithColor = false;
	}
	
	public List<Color> getRenderingColors() {
		return renderingColors;
	}
	
	public boolean renderWithColor() {
		return renderWithColor;
	}

    public enum RoomSize {
    	NORMAL, TALL;
    }

    /**
     * Creates all the components of the Map
     */
    private void create() {
        this.world = new World();
        tiledMap = new TmxMapLoader().load(mapFile);
        tiledMap.getLayers().getByType(TiledMapTileLayer.class).forEach((layer) -> {
            this.layersToRender.add(layer);
        });
        createSolidCollisionList();
        createJumpThroughCollisionList();
        createDamageCollisionList();
        createSpawnPoints();
        createMovingPlatforms();
        createLights();
        createDoors();
        createChests();
        createCrates();
        createShop();
        createConnectionsZones();
        createEnemies();

        this.world.update(0f); // triggers starting collisions
    }

    private void createConnectionsZones() {
        MapLayer connectionZones = tiledMap.getLayers().get(Room.CONNECTIONS_LAYER);
        if (connectionZones != null) {
            Coordinates center;
            for (MapObject connectionZone : connectionZones.getObjects()) {
                RectangleMapObject rectangleConnectionZone = (RectangleMapObject) connectionZone;
                Rectangle rectangle = rectangleConnectionZone.getRectangle();
                String name = (String) connectionZone.getProperties().get("connectionPosition");
                for (ConnectionPosition position : ConnectionPosition.values()) {
                	if (position.name().contains(name)) {
                		this.connectionZones.put(position, rectangle);
                		center = new Coordinates(
                				rectangle.x + rectangle.width / 2f,
                				rectangle.y + rectangle.height / 2f);
                		
                		this.world.addBody(new ConnectionBody(
                				Body.Type.CONNECTION,
                				false,
                				new AxisAlignedBoundingBox(rectangle),
                				center,
                				this,
                				position));
                		break;
                	}
                }
            }
        }
	}
    
    public void setConnection(ConnectionPosition pos, Connection connection) {
    	this.connections.put(pos, connection);
    	this.tiledMap.getLayers().getByType(TiledMapTileLayer.class).forEach((layer) -> {
    		if (layer.getName().contains(pos.name()))
    			this.layersToRender.remove(layer);
    	});
    }
    
    public Map<ConnectionPosition, Connection> getConnections() {
		return connections;
	}
    
    public Connection getConnectionAtPosition(ConnectionPosition pos) {
    	return this.connections.get(pos);
    }

	public Rectangle getConnectionZoneAtPosition(ConnectionPosition connectionPosition) {
		return this.connectionZones.get(connectionPosition);
	}
	
	public List<TiledMapTileLayer> getLayersToRender() {
		return layersToRender;
	}
    
    private void createDoors() {
        MapLayer doors = tiledMap.getLayers().get(Room.OBJECTS_LAYER);
        if (doors != null) {
            Coordinates center;
            for (MapObject doorObject : doors.getObjects()) {
            	if (doorObject.getProperties().get("type").toString().equals("Door")) {
	                RectangleMapObject rectangleDoor = (RectangleMapObject) doorObject;
	                Rectangle rectangle = rectangleDoor.getRectangle();
	                rectangle.setSize(ZeroToHero.getTileWidth(), ZeroToHero.getTileHeight()*3);
	                String[] doorTypeNames = ((String) doorObject.getProperties().get("doorTypes")).split(" "),
	                		imagePaths = ((String) doorObject.getProperties().get("images")).split(" ");
	                if (imagePaths.length >= doorTypeNames.length) {
		                Map<String, String[]> doorList = new HashMap<String, String[]>();
		                for (int i = 0; i < doorTypeNames.length; i++)
		                	doorList.put(doorTypeNames[i], imagePaths[i].split("&amp;"));
		                if (!doorList.isEmpty()) {
		                	Random random = this.screen.getSeededRandom();
		                	String doorTypeName = (String) doorList.keySet().toArray()[random.nextInt(doorList.size())];
                			String[] doorImagePaths = doorList.get(doorTypeName);
		                	DoorType doorType = DoorType.valueOf(doorTypeName);
		                    center = new Coordinates(
		                            rectangle.x + rectangle.width / 2f,
		                            rectangle.y + rectangle.height / 2f);
		
		                    Body body = new Body(
		                            Body.Type.TILE_ENTITY,
		                            false,
		                            new AxisAlignedBoundingBox(rectangle),
		                            center);
		                    
		                    this.world.addBody(body);
		                    
		                    Door door = new Door(doorImagePaths, body, doorType);
		                    this.doors.add(door);
		                    door.setRoom(this);
	                        this.actors.add(door);
	                        this.newActors.add(door);
		                }
	                }
            	}
            }
        }
    }
    
    private void createChests() {
        MapLayer objectsLayer = tiledMap.getLayers().get(Room.OBJECTS_LAYER);
        if (objectsLayer != null) {
            Coordinates center;
            for (MapObject chestObject : objectsLayer.getObjects()) {
                if (chestObject.getProperties().get("type").toString().equals("Chest")) {
                    RectangleMapObject rectangleChest = (RectangleMapObject) chestObject;
                    Rectangle rectangle = rectangleChest.getRectangle();

                    rectangle.setSize(ZeroToHero.getTileWidth() * 1.5f,
                                      ZeroToHero.getTileHeight() * 2f / 3f);
                    rectangle.x -= rectangle.width / 2f;
                    String[] chestTypeNames = ((String) chestObject.getProperties().get(
                            "chestTypes")).split(" "),
                            imagePaths = ((String) chestObject.getProperties().get("images")).split(" ");
                    if (imagePaths.length >= chestTypeNames.length) {
                        Map<String, String[]> chestTypeMap = new HashMap<String, String[]>();
                        for (int i = 0; i < chestTypeNames.length; i++)
                             chestTypeMap.put(chestTypeNames[i], imagePaths[i].split("&amp;"));
                        if (!chestTypeMap.isEmpty()) {
                            Random random = this.screen.getSeededRandom();
                            String chestTypeName = (String) chestTypeMap.keySet().toArray()[random.nextInt(chestTypeMap.size())];
                            String[] chestImagePaths = chestTypeMap.get(chestTypeName);
                            ChestType chestType = ChestType.valueOf(chestTypeName);
                            center = new Coordinates(
                                    rectangle.x + rectangle.width / 2f,
                                    rectangle.y + rectangle.height / 2f);

                            Body body = new Body(
                                    Body.Type.TILE_ENTITY,
                                    false,
                                    new AxisAlignedBoundingBox(rectangle),
                                    center);

                            this.world.addBody(body);

                            Chest chest = new Chest(chestImagePaths[0], chestImagePaths[1], body, chestType);
                            this.chests.add(chest);
                            chest.setRoom(this);
                            this.actors.add(chest);
                            this.newActors.add(chest);
                        }
                    }
                }
            }
        }
    }
    
    private void createCrates() {
        MapLayer objectsLayer = tiledMap.getLayers().get(Room.OBJECTS_LAYER);
        if (objectsLayer != null) {
            Coordinates center;
            for (MapObject crateObject : objectsLayer.getObjects()) {
                if (crateObject.getProperties().get("type").toString().equals("Crate")) {
                    RectangleMapObject rectangleCrate = (RectangleMapObject) crateObject;
                    Rectangle rectangle = rectangleCrate.getRectangle();

                    rectangle.setSize(ZeroToHero.getTileWidth() * 1.5f,
                                      ZeroToHero.getTileHeight() * 1.5f);
                    rectangle.x -= rectangle.width / 2f;
                    String[] crateTypeNames = ((String) crateObject.getProperties().get(
                            "objectTypes")).split(" "),
                            imagePaths = ((String) crateObject.getProperties().get("images")).split(" ");
                    if (imagePaths.length >= crateTypeNames.length) {
                        Map<String, String[]> crateTypeMap = new HashMap<String, String[]>();
                        for (int i = 0; i < crateTypeNames.length; i++)
                             crateTypeMap.put(crateTypeNames[i], imagePaths[i].split("&amp;"));
                        if (!crateTypeMap.isEmpty()) {
                            String[] crateImagePaths = crateTypeMap.get(crateTypeNames[0]);
                            center = new Coordinates(
                                    rectangle.x + rectangle.width / 2f,
                                    rectangle.y + rectangle.height / 2f);

                            Body body = new Body(
                                    Body.Type.TILE_ENTITY,
                                    false,
                                    new AxisAlignedBoundingBox(rectangle),
                                    center);

                            this.world.addBody(body);

                            Crate crate = new Crate(crateImagePaths[0], crateImagePaths[1], body);
                            this.tileEntities.add(crate);
                            crate.setRoom(this);
                            this.actors.add(crate);
                            this.newActors.add(crate);
                        }
                    }
                }
            }
        }
    }

    private void createShop() {
        MapLayer objectsLayer = tiledMap.getLayers().get(Room.OBJECTS_LAYER);
        if (objectsLayer != null) {
            for (MapObject mainShopObject : objectsLayer.getObjects()) {
                if (mainShopObject.getProperties().get("type").toString().equals("Shop")) {
                    ShopType shopType = ShopType.valueOf(mainShopObject.getProperties().get("shopType").toString());
                    Objects.requireNonNull(shopType);
                    RectangleMapObject rectangleMapObject = (RectangleMapObject) mainShopObject;
                    Coordinates centerBottom = new Coordinates(
                            rectangleMapObject.getRectangle().x,
                            rectangleMapObject.getRectangle().y
                    );

                    Random rand = this.screen.getSeededRandom();

                    List<Coordinates> primaryProductsCenterBottom = this.getProductPositions(objectsLayer.getObjects(), "PRIMARY");
                    List<Coordinates> secondaryProductsCenterBottom = this.getProductPositions(objectsLayer.getObjects(), "SECONDARY");

                    System.out.println("Room.createShop() "+primaryProductsCenterBottom.size()+" big positions and "+secondaryProductsCenterBottom.size()+" small positions.");

                    List<Product> primaryProducts = ShopSpawner.createPrimaryProducts(
                            shopType,
                            primaryProductsCenterBottom,
                            rand);
                    List<Product> secondaryProducts = ShopSpawner.createSecondaryProducts(
                            secondaryProductsCenterBottom, rand);
                    Actor mainShop = ShopSpawner.createShopActor(shopType, centerBottom );

                    System.out.println("Room.createShop() - "+shopType.name()+" shop with "+primaryProducts.size()+" big products and "+secondaryProducts.size()+" small products.");

                    this.addActor(mainShop);
                    for (Product primaryProduct : primaryProducts) {
                        this.world.addBody(primaryProduct.getBody());
                        this.addActor(primaryProduct);
                        primaryProduct.setRoom(this);
                    }
                    for (Product secondaryProduct : secondaryProducts) {
                        this.world.addBody(secondaryProduct.getBody());
                        this.addActor(secondaryProduct);
                        secondaryProduct.setRoom(this);
                    }
                }
            }
        }
    }

    private List<Coordinates> getProductPositions(MapObjects objects, String productType) {
        Objects.requireNonNull(objects);
        Objects.requireNonNull(productType);

        List<Coordinates> positions = new LinkedList<>();
        for (MapObject mapObject: objects) {
            String objectType = mapObject.getProperties().get("type").toString();
            if (objectType.equals("ShopItem") && mapObject.getProperties().get("productType").toString().equals(productType)) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
                positions.add(new Coordinates(rectangle.x, rectangle.y));
            }
        }
        return positions;
    }

	private void createEnemies() {
        for(EnemySpawn enemySpawn : this.enemySpawnPoints) {
        	EnemyActor actor = EnemyFactory.randomEnemy(this, enemySpawn, screen.getSeededRandom());
            if (actor != null)
            	this.addActor(actor);
        }
    }

    /**
     * @return Returns the Tile Map of the Map
     */
    public TiledMap getTiledMap() {
        return this.tiledMap;
    }

    /**
     * Creates the Solid Collisions of the Map
     */
    private void createSolidCollisionList() {
        MapLayer solidCollisions = tiledMap.getLayers().get(Room.SOLID_COLLISIONS_LAYER);
        if (solidCollisions != null) {
            Coordinates center;
            for (MapObject collisionBox : solidCollisions.getObjects()) {
                RectangleMapObject rectangleCollisionBox = (RectangleMapObject) collisionBox;
                Rectangle rectangle = rectangleCollisionBox.getRectangle();
                this.solidCollisions.add(rectangle);
                center = new Coordinates(
                        rectangle.x + rectangle.width / 2f,
                        rectangle.y + rectangle.height / 2f);

                this.world.addBody(new Body(
                        Body.Type.GROUND,
                        false,
                        new AxisAlignedBoundingBox(rectangle),
                        center));
            }
        }
    }

    /**
     * Creates the Damage Collisions of the Map
     */
    private void createDamageCollisionList() {
        MapLayer damageCollisions = tiledMap.getLayers().get(Room.DAMAGE_COLLISIONS_LAYER);
        if (damageCollisions != null) {
            Coordinates center;
            for (MapObject collisionBox : damageCollisions.getObjects()) {
                RectangleMapObject rectangleCollisionBox = (RectangleMapObject) collisionBox;
                Rectangle rectangle = rectangleCollisionBox.getRectangle();
                this.damageCollisions.add(rectangle);
                center = new Coordinates(
                        rectangle.x + rectangle.width / 2f,
                        rectangle.y + rectangle.height / 2f);

                this.world.addBody(new Body(
                        Body.Type.DAMAGE,
                        false,
                        new AxisAlignedBoundingBox(rectangle),
                        center));
            }
        }
    }
    
    private void createLights() {
        MapLayer lightsLayer = tiledMap.getLayers().get(Room.LIGHTS_LAYER);
        if (lightsLayer != null) {
            for (MapObject light : lightsLayer.getObjects()) {
                Rectangle lightRectangle = ((RectangleMapObject) light).getRectangle();
                MapProperties properties = light.getProperties();
                
                Color color = (Color) properties.get(Room.LIGHT_COLOR_PROPERTY);
                color = color.mul(1f, 1f, 1f, 0.75f);
                float radius = (float) properties.get(Room.LIGHT_RADIUS_PROPERTY);
                
                this.lights.add(new Tuple3<Coordinates, Color, Float>(new Coordinates(lightRectangle.getX(), lightRectangle.getY()), color, (float) (int) radius));
            }
        }
    }

    /**
     * @return Returns the list of Solid Collisions of the Map
     */
    public List<Rectangle> getSolidCollisionList() {
        return this.solidCollisions;
    }

    /**
     * Creates the Jump-Through Collisions of the Map
     */
    private void createJumpThroughCollisionList() {
        MapLayer jumpThroughCollisions = tiledMap.getLayers().get(Room.JUMP_THROUGH_COLLISIONS_LAYER);
        if (jumpThroughCollisions != null) {
            Coordinates center;
            for (MapObject platformCollisionBox : jumpThroughCollisions.getObjects()) {
                RectangleMapObject platformRectangleCollisionBox = (RectangleMapObject) platformCollisionBox;
                Rectangle rectangle = platformRectangleCollisionBox.getRectangle();
                this.jumpThroughCollisions.add(rectangle);

                center = new Coordinates(
                        rectangle.x + rectangle.width / 2f,
                        rectangle.y + rectangle.height / 2f);

                this.world.addBody(new Body(
                        Body.Type.JUMP_THROUGH,
                        false,
                        new AxisAlignedBoundingBox(rectangle),
                        center));
            }
        }
    }

    /**
     * @return Returns the list of Jump-Through Collisions of the Map
     */
    public List<Rectangle> getJumpThroughCollisionList() {
        return this.jumpThroughCollisions;
    }

    /**
     * Creates the Spawn Points of the Map
     */
    private void createSpawnPoints() {
        this.playerSpawnPoint = new Coordinates();
        MapLayer spawnPoints = tiledMap.getLayers().get(Room.SPAWN_POINTS_LAYER);
        if (spawnPoints != null) {
            for (MapObject point : spawnPoints.getObjects()) {
                Rectangle pointRectangle = ((RectangleMapObject) point).getRectangle();
                if (point.getProperties().get("type").toString().equals(Room.PLAYER_SPAWN_POINT_NAME)) {
                    this.playerSpawnPoint.setX(pointRectangle.getX());
                    this.playerSpawnPoint.setY(pointRectangle.getY());
                } else if (point.getProperties().get("type").toString().equals(Room.ENEMY_SPAWN_POINT_NAME)) {
                    Object enemiesProperty = point.getProperties().get("enemies");
                    String enemiesString = "";
                    if (enemiesProperty != null) {
                    	enemiesString = (String) enemiesProperty;
                    	String[] allEnemies = enemiesString.split(";");
                    	Map<String, Integer> enemiesWithWeight = new HashMap<String, Integer>();
                    	for (String enemyWithWeight : allEnemies) {
                    		System.out.println(enemyWithWeight);
                    		String[] values = enemyWithWeight.split("%");
                    		int value = 0;
                    		if (values.length == 1) {
                    			value = 1;
                    		} else {
	                    		try {
	                    			value = Integer.parseInt(values[1]);
	                    		} catch (Exception e) {
	                    			System.err.println("Error in "+mapFile+" : Some enemy spawnpoints are using a wrong format !");
	                    		}
                    		}
                    		enemiesWithWeight.put(values[0], value);
                    	}
                    	this.enemySpawnPoints.add(new EnemySpawn(new Coordinates(pointRectangle.getX(), pointRectangle.getY()), enemiesWithWeight));
                    }
//                    this.enemySpawnPointList.add(new Coordinates(pointRectangle.getX(), pointRectangle.getY()));
                }
            }
        }
    }

    /**
     * Creates the Moving Platforms of the Map
     */
    private void createMovingPlatforms() {
        MapLayer movingPlaftormPoints = tiledMap.getLayers().get(Room.MOVING_PLATFORMS_LAYER);
        List<MapObject> listOfPlatforms = new ArrayList<MapObject>();
        HashMap<String, List<Coordinates>> platformsWithCoordinates = new HashMap<String, List<Coordinates>>();
        HashMap<String, Tuple5<Coordinates, String, Float, Float, Boolean>> platformsWithProperties = new HashMap<String, Tuple5<Coordinates, String, Float, Float, Boolean>>();
        if (movingPlaftormPoints != null) {
            for (MapObject point : movingPlaftormPoints.getObjects()) {
                if (point.getProperties().containsKey(Room.ORDER_ID_PROPERTY)) {
                    listOfPlatforms.add(point);
                }
            }
            int index = 0;
            while (!listOfPlatforms.isEmpty()) {
                MapObject point = listOfPlatforms.get(index);
                MapProperties properties = point.getProperties();
                if ((int) point.getProperties().get(Room.ORDER_ID_PROPERTY) == 0) {
                    Rectangle pointRectangle = ((RectangleMapObject) point).getRectangle();
                    List<Coordinates> coordinates = new ArrayList<Coordinates>();
                    coordinates.add(new Coordinates(pointRectangle.getX(), pointRectangle.getY()));
                    platformsWithCoordinates.put(point.getName(), coordinates);
                    Coordinates size = new Coordinates((float) properties.get(Room.WIDTH_PROPERTY), (float) properties.get(Room.HEIGHT_PROPERTY));
                    String imagePath = (String) properties.get(Room.IMAGE_PATH_PROPERTY);
                    Float speed = (float) properties.get(Room.SPEED_PROPERTY),
                            waitTime = (float) properties.get(Room.WAIT_TIME_PROPERTY);
                    Boolean bothWays = (boolean) properties.get(Room.BOTH_WAYS_PROPERTY);
                    platformsWithProperties.put(point.getName(), new Tuple5<Coordinates, String, Float, Float, Boolean>(size, imagePath, speed, waitTime, bothWays));
                    listOfPlatforms.remove(index);
                } else {
                    int orderID = (int) properties.get(Room.ORDER_ID_PROPERTY);
                    List<Coordinates> coordinates = platformsWithCoordinates.get(point.getName());
                    if (coordinates.size() == orderID) {
                        Rectangle pointRectangle = ((RectangleMapObject) point).getRectangle();
                        coordinates.add(new Coordinates(pointRectangle.getX(), pointRectangle.getY()));
                        platformsWithCoordinates.put(point.getName(), coordinates);
                        listOfPlatforms.remove(index);
                    }
                }
                index++;
                if (index > listOfPlatforms.size()-1)
                    index = 0;
            }
            platformsWithProperties.forEach((name, tuple) -> {
                float halfX = tuple.a.getX() / 2f;
                float halfY = tuple.a.getY() / 2f;
                float x = platformsWithCoordinates.get(name).get(0).getX()-halfX;
                float y = platformsWithCoordinates.get(name).get(0).getY();
                Body body = new Body(
                        Body.Type.TILE_ENTITY,
                        true,
                        new AxisAlignedBoundingBox(new Coordinates(x + halfX, y + halfY),
                                halfX, halfY),
                        new Coordinates(x + halfX, y+halfY));
                this.world.addBody(body);
                MovingPlatform
                        platform = new MovingPlatform(tuple.b, body, tuple.a.getX(), tuple.a.getY(), tuple.c, tuple.d);
//                MovingPlatform platform = new MovingPlatform(name, platformsWithCoordinates.get(name).get(0).getX()-tuple.a.getX()/2, platformsWithCoordinates.get(name).get(0).getY(), (int) tuple.a.getX(), (int) tuple.a.getY(), tuple.b, tuple.c, tuple.d, body);
                Coordinates[] coordinates = new Coordinates[platformsWithCoordinates.get(name).size()];
                for (int index1 = 0; index1 < platformsWithCoordinates.get(name).size(); index1++) {
                    Coordinates coordinatesToAdd = platformsWithCoordinates.get(name).get(index1);
                    coordinatesToAdd.moveX(-tuple.a.getX()/2);
                    coordinates[index1] = coordinatesToAdd;
                }
                platform.addAllPoints(tuple.e, coordinates);
                platform.setRoom(this);

                actors.add(platform);
                newActors.add(platform);
//                movingPlatforms.add(platform);
            });
        }
    }

    /**
     * @return Returns the Player Spawn Point of the Map
     */
    public Coordinates getPlayerSpawnPoint() {
    	if (this.playerSpawnPoint == null)
    		return new Coordinates();
        return this.playerSpawnPoint;
    }

    /**
     * @return Returns the list of Enemy Spawn Points of the Map
     */
    public List<Coordinates> getEnemySpawnPointList() {
        return this.enemySpawnPoints.stream()
                                    .map(EnemySpawn::getPosition)
                                    .collect(Collectors.toList());
    }
    
    public List<Tuple3<Coordinates, Color, Float>> getLights() {
		return lights;
	}

    public Set<Actor> getAllActors() {
        return actors;
    }

    public Set<Actor> getNewActors() {
        return newActors;
    }

    public World getWorld() {
        return this.world;
    }

	public float getWidth(boolean inPixels) {
		int width = (int) getTiledMap().getProperties().get("width");
		if (inPixels)
			width *= (int) getTiledMap().getProperties().get("tilewidth");
		return width;
	}

	public float getHeight(boolean inPixels) {
		int height = (int) getTiledMap().getProperties().get("height");
		if (inPixels)
			height *= (int) getTiledMap().getProperties().get("tileheight");
		return height;
	}
	
	public void removeActor(Actor actor, Body body) {
//		float x = body.getXLeft(),
//				y = body.getYBottom();
        this.actors.remove(actor);
		this.newActors.remove(actor);
		if (body != null)
		    this.world.markForRemoval(body);
//		ZeroToHero.getPlayer().getItems().forEach((item) -> {
//			item.getProperties().getAction().onEnemyDied(x, y);
//		});
	}
	
	public List<Color> getBackgroundColors() {
		return backgroundColors;
	}
	
	public Map<ConnectionPosition, Rectangle> getConnectionZones() {
		return connectionZones;
	}

	public void addActor(Actor actor) {
        actors.add(actor);
//        if (! (actor instanceof TestPlayer))
		    newActors.add(actor);
	}

	public void interactWithClosestInteractable() {
		Interactable closestActor = this.getClosestInteractable();
		
		if (closestActor != null)
			closestActor.interact();
	}
	
	public Interactable getClosestInteractable() {
		Map<Interactable, Float> distances = new HashMap<Interactable, Float>();
		Coordinates playerCenter = ZeroToHero.getPlayer().getBody().getPosition();
		Coordinates actorCenter;
		float distance;

		for(Actor actor : this.actors) {
		    if (actor instanceof Interactable) {
                actorCenter = new Coordinates(actor.getX()+actor.getWidth() / 2f,
                                              actor.getY()+actor.getHeight() / 2f);
                distance = playerCenter.getDistance(actorCenter);
                if (distance < Player.INTERACT_RANGE * ZeroToHero.getTileWidth() && ((Interactable) actor).canInteract()) {
                    distances.put((Interactable) actor, distance);
                }
            }
        }

		distance = -1f;
		Interactable closestActor = null;

		for(Map.Entry<Interactable, Float> entry: distances.entrySet()) {
            if (distance == -1) {
                distance = entry.getValue();
                closestActor = entry.getKey();
            } else if (entry.getValue() < distance) {
                distance = entry.getValue();
                closestActor = entry.getKey();
            }
        }

		return closestActor;
	}

	public List<Actor> getCloseActors(Actor baseActor, float range) {
		Coordinates pos = new Coordinates(baseActor.getX()+baseActor.getWidth() / 2f,
												  baseActor.getY()+baseActor.getHeight() / 2f);
		List<Actor> actors = new LinkedList<Actor>();
		this.actors.forEach((actor) -> {
		    if (actor.equals(baseActor)) return;

			Coordinates actorCenter = new Coordinates(actor.getX()+actor.getWidth() / 2f,
													  actor.getY()+actor.getHeight() / 2f);
			if (actorCenter.getDistance(pos) < range * ZeroToHero.getTileWidth())
				actors.add(actor);
//			else
//                System.out.println("Room.getCloseActors(): "+actor.getName()+" was not in range.");
		});
		
		return actors;
	}


    @Override
    public void dispose() {
        this.tiledMap.dispose();
    }
    
	public void placeBomb(BombType type, int power, float range, float cooldown, Coordinates initialSpeed) {
		float x = ZeroToHero.getPlayer().getX(),
				y = ZeroToHero.getPlayer().getY();
		if (ZeroToHero.getPlayer().getFacing() == ViewDirection.RIGHT)
			x -= 24;
		Rectangle rectangle = new Rectangle(x, y, 48, 48);
        Coordinates center = new Coordinates(
                rectangle.x + rectangle.width / 2f,
                rectangle.y + rectangle.height / 2f);
		Body bombBody = new Body(
                Body.Type.NO_CLIP_TILE_ENTITY,
                true,
                new AxisAlignedBoundingBox(rectangle),
                center);
		this.world.addBody(bombBody);
        
		Bomb bomb = new Bomb("tileEntities/normal/Bomb.png", bombBody, type, power,
                              range, cooldown, initialSpeed);
        bomb.setRoom(this);

        this.actors.add(bomb);
        this.newActors.add(bomb);
	}

    public void spawnGold(int amount, float x, float y) {
        PickupableActor gold = Pickupables.gold(amount, x, y);
        this.world.addBody(gold.getBody());
        gold.setRoom(this);
        this.addActor(gold);
//        float radius = 48 / 2f;
//        BoundingShape shape = new CircleBounds(new Coordinates(x, y),
//                                               radius);
//        Body body = new Body(Body.Type.NO_CLIP_TILE_ENTITY, false, shape,
//                             new Coordinates(x, y), new Coordinates());
//        this.world.addBody(body);
//
//        Gold gold = new Gold("gui/moneyIcon.png", body, amount);
//        gold.setRoom(this);
//        this.addActor(gold);
    }

    public void spawnHeart(int amount, float x, float y) {
        PickupableActor hearts = Pickupables.hearts(amount, x, y);
        this.world.addBody(hearts.getBody());
        hearts.setRoom(this);
        this.addActor(hearts);
//        float width = 48f;
//        float height = 48f;
//        BoundingShape shape = new AxisAlignedBoundingBox(new Coordinates(x, y),
//                                                         width / 2f,
//                                                         height / 2f);
//        Body body = new Body(Body.Type.NO_CLIP_TILE_ENTITY, false, shape,
//                             new Coordinates(x, y), new Coordinates());
//        this.world.addBody(body);
//
//        Heart heart = new Heart("gui/heartIcon.png", body, amount);
//        heart.setRoom(this);
//        this.addActor(heart);
    }

    public void spawnKey(float x, float y) {
        PickupableActor key = Pickupables.key(x, y);
        this.world.addBody(key.getBody());
        key.setRoom(this);
        this.addActor(key);
    }

    public void spawnBomb(float x, float y) {
        PickupableActor bomb = Pickupables.bomb(x, y);
        this.world.addBody(bomb.getBody());
        bomb.setRoom(this);
        this.addActor(bomb);
    }

    public void spawnWeapon(Weapon weapon, float x, float y) {
    	spawnWeapon(weapon.getId(), x, y);
    }

	public void spawnWeapon(WeaponActor weapon, float x, float y) {
		spawnWeapon(weapon.getType(), x, y);
	}
	
	public void spawnWeapon(String id, float x, float y) {
    	spawnWeapon(ZeroToHero.weaponRegistry.getProperties(id), x, y);
	}
	
	private void spawnWeapon(WeaponProperties properties, float x, float y) {
        PickupableActor pickupable = Pickupables.weapon(properties, x, y);
        this.world.addBody(pickupable.getBody());
        pickupable.setRoom(this);
        this.addActor(pickupable);
	}

    public void spawnItem(Item item, float x, float y) {
    	spawnItem(item.getId(), x, y);
    }
	
	public void spawnItem(String id, float x, float y) {
		spawnItem(ZeroToHero.itemRegistry.getProperties(id), x, y);
	}
	
	private void spawnItem(ItemProperties properties, float x, float y) {
        PickupableActor pickupable = Pickupables.item(properties, x, y);
        this.world.addBody(pickupable.getBody());
        pickupable.setRoom(this);
        this.addActor(pickupable);
	}

	public List<Rectangle> getDamageCollisionList() {
		return damageCollisions;
	}
	
	public boolean isTall() {
		return roomSize == RoomSize.TALL;
	}

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public String getMapIconPath() {
        return mapIconPath;
    }
}
