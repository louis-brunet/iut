package fr.iutfbleau.zerotohero;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import fr.iutfbleau.zerotohero.actors.Player;
import fr.iutfbleau.zerotohero.actors.enemies.ICharacterAI;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.entities.shop.Currencies;
import fr.iutfbleau.zerotohero.game.Assets;
import fr.iutfbleau.zerotohero.game.AudioPlayer;
import fr.iutfbleau.zerotohero.game.GameSettings;
import fr.iutfbleau.zerotohero.items.IItemAction;
import fr.iutfbleau.zerotohero.registries.Enemies;
import fr.iutfbleau.zerotohero.registries.Enemy;
import fr.iutfbleau.zerotohero.registries.EnemyProperties;
import fr.iutfbleau.zerotohero.registries.Item;
import fr.iutfbleau.zerotohero.registries.ItemProperties;
import fr.iutfbleau.zerotohero.registries.Items;
import fr.iutfbleau.zerotohero.registries.Registry;
import fr.iutfbleau.zerotohero.registries.Weapon;
import fr.iutfbleau.zerotohero.registries.WeaponProperties;
import fr.iutfbleau.zerotohero.registries.Weapons;
import fr.iutfbleau.zerotohero.room.Debugger;
import fr.iutfbleau.zerotohero.screens.MainMenuScreen;
import fr.iutfbleau.zerotohero.utils.InputMapper;
import fr.iutfbleau.zerotohero.weapons.SimpleChargeableWeapon;
import fr.iutfbleau.zerotohero.weapons.SimpleWeapon;
import fr.iutfbleau.zerotohero.weapons.WeaponActor;

public class ZeroToHero extends Game {
    private static final int TILE_WIDTH = 48;
    private static final int TILE_HEIGHT = 48;

    private static Screen currentScreen;
    private static Player player;
    private static Assets assetManager;
	private static GameSettings settings;
	private static AudioPlayer audioPlayer;

	public static Registry<Item, ItemProperties> itemRegistry;
	public static Registry<Weapon, WeaponProperties> weaponRegistry;
	public static Registry<Enemy, EnemyProperties> enemyRegistry;

    private static InputMapper inputMapper;

    @Override
    public void create() {
    	ZeroToHero.assetManager = new Assets();
    	ZeroToHero.settings = new GameSettings();
    	ZeroToHero.inputMapper = new InputMapper(ZeroToHero.settings);
    	ZeroToHero.audioPlayer = new AudioPlayer(ZeroToHero.settings, ZeroToHero.assetManager);
        
        this.initJsons();

		this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        ZeroToHero.currentScreen = screen;
    }

    @Override
    public void render() {
        float delta = Math.min(1f / 20f, Gdx.graphics.getDeltaTime());
        if (this.screen != null) screen.render(delta);
    }

    private void initJsons() {
    	JsonReader reader = new JsonReader();

    	this.initItemsJson(reader);
    	this.initWeaponsJson(reader);
    	this.initEnemiesJson(reader);
	}

	@SuppressWarnings("unchecked")
	private void initItemsJson(JsonReader reader) {
		itemRegistry = new Registry<Item, ItemProperties>(new Items());
    	FileHandle file = Gdx.files.internal("items.json");
		JsonValue items = reader.parse(file);
		for (JsonValue item: items) {
			System.out.println("init item "+item.name());
			JsonValue[] allValues = new JsonValue[] {
					item.get("Name"),
					item.get("Desc"),
					item.get("Image"),
					item.get("Width"),
					item.get("Height"),
					item.get("Size"),
					item.get("AutoPickup"),
					item.get("ActionClass"),
					item.get("Currency"),
					item.get("Price")};
			Map<String, String> stringValues = new HashMap<String, String>();
			Map<String, Float> floatValues = new HashMap<String, Float>();
			Map<String, Class<? extends IItemAction>> classes = new HashMap<String, Class<? extends IItemAction>>();
			Boolean[] autoPickup = new Boolean[] {false};
			boolean[] canRegister = {true};
			for (int i = 0; i < allValues.length; i++) {
				JsonValue value = allValues[i];
				if (value == null || value.isNull()) {
					canRegister[0] = false;
					stringValues.put(allValues[i].name(), "Missing value : "+allValues[i].name());
				} else if (!value.isArray()) {
					if (value.isBoolean()) {
						autoPickup[0] = value.asBoolean();
					} else if (value.isNumber()) {
						floatValues.put(value.name, value.asFloat());
					} else {
						if (value.name.contains("ActionClass")) {
							try {
								Class<?> clazz = Class.forName("fr.iutfbleau.zerotohero.items."+value.asString());
								if (Arrays.asList(clazz.getInterfaces()).contains(IItemAction.class)) {
									classes.put(value.name, (Class<? extends IItemAction>) clazz);
								}
							} catch (Exception e) {
								canRegister[0] = false;
								System.err.println("Error : Class \"fr.iutfbleau.zerotohero.items."+value.asString()+"\" does not exist");
							}
						} else {
							stringValues.put(value.name, value.asString());
						}
					}
				} else {
					String[] childValues = allValues[i].asStringArray();
					for (int j = 0; j < childValues.length; j++)
						 stringValues.put(allValues[i].name()+j, childValues[j]);
				}
			}
			itemRegistry.register( item.name(), new ItemProperties(
					stringValues.get("Name"),
					item.name(),
					stringValues.get("Desc"),
					stringValues.get("Image"),
					floatValues.get("Width"),
					floatValues.get("Height"),
					floatValues.get("Size"),
					autoPickup[0],
					classes.get("ActionClass"),
					Currencies.get(stringValues.get("Currency")),
					floatValues.get("Price")));
		}
	}

