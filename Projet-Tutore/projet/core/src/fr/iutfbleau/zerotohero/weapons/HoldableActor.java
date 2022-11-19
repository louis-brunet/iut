package fr.iutfbleau.zerotohero.weapons;

import fr.iutfbleau.zerotohero.actors.AnimatedActor;
import fr.iutfbleau.zerotohero.utils.Coordinates;

public class HoldableActor extends AnimatedActor {
    private final Coordinates anchor;

    public HoldableActor(String name, float width, float height) {
        super(name);
        this.setWidth(width);
        this.setHeight(height);
        anchor = new Coordinates();
    }

    public void setAnchor(float x, float y) {
        this.anchor.setX(x);
        this.anchor.setY(y);

        this.setX(x - this.getWidth() / 2f);
        this.setY(y - this.getHeight() / 2f);
    }

    public float getAnchorX() {
        return this.anchor.getX();
    }

    public float getAnchorY() {
        return this.anchor.getY();
    }
}
