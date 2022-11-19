package fr.iutfbleau.zerotohero.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.registries.Item;
import fr.iutfbleau.zerotohero.room.Room;
import fr.iutfbleau.zerotohero.screens.GameplayScreen;
import fr.iutfbleau.zerotohero.stages.GameplayUI;
import fr.iutfbleau.zerotohero.utils.InputAction;
import fr.iutfbleau.zerotohero.utils.InputMapper;
import fr.iutfbleau.zerotohero.utils.Tuple3;
import fr.iutfbleau.zerotohero.weapons.WeaponActor;

/**
 * The player character that goes from room to room.
 */
public class Player extends Character {
    /** Animation id for idle state */
    public static final short IDLE_ANIMATION_ID = 0;
    /** Animation id for running state */
    public static final short RUN_ANIMATION_ID = 1;
    /** Animation id for jumping or falling state */
    public static final short JUMP_ANIMATION_ID = 2;

    /** Actor's name */
    private static final String ACTOR_NAME = "player_actor";
    /** Starting tint */
    private static final Color COLOR = new Color(1f,1f,1f,1f);
    /** Whether to invert the tint color. */
    private Boolean invertColor = false;
    /** Starting max speed */
    private static final Coordinates MAX_SPEED = new Coordinates(250, 600);
    /** Starting jump speed */
    private static final float JUMP_SPEED = 550;
    /** Starting health */
    private static final int STARTING_HEALTH = 3;
    /** Starting max health */
    private static final int MAX_HEALTH = 10;
    /** Starting shields */
    private static final int STARTING_SHIELDS = 0;
    /** Starting max shields */
    private static final int MAX_SHIELDS = 10;
    /** Starting acceleration */
    private static final float ACCELERATION = 1500f;
    /** Starting ground friction */
    private static final float FRICTION_GROUND = 1500f;
    /** Starting air friction */
    private static final float FRICTION_AIR = 200f;
    /** Downward gravity */
    private static final float GRAVITY = 1000f;
    /** Duration of invincibility after taking damage */
    private static final float INVINCIBILITY_DURATION = 0.4f;
    /** Range to look for interactive actors */
    public static final float INTERACT_RANGE = 2f;
    /** Max gold the player can hold */
    private static final int MAX_GOLD = 999;

    /** The InputMapper to use to handle user input */
    private final InputMapper inputMapper;
    /** The player's held weapon */
    private WeaponActor weapon;
    /** The player's held items */
    private List<Item> items;

    /**
     * Creates a player actor.
     *
     * @param defaultRoom the player's starting room
     * @param inputMapper the InputMapper to use for user input
     * @param b the player's body
     * @param ai player behaviour
     */
    public Player(Room defaultRoom, InputMapper inputMapper, Body b, PlayerAI ai) {
        super(Player.ACTOR_NAME, b, Player.STARTING_HEALTH, Player.MAX_HEALTH,
              Player.STARTING_SHIELDS, Player.MAX_SHIELDS,
              Player.MAX_SPEED, Player.ACCELERATION, Player.FRICTION_GROUND,
              Player.FRICTION_AIR, Player.JUMP_SPEED, Player.GRAVITY,
              Player.INVINCIBILITY_DURATION, Player.COLOR, ai);
        ai.setPlayer(this);

        this.setRoom(defaultRoom);
        this.inputMapper = inputMapper;

        this.items = new ArrayList<Item>();
        
        this.setStatValue(Stat.KEYS, 0);
        this.setStatValue(Stat.BOMBS, 0);
        this.setStatValue(Stat.GOLD, 0);

        this.body.addContactListener(new ConnectionContactListener(this));
        this.body.addContactListener(new PickupContactListener(this));
    }
    
    public Boolean getInvertColor() {
		return invertColor;
	}
    
    public void setInvertColor(Boolean invertColor) {
		this.invertColor = invertColor;
	}

