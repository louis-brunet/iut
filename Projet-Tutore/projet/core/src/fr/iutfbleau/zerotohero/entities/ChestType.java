package fr.iutfbleau.zerotohero.entities;

public enum ChestType {
    NORMAL("Open"),
    LOCKED_KEY("Unlock"); //,
    // LOCKED_GOLD,
    // LOCKED_DAMAGE
    private String interactText;
    ChestType(String interactText) {
        this.interactText = interactText;
    }

    public String getInteractText() {
        return interactText;
    }
}
