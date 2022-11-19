package fr.iutfbleau.zerotohero.entities.shop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.entities.PickupableActor;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.screens.GameplayScreen;
import fr.iutfbleau.zerotohero.stages.RoomOverlay;

import java.util.function.Supplier;

public class Product extends PickupableActor {
    private final Currency currency;
    private final float price;

    public Product(PickupableActor pickupable, Currency currency, float price) {
        this(pickupable.getName(), pickupable.getBody(), pickupable::getPickupAction, currency, price);

        this.setScale(pickupable.getScaleX(), pickupable.getScaleY());

        short animationId = 0;
        this.addAnimation(animationId, pickupable.getCurrentAnimation());
        this.setCurrentAnimation(animationId, true);
    }

    public Product(String name, Body b, Supplier<Action> onPurchased,
                   Currency currency, float price) {
        super(name, b, false, onPurchased);
        this.currency    = currency;
        this.price       = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public void interact() {
        // return if player can't afford
        if ( ! this.currency.canPlayerPurchase(this.price)) {
            GameplayScreen gs = ((GameplayScreen) ZeroToHero.getCurrentScreen());
            gs.getUI()
              .addMessage("You can't afford this item.", Color.RED, 2f);
            return;
        }

        // else pay & apply pickup action
        this.currency.applyPurchase(this.price);
        super.interact();
    }

    @Override
    public String getInteractText() {
        return  "Buy";
    }

    @Override
    public void drawAdditionalInteractOverlay(RoomOverlay overlay) {
        overlay.drawPrice( this.currency.toPriceString(this.price),
                           this.currency.getIcon(),
                           this.currency.canPlayerPurchase(this.price),
                           this.getBody().getPosition() );
    }
}
