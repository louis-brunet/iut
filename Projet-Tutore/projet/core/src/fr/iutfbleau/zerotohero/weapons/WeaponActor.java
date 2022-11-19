package fr.iutfbleau.zerotohero.weapons;

import com.badlogic.gdx.math.Vector2;

import fr.iutfbleau.zerotohero.registries.WeaponProperties;
import fr.iutfbleau.zerotohero.room.Room;

public abstract class WeaponActor extends HoldableActor {
    public static final short IDLE_ANIMATION_ID = 0;
    // public static final short FIRE_ANIMATION_ID = 1;

    private final Vector2 aiming, position, toCursor;
    private Room room;
    private WeaponProperties type;

    public WeaponActor(WeaponProperties type) {
        super("weapon_"+type.getId().toLowerCase(), type.getWidth(), type.getHeight());
        this.type = type;
        this.setRoom(room);

        this.aiming = new Vector2();
        this.position = new Vector2();
        this.toCursor = new Vector2();

        this.setOrigin(type.getWidth() / 2f, type.getHeight() / 2f);
    }

//    public abstract void aim(float x, float y);
//    public abstract void use();

    /**
     * Updates the weapon.
     *
     * @param delta
     * @param cursorX
     * @param cursorY
     * @param use is the USE_WEAPON action triggered ?
     * @return the created Projectile, or null if no Projectile was fired
     */
    public abstract Projectile update(float delta, float cursorX, float cursorY, boolean use);

    public void aim(float x, float y) {
        this.aiming.set(x, y);
        this.position.set(this.getAnchorX(), this.getAnchorY());

        this.flipX = x < this.getAnchorX();

        this.toCursor.set(aiming.x - position.x,
                          aiming.y - position.y);

        this.setRotation( toCursor.angle() + (flipX ? 180f : 0));
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public WeaponProperties getType() {
        return type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDirectionToCursor() {
        return toCursor;
    }
}
