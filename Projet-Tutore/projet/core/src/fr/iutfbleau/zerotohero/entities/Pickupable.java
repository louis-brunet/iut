package fr.iutfbleau.zerotohero.entities;

import com.badlogic.gdx.scenes.scene2d.Action;

public interface Pickupable {
    Action getPickupAction();

    boolean isAutoPickup();
}
