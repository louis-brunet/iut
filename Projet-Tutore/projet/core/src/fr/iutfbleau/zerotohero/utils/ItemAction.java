package fr.iutfbleau.zerotohero.utils;

import fr.iutfbleau.zerotohero.ZeroToHero;

public enum ItemAction {
	PLAYER_HEAL("Player.heal", () -> {
		ZeroToHero.getPlayer().addMaxHearts(1);
	}),
	PLAYER_ADD_SHIELD("Player.add_shield", () -> {
		ZeroToHero.getPlayer().addShields(1);
	});

	private String ID;
	private Runnable actions;
	
	ItemAction(String ID, Runnable actions) {
		this.ID = ID;
		this.actions = actions;
	}

	/**
	 * Tries to find and return the ItemAction that has this identifier
	 * @param ID The string identifier
	 * @return The ItemAction that has this identifier, null if no action found 
	 */
	public ItemAction getActionByID(String ID) {
		for (ItemAction action : ItemAction.values()) {
			if (action.getID().equals(ID))
				return action;
		}
		return null;
	}
	
	public String getID() {
		return this.ID;
	}
	
	public void act() {
		this.actions.run();
	}
}
