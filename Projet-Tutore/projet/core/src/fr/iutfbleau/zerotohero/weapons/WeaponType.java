package fr.iutfbleau.zerotohero.weapons;

public enum WeaponType {
    SIMPLE_GUN(false,"weapons/fiveseven.png", 128f, 128f, 0.2f),
    SIMPLE_BOW(true,"weapons/bow_idle.png", 70f, 90f, 0.4f);

    private final String idleTexturePath;
    private final float textureWidth, textureHeight;
    private final float scale;
    private final boolean isChargeable;

    WeaponType(boolean isChargeable, String idleTexturePath, float textureWidth,
               float textureHeight, float scale) {
        this.idleTexturePath = idleTexturePath;
        this.textureWidth    = textureWidth;
        this.textureHeight   = textureHeight;
        this.scale           = scale;
        this.isChargeable    = isChargeable;
    }
//    public Body createBody(float x, float y){
////        switch(this) {
////            case SIMPLE_GUN:
////                break;
////            default:
////                throw new IllegalStateException("WeaponType not recognized : "+name());
////        }
//        BoundingShape shape = new AxisAlignedBoundingBox(new Coordinates(x,y),
//                                                         textureWidth * scale / 2f,
//                                                         textureHeight * scale / 2f);
//
//        return new Body(Body.Type.NO_CLIP_TILE_ENTITY, false, shape, new Coordinates(x,y));
//    }

    public String getIdleTexturePath() {
        return this.idleTexturePath;
    }

    public float getTextureWidth() {
        return textureWidth;
    }

    public float getTextureHeight() {
        return textureHeight;
    }

    public float getScale() {
        return scale;
    }

    public boolean isChargeable() {
        return isChargeable;
    }
}
