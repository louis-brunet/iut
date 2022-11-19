package fr.iutfbleau.zerotohero.entities.shop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.Player;

public class GoldCurrency implements Currency {
    public GoldCurrency() {
        ZeroToHero.getAssetManager().addAsset("gui/moneyIcon.png", Texture.class);
    }

    @Override
    public Texture getIcon() {
        return ZeroToHero.getAssetManager().getAsset("gui/moneyIcon.png", Texture.class);
    }

    @Override
    public String toPriceString(float price) {
        return String.format("%d", MathUtils.ceil(price));
    }

    @Override
    public boolean canPlayerPurchase(float price) {
        Player player = ZeroToHero.getPlayer();
        return player.getMoney() >= MathUtils.ceil(price);
    }

    @Override
    public void applyPurchase(float price) {
        Player player = ZeroToHero.getPlayer();
        player.addMoney( - MathUtils.ceil(price));
    }
}
