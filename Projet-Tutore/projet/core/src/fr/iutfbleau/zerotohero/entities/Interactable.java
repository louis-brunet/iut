package fr.iutfbleau.zerotohero.entities;

import fr.iutfbleau.zerotohero.stages.RoomOverlay;

public interface Interactable {
	void interact();
	
	boolean canInteract();

	String getInteractText();

	default void drawAdditionalInteractOverlay(RoomOverlay overlay) { }
}
