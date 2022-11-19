package fr.iutfbleau.zerotohero.entities.shop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.AnimatedActor;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.entities.Pickupables;
import fr.iutfbleau.zerotohero.game.Assets;
import fr.iutfbleau.zerotohero.registries.ItemProperties;
import fr.iutfbleau.zerotohero.registries.WeaponProperties;

import java.util.*;

public class ShopSpawner {
    public static Actor createShopActor(ShopType type, Coordinates centerBottom) {
        Assets assets = ZeroToHero.getAssetManager();
        assets.addAsset(type.getTexturePath(), Texture.class);
        Texture texture = assets.getAsset(type.getTexturePath(), Texture.class);

        AnimatedActor actor = new AnimatedActor("shop_"+type.name().toLowerCase());

        actor.setWidth(texture.getWidth() * type.getScale() / type.getAnimationColumns());
        actor.setHeight(texture.getHeight() * type.getScale() / type.getAnimationRows());
        actor.setX(centerBottom.getX(), Align.bottom);
        actor.setY(centerBottom.getY(), Align.bottom);
        actor.setScale(type.getScale());


        short animationId = 0;
        actor.addAnimation(animationId, texture, type.getAnimationRows(), type.getAnimationColumns(), 0.4f);
        actor.setCurrentAnimation(animationId, false);

        return actor;
    }

    public static List<Product> createSecondaryProducts(List<Coordinates> centerBottoms, Random rand) {
        List<Product> allSmallProducts = new ArrayList<>();
        allSmallProducts.add(new Product(Pickupables.bomb(0, 0), Currencies.GOLD, 20f));
        allSmallProducts.add(new Product(Pickupables.hearts(1, 0,0), Currencies.GOLD, 50f));
        allSmallProducts.add(new Product(Pickupables.key( 0,0), Currencies.GOLD, 30f));

        return ShopSpawner.selectProducts(allSmallProducts, centerBottoms, rand);
    }

    public static List<Product> createPrimaryProducts(ShopType shopType, List<Coordinates> centerBottoms, Random rand) {
        List<Product> allProducts = null; //= Product.createProducts();

        switch(shopType) {
            case ITEMS:
                List<ItemProperties> allItems = ZeroToHero.itemRegistry.getAllProperties();
                allProducts = new LinkedList<>();

                for(ItemProperties item: allItems) {
                    allProducts.add( new Product(Pickupables.item(item,0,0), item.getCurrency(), item.getPrice()) );
                }
                break;
            case WEAPONS:
                List<WeaponProperties> allWeapons = ZeroToHero.weaponRegistry.getAllProperties();
                allProducts = new LinkedList<>();

                for(WeaponProperties weapon: allWeapons) {
                    allProducts.add( new Product(Pickupables.weapon(weapon, 0,0 ), weapon.getCurrency(), weapon.getPrice()));
                }
                break;
        }

        return ShopSpawner.selectProducts(allProducts, centerBottoms, rand);
    }

    private static List<Product> selectProducts(List<Product> allProducts, List<Coordinates> centerBottoms, Random rand) {
        List<Product> chosenProducts = new LinkedList<>();

        int productCount = Math.min(centerBottoms.size(), allProducts.size());
        for (int i = 0; i < productCount; i++) {
            Coordinates centerBottom = centerBottoms.get(i);
            int index = rand.nextInt(allProducts.size());
            Product choice = allProducts.remove(index);
            choice.getBody().setPosition(centerBottom.getX(), centerBottom.getY() + choice.getBody().getBounds().getHalfHeight());
            chosenProducts.add(choice);
        }

        return chosenProducts;
    }
//    private static List<ItemProperties> getRandomItems(int itemCount, List<ItemProperties> allItems, final Random rand) {
//        // copy to not remove items from given list
//        allItems = new LinkedList<>(allItems);
//        // cap number of selected items to number of available items
//        itemCount = Math.min(itemCount, allItems.size());
//        List<ItemProperties> pickedItems = new LinkedList<>();
//
//        for (int i = 0; i < itemCount; i++) {
//            int index = rand.nextInt(allItems.size());
//            ItemProperties item = allItems.get( index );
//            pickedItems.add(item);
//            allItems.remove(index);
//        }
//
//        return pickedItems;
//    }

//    private final List<Product> startingProducts;
//
//    public ShopSpawner(ShopType type, List<Coordinates> bigProductPositions, List<Coordinates> smallProductPositions, Random random) {
//        this(ShopSpawner.getProducts( , productCount) );
//    }
//
//    public ShopSpawner(List<Product> products) {
//        this.availableProducts = products;
//    }

//    public void buy(Product product) {
//        Objects.requireNonNull(product);
//        if (! this.availableProducts.contains(product))
//            throw new IllegalArgumentException("This shop does not sell "+product.getProductName()+".");
//
//
//        if (product.tryToBuy()) {
//            this.availableProducts.remove(product);
//        } else {
//            GameplayScreen gs = ((GameplayScreen) ZeroToHero.getCurrentScreen());
//            gs.getUI()
//              .addMessage("You can't afford this item.", Color.RED, 2f);
//        }
//    }
}