	@SuppressWarnings("unchecked")
	private void initWeaponsJson(JsonReader reader) {
		weaponRegistry = new Registry<Weapon, WeaponProperties>(new Weapons());
		FileHandle file = Gdx.files.internal("weapons.json");
		JsonValue weapons = reader.parse(file);
		for (JsonValue weapon: weapons) {
			Map<String, JsonValue> allValues = new HashMap<String, JsonValue> ();
			allValues.put("Name",weapon.get("Name"));
			allValues.put("Desc",weapon.get("Desc"));
			allValues.put("DamagesMin",weapon.get("DamagesMin"));
			allValues.put("DamagesMax",weapon.get("DamagesMax"));
			allValues.put("ProjectileType",weapon.get("ProjectileType"));
			allValues.put("ProjectileImagePath",weapon.get("ProjectileImagePath"));
			allValues.put("ProjectileImageWidth",weapon.get("ProjectileImageWidth"));
			allValues.put("ProjectileImageHeight",weapon.get("ProjectileImageHeight"));
			allValues.put("ProjectileSizeMin",weapon.get("ProjectileSizeMin"));
			allValues.put("ProjectileSizeMax",weapon.get("ProjectileSizeMax"));
			allValues.put("ProjectileSpeedMin",weapon.get("ProjectileSpeedMin"));
			allValues.put("ProjectileSpeedMax",weapon.get("ProjectileSpeedMax"));
			allValues.put("Cooldown",weapon.get("Cooldown"));
			allValues.put("ChargeDuration",weapon.get("ChargeDuration"));
			allValues.put("Rarity",weapon.get("Rarity"));
			allValues.put("Width",weapon.get("Width"));
			allValues.put("Height",weapon.get("Height"));
			allValues.put("Size",weapon.get("Size"));
			allValues.put("IsChargeable",weapon.get("IsChargeable"));
			allValues.put("Image",weapon.get("Image"));
			allValues.put("ImageWidth",weapon.get("ImageWidth"));
			allValues.put("ImageHeight",weapon.get("ImageHeight"));
			allValues.put("ChargingImage",weapon.get("ChargingImage"));
			allValues.put("ChargingImageWidth",weapon.get("ChargingImageWidth"));
			allValues.put("ChargingImageHeight",weapon.get("ChargingImageHeight"));
			allValues.put("CustomBehavior",weapon.get("CustomBehavior"));
			allValues.put("Currency", weapon.get("Currency"));
			allValues.put("Price", weapon.get("Price"));
			Map<String, String> stringValues = new HashMap<String, String>();
			Map<String, Float> floatValues = new HashMap<String, Float>();
			Map<String, Boolean> boolValues = new HashMap<String, Boolean>();
			Map<String, Class<? extends WeaponActor>> classes = new HashMap<String, Class<? extends WeaponActor>>();
			boolean[] canRegister = {true};
			allValues.forEach((key, value) -> {
				if (value == null || value.isNull()) {
					if (key.contentEquals("CustomBehavior")) {
						if (weapon.get("IsChargeable").asBoolean())
							classes.put(key, SimpleChargeableWeapon.class);
						else
							classes.put(key, SimpleWeapon.class);
					} else {
						canRegister[0] = false;
						System.err.println("Error : Missing value \""+key+"\"");
					}
				} else if (!value.isArray()) {
					if (value.isNumber())
						floatValues.put(value.name, value.asFloat());
					else if (value.isBoolean())
						boolValues.put(value.name, value.asBoolean());
					else {
						if (value.name.contentEquals("CustomBehavior")) {
							try {
								Class<?> clazz = Class.forName("fr.iutfbleau.zerotohero.weapons."+value.asString());
								if (Arrays.asList(clazz.getInterfaces()).contains(WeaponActor.class)) {
									classes.put(value.name, (Class<? extends WeaponActor>) clazz);
								}
							} catch (Exception e) {
								if (weapon.get("IsChargeable").asBoolean())
									classes.put(key, SimpleChargeableWeapon.class);
								else
									classes.put(key, SimpleWeapon.class);
								System.err.println("Error : Class \"fr.iutfbleau.zerotohero.actors.enemies."+value.asString()+"\" does not exist");
							}
						} else {
							stringValues.put(value.name, value.asString());
						}
					}
				}
				// TODO Use later for more customization
					/* else {
						String[] childValues = allValues[i].asStringArray();
						for (int j = 0; j < childValues.length; j++)
							stringValues.put(allValues[i].name()+j, childValues[j]);
					} */
			});
			if (canRegister[0])
				weaponRegistry.register(weapon.name(), new WeaponProperties(
						stringValues.get("Name"),
						weapon.name(),
						stringValues.get("Desc"),
						stringValues.get("Image"),
						new Coordinates(floatValues.get("ImageWidth"), floatValues.get("ImageHeight")),
						stringValues.get("ChargingImage"),
						new Coordinates(floatValues.get("ChargingImageWidth"), floatValues.get("ChargingImageHeight")),
						floatValues.get("DamagesMin"),
						floatValues.get("DamagesMax"),
						stringValues.get("ProjectileType"),
						stringValues.get("ProjectileImagePath"),
						new Coordinates(floatValues.get("ProjectileImageWidth"), floatValues.get("ProjectileImageHeight")),
						floatValues.get("ProjectileSizeMin"),
						floatValues.get("ProjectileSizeMax"),
						floatValues.get("ProjectileSpeedMin"),
						floatValues.get("ProjectileSpeedMax"),
						floatValues.get("Cooldown"),
						floatValues.get("ChargeDuration"),
						floatValues.get("Rarity"),
						floatValues.get("Width"),
						floatValues.get("Height"),
						floatValues.get("Size"),
						boolValues.get("IsChargeable"),
						Currencies.get(stringValues.get("Currency")),
						floatValues.get("Price")));
		}
	}