    /**
     * Update the player's tint to match it's room's background gradient.
     */
    void updateColor() {

        List<Color> colors = this.getRoom().getRenderingColors();
        float x = body.getPosition().getX(),
                y = body.getPosition().getY(),
                width = getRoom().getWidth(true),
                height = getRoom().getHeight(true),
                xPercentage = x/width,
                yPercentage = y/height;
        Tuple3<Float, Float, Float>
                topLeft = new Tuple3<Float, Float, Float>(
                colors.get(3).r,colors.get(3).g,colors.get(3).b),
                topRight = new Tuple3<Float, Float, Float>(
                        colors.get(2).r,colors.get(2).g,colors.get(2).b),
                bottomLeft = new Tuple3<Float, Float, Float>(
                        colors.get(0).r,colors.get(0).g,colors.get(0).b),
                bottomRight = new Tuple3<Float, Float, Float>(
                        colors.get(1).r,colors.get(1).g,colors.get(1).b),
                topDifference = new Tuple3<Float, Float, Float>(
                        topLeft.a-topRight.a, topLeft.b-topRight.b, topLeft.c-topRight.c),
                bottomDifference = new Tuple3<Float, Float, Float>(
                        bottomLeft.a-bottomRight.a, bottomLeft.b-bottomRight.b, bottomLeft.c-bottomRight.c),
                colorTop = new Tuple3<Float, Float, Float>(
                        topLeft.a-topDifference.a*xPercentage,
                        topLeft.b-topDifference.b*xPercentage,
                        topLeft.c-topDifference.c*xPercentage),
                colorBottom = new Tuple3<Float, Float, Float>(
                        bottomLeft.a-bottomDifference.a*xPercentage,
                        bottomLeft.b-bottomDifference.b*xPercentage,
                        bottomLeft.c-bottomDifference.c*xPercentage),
                difference = new Tuple3<Float, Float, Float>(
                        colorBottom.a-colorTop.a,
                        colorBottom.b-colorTop.b,
                        colorBottom.c-colorTop.c),
                colorValues = new Tuple3<Float, Float, Float>(
                        colorBottom.a-difference.a*yPercentage,
                        colorBottom.b-difference.b*yPercentage,
                        colorBottom.c-difference.c*yPercentage);

        Color color = new Color(1,1,1,1);
        if (invertColor)
            color.sub(new Color(colorValues.a, colorValues.b, colorValues.c, 1));
        else
            color.mul(new Color(colorValues.a, colorValues.b, colorValues.c, 1));

        color.a = 1f;
        this.setColor(color.sub(0.3f,0.3f,0.3f, 0f));//.add(0, 0, 0, 1));
    }

    /**
     * Sets the value addociated to the given stat. Refreshed the HUD if necessary.
     *
     * @param stat the stat whose value to modify
     * @param value the value to set
     * @param <V> the value's class
     * @throws ClassCastException if value is of incompatible class
     */
    @Override
    public <V> void setStatValue(Stat stat, V value) {
        super.setStatValue(stat, value);
        if (GameplayUI.PLAYER_STATS.contains(stat))
            this.refreshUIDisplay();
    }

    /**
     * Handles debug input.
     */
    void handleDebugInput() {
        if (this.getInputMapper().isTriggered(InputAction.GIVE_KEY)) {
            this.addKeys(1);
            ((GameplayScreen) ZeroToHero.getCurrentScreen()).getUI().addMessage("Clé donnée !", Color.CYAN, 2f);
        }

        if (this.getInputMapper().isTriggered(InputAction.GIVE_BOMB)) {
            this.addBombs(1);
            ((GameplayScreen) ZeroToHero.getCurrentScreen()).getUI().addMessage("Bombe donnée !", Color.CYAN, 2f);
        }

        if (this.getInputMapper().isTriggered(InputAction.GIVE_MONEY)) {
            this.addMoney(1);
            ((GameplayScreen) ZeroToHero.getCurrentScreen()).getUI().addMessage("Monnaie donnée !", Color.CYAN, 2f);
        }

        if (this.getInputMapper().isTriggered(InputAction.HEAL)) {
            this.heal(1, true);
            ((GameplayScreen) ZeroToHero.getCurrentScreen()).getUI().addMessage("Soigné !", Color.CYAN, 2f);
        }

        if (this.getInputMapper().isTriggered(InputAction.DAMAGE)) {
            this.damage(1);
            ((GameplayScreen) ZeroToHero.getCurrentScreen()).getUI().addMessage("Blessé !", Color.CYAN, 2f);
        }

        if (this.getInputMapper().isTriggered(InputAction.GIVE_SHIELD)) {
            this.addShields(1);
            ((GameplayScreen) ZeroToHero.getCurrentScreen()).getUI().addMessage("Bouclier donné !", Color.CYAN, 2f);
        }

        if (this.getInputMapper().isTriggered(InputAction.REMOVE_SHIELD)) {
            this.removeShields(1);
            ((GameplayScreen) ZeroToHero.getCurrentScreen()).getUI().addMessage("Bouclié retiré !", Color.CYAN, 2f);
        }
    }

