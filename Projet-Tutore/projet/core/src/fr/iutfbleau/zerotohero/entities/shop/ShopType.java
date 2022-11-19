package fr.iutfbleau.zerotohero.entities.shop;

public enum ShopType {
    ITEMS("shop/shop_items.png", 1, 1, 0.5f),
    WEAPONS("shop/shop_weapons.png",1 ,1, 0.5f);

    private final String texturePath;
    private final int animationRows, animationColumns;
    private final float scale;
    ShopType(String texturePath, int rows, int columns, float scale) {
        this.texturePath = texturePath;
        this.animationColumns = columns;
        this.animationRows = rows;
        this.scale         = scale;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public int getAnimationRows() {
        return animationRows;
    }

    public int getAnimationColumns() {
        return animationColumns;
    }

    public float getScale() {
        return scale;
    }
}