package fr.iutfbleau.zerotohero.entities.shop;

import com.badlogic.gdx.graphics.Texture;

public interface Currency {
    Texture getIcon();
    String toPriceString(float price);
    boolean canPlayerPurchase(float price);
    void applyPurchase(float price);
}