    /**
     * Returns the amount of keys the player is holding.
     * @return the player's key count
     */
    public int getAmountOfKeys() {
        return this.getStatValue(Stat.KEYS, Integer.class);
    }

    /**
     * Adds keys to the player's inventory.
     * @param amount the amount of keys to add
     */
    public void addKeys(int amount) {
        this.addStatValue(Stat.KEYS, amount, 0);
    }

    /**
     * Remove keys from the player's inventory
     * @param amount the amount of keys to remove
     */
    public void removeKeys(int amount) {
        this.addKeys( - amount);
    }

    /**
     * Returns the amount of bombs the player is holding.
     * @return the player's bomb count
     */
    public int getAmountOfBombs() {
        return this.getStatValue(Stat.BOMBS, Integer.class);
    }

    /**
     * Adds bombs to the player's inventory.
     * @param amount the amount of bombs to add
     */
    public void addBombs(int amount) {
        this.addStatValue(Stat.BOMBS, amount, 0);
    }

    /**
     * Remove bombs from the player's inventory
     * @param amount the amount of bombs to remove
     */
    public void removeBombs(int amount) {
        this.addBombs( - amount);
    }

    /**
     * Returns the player's gold count
     * @return the player's gold count
     */
    public int getMoney() {
        return this.getStatValue(Stat.GOLD, Integer.class);
    }

    /**
     * Gives the player gold
     * @param amount the amount of gold to give
     */
    public void addMoney(int amount) {
    	if ((this.getStatValue(Stat.GOLD, Integer.class) + amount) >= MAX_GOLD)
    		this.setStatValue(Stat.GOLD, MAX_GOLD);
    	else
    		this.addStatValue(Stat.GOLD, amount, 0);
    }

    /**
     * Sets the player's held weapon. If null, removes the player's held weapon.s
     * @param w the weapon to hold, or null for no weapon
     */
    public void setWeapon(WeaponActor w) {
        this.weapon = w;
        if (w != null) {
            w.setRoom(this.getRoom());
        }
    }

    /**
     * Returns the player's held weapon
     * @return the player's weapon
     */
    public WeaponActor getWeapon() {
        return weapon;
    }

    /** Refreshes the HUD's player info, excluding the player's held items */
    private void refreshUIDisplay() {
        Screen screen = ZeroToHero.getCurrentScreen();
        if (screen instanceof GameplayScreen) {
            GameplayUI ui = ((GameplayScreen) screen).getUI();
            if (ui != null)
                ui.refreshPlayerInfo();
        }
    }

    /**
     * Sets the player's current room to the given room. The room is marked as visited
     * and the RoomRenderer's room to render is updated.
     *
     * @param room the player's new room
     */
    @Override
    public void setRoom(Room room) {
    	super.setRoom(room);
    	if (ZeroToHero.getCurrentScreen() instanceof GameplayScreen) {
    		((GameplayScreen) ZeroToHero.getCurrentScreen()).getRoomRenderer().updateRoom(this);
    	}
        room.getWorld().addBody(this.body);
        room.setVisited(true);
    }

    /**
     * Returns the input mapper used for user input
     * @return the input mapper used for user input
     */
	public InputMapper getInputMapper() {
		return inputMapper;
	}

	/**
     * Returns the player's held items
     * @return the player's list of held items
	 */
	public List<Item> getItems() {
		return this.items;
	}

    /**
     * Adds an item the player's inventory.
     * @param itemId the id of the item to add in the "items" registry
     */
	public void addItem(String itemId) {//PickupableActor item) {
		Item itemToAdd = ZeroToHero.itemRegistry.getRegisterable(itemId);
		if (itemToAdd != null)
			this.items.add(itemToAdd);
		else
            System.out.println("Player couldn't find item to add : "+itemId);
	}
}
