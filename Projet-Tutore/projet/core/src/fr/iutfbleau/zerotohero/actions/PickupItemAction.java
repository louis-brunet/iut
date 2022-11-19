package fr.iutfbleau.zerotohero.actions;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Action;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.Player;
import fr.iutfbleau.zerotohero.registries.ItemProperties;
import fr.iutfbleau.zerotohero.screens.GameplayScreen;
import fr.iutfbleau.zerotohero.stages.GameplayUI;

/**
 * This action's actor must be the player. Adds an item to the player's inventory.
 */
public class PickupItemAction extends Action {
    /**
     * The properties of the item to add to the player's inventory.
     */
    private ItemProperties type;

    public PickupItemAction() {}

    /**
     * Initiliazes this action to add the given item to the player.
     * @param item the item to add
     */
    public void init(ItemProperties item) {
        this.type = item;
    }

    /**
     * Updates the player's inventory in the UI.
     */
    private void updateUI() {
        Screen s = ZeroToHero.getCurrentScreen();
        if (s instanceof GameplayScreen) {
            GameplayUI ui = ((GameplayScreen) s).getUI();
            if (ui != null)
                ui.updatePlayerItems();
        }
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
//        Room room = player.getRoom();

        System.out.println("Grabbing item : "+this.type.getName());

        player.getItems().forEach((playerItem) -> {
            playerItem.getProperties().getAction().onPickupItem();
        });


        this.type.getAction().onPickup();
        player.addItem(this.type.getId());
        this.updateUI();

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
