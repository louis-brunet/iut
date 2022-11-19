package fr.iutfbleau.zerotohero.entities.shop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import fr.iutfbleau.zerotohero.ZeroToHero;

public class HeartsCurrency implements Currency {
    public HeartsCurrency() {
        ZeroToHero.getAssetManager().addAsset("gui/heartIcon.png", Texture.class);
    }

    @Override
    public Texture getIcon() {
        return ZeroToHero.getAssetManager().getAsset("gui/heartIcon.png", Texture.class);
    }

    @Override
    public String toPriceString(float price) {
        return "" + MathUtils.ceil(price);
    }

    @Override
    public boolean canPlayerPurchase(float price) {
        return ZeroToHero.getPlayer().getHealth() > MathUtils.ceil(price);
    }

    @Override
    public void applyPurchase(float price) {
        ZeroToHero.getPlayer().damage(MathUtils.ceil(price));
    }
}
