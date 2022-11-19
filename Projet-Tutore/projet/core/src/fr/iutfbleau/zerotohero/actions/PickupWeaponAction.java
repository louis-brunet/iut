package fr.iutfbleau.zerotohero.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

import fr.iutfbleau.zerotohero.actors.Player;
import fr.iutfbleau.zerotohero.registries.WeaponProperties;
import fr.iutfbleau.zerotohero.room.Room;
import fr.iutfbleau.zerotohero.weapons.Projectile;
import fr.iutfbleau.zerotohero.weapons.WeaponActor;
import fr.iutfbleau.zerotohero.weapons.WeaponFactory;

/**
 * The actor performing this action must be the player.
 * Makes the player pick up a weapon.
 */
public class PickupWeaponAction extends Action {
    /**
     * The type of weapon to pick up
     */
    private WeaponProperties type;

    public PickupWeaponAction() {}

    /**
     * Initializes this action to pick up the given weapon type.
     */
    public void init(WeaponProperties weapon) {
        this.type = weapon;
    }

    /**
     * Updates the action based on time. Typically this is called each frame by
     * Actor.act(float).
     *
     * @param delta Time in seconds since the last frame.
     * @return true if the action is done. This method may continue to be called after
     * the action is done.
     */
    @Override
    public boolean act(float delta) {
        Player player =  (Player) this.actor;
        Room room = player.getRoom();
        WeaponActor oldWeapon = player.getWeapon();

        if (oldWeapon != null) {
            oldWeapon.remove();
            room.removeActor(oldWeapon, null);
            room.spawnWeapon(oldWeapon,
                                       player.getBody().getPosition().getX(),
                                       player.getBody().getPosition().getY());
        }
        player.setWeapon(WeaponFactory.create(this.type, Projectile.Collision.PLAYER));
        room.addActor(player.getWeapon());

        return true;
    }

    /**
     * Resets the optional state of this action to as if it were newly created,
     * allowing the action to be pooled and reused. State
     * required to be set for every usage of this action or computed during the action
     * does not need to be reset.
     * <p>
     * The default implementation calls {@link #restart()}.
     * <p>
     * If a subclass has optional state, it must override this method, call super, and
     * reset the optional state.
     */
    @Override
    public void reset() {
        super.reset();
        this.type = null;
    }
}