	@SuppressWarnings("unchecked")
	private void initEnemiesJson(JsonReader reader) {
		enemyRegistry = new Registry<Enemy, EnemyProperties>(new Enemies());
		FileHandle file = Gdx.files.internal("enemies.json");
		JsonValue enemies = reader.parse(file);
		for (JsonValue enemy: enemies) {
			Map<String, JsonValue> allValues = new HashMap<String, JsonValue> ();
			allValues.put("Name",enemy.get("Name"));
			allValues.put("Desc",enemy.get("Desc"));
			allValues.put("BaseHealth",enemy.get("BaseHealth"));
			allValues.put("MaxHealth",enemy.get("MaxHealth"));
			allValues.put("BaseDamages",enemy.get("BaseDamages"));
			allValues.put("FocusRange",enemy.get("FocusRange"));
			allValues.put("MaxSpeed",enemy.get("MaxSpeed"));
			allValues.put("JumpSpeed",enemy.get("JumpSpeed"));
			allValues.put("Width",enemy.get("Width"));
			allValues.put("Height",enemy.get("Height"));
			allValues.put("Size",enemy.get("Size"));
			allValues.put("DoesFly",enemy.get("DoesFly"));
			allValues.put("IdleImage",enemy.get("IdleImage"));
			allValues.put("IdleImageWidth",enemy.get("IdleImageWidth"));
			allValues.put("IdleImageHeight",enemy.get("IdleImageHeight"));
			allValues.put("MoveImage",enemy.get("MoveImage"));
			allValues.put("MoveImageWidth",enemy.get("MoveImageWidth"));
			allValues.put("MoveImageHeight",enemy.get("MoveImageHeight"));
			allValues.put("JumpImage",enemy.get("JumpImage"));
			allValues.put("JumpImageWidth",enemy.get("JumpImageWidth"));
			allValues.put("JumpImageHeight",enemy.get("JumpImageHeight"));
			allValues.put("Weapons",enemy.get("Weapons"));
			allValues.put("Type",enemy.get("Type"));
			allValues.put("Color", enemy.get("Color"));
			Map<String, String> stringValues = new HashMap<String, String>();
			Map<String, Float> floatValues = new HashMap<String, Float>();
			Map<String, Boolean> boolValues = new HashMap<String, Boolean>();
			Map<String, String[]> stringArrays = new HashMap<String, String[]>();
			Map<String, Map<String, Float>> jsonFloatValues = new HashMap<String, Map<String, Float>>();
			Map<String, Class<? extends ICharacterAI>> classes = new HashMap<String, Class<? extends ICharacterAI>>();
			Map<String, Color> colors = new HashMap<String, Color>();
			boolean[] canRegister = {true};
			allValues.forEach((key, value) -> {
				if (value == null || value.isNull()) {
					canRegister[0] = false;
					System.err.println("Error : Missing value \""+key+"\"");
				} else if (!value.isArray()) {
					if (value.isNumber())
						floatValues.put(value.name, value.asFloat());
					else if (value.isBoolean())
						boolValues.put(value.name, value.asBoolean());
					else if (value.isObject()) {
						Map<String, Float> childValues = new HashMap<String, Float>();
						value.forEach((childValue) -> {
							if (childValue.isNumber())
								childValues.put(childValue.name, childValue.asFloat());
						});
						jsonFloatValues.put(value.name, childValues);
					} else {
						if (value.name.contentEquals("Type")) {
							try {
								Class<?> clazz = Class.forName("fr.iutfbleau.zerotohero.actors.enemies."+value.asString());
								if (Arrays.asList(clazz.getInterfaces()).contains(ICharacterAI.class)) {
									classes.put(value.name, (Class<? extends ICharacterAI>) clazz);
								}
							} catch (Exception e) {
								canRegister[0] = false;
								System.err.println("Error : Class \"fr.iutfbleau.zerotohero.actors.enemies."+value.asString()+"\" does not exist");
							}
						} else {
							stringValues.put(value.name, value.asString());
						}
					}
				} else {
					try {
						int[] colorValues = value.asIntArray();
						colors.put(value.name, new Color(colorValues[0]/255f, colorValues[1]/255f, colorValues[2]/255f, colorValues[3]/255f));
					} catch (Exception e) {
						stringArrays.put(value.name, value.asStringArray());
					}
				}
			});
			if (colors.isEmpty())
				colors.put("Color", new Color(1f, 1f, 1f, 1f));
			if (canRegister[0])
				enemyRegistry.register(enemy.name(), new EnemyProperties(
						stringValues.get("Name"),
						enemy.name(),
						stringValues.get("Desc"),
						floatValues.get("BaseHealth"),
						floatValues.get("MaxHealth"),
						floatValues.get("BaseDamages"),
						floatValues.get("FocusRange"),
						floatValues.get("MaxSpeed"),
						floatValues.get("JumpSpeed"),
						floatValues.get("Width"),
						floatValues.get("Height"),
						floatValues.get("Size"),
						boolValues.get("DoesFly"),
						stringValues.get("IdleImage"),
						new Coordinates(floatValues.get("IdleImageWidth"), floatValues.get("IdleImageHeight")),
						stringValues.get("MoveImage"),
						new Coordinates(floatValues.get("MoveImageWidth"), floatValues.get("MoveImageHeight")),
						stringValues.get("JumpImage"),
						new Coordinates(floatValues.get("JumpImageWidth"), floatValues.get("JumpImageHeight")),
						jsonFloatValues.get("Weapons"),
						classes.get("Type"),
						colors.get("Color")));
		}
	}

    public static int getTileWidth() {
        return TILE_WIDTH;
    }

    public static int getTileHeight() {
        return TILE_HEIGHT;
    }

    public static InputMapper getInputMapper() {
        return inputMapper;
    }

	public static Player getPlayer() {
		return player;
	}

    public static void setPlayer(Player player) {
        ZeroToHero.player = player;
    }

    public static Screen getCurrentScreen() {
        return ZeroToHero.currentScreen;
    }

	public static Assets getAssetManager() {
		return ZeroToHero.assetManager;
	}

	public static AudioPlayer getAudioPlayer() {
		return ZeroToHero.audioPlayer;
	}

	public static GameSettings getSettings() {
		return settings;
	}

	@Override
    public void dispose() {
		super.dispose();
        ZeroToHero.assetManager.dispose();
        Debugger.dispose();
        ZeroToHero.currentScreen = null;
        ZeroToHero.assetManager = null;
        ZeroToHero.player = null;
        ZeroToHero.inputMapper = null;
        ZeroToHero.audioPlayer = null;
        ZeroToHero.settings = null;
    }
}
