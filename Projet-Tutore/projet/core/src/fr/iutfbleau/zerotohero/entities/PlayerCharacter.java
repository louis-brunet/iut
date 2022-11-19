//package fr.iutfbleau.zerotohero.entities;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import fr.iutfbleau.zerotohero.actors.PhysicsActor;
//import fr.iutfbleau.zerotohero.physics.Body;
//
//import java.util.Objects;
//
//public class PlayerCharacter extends PhysicsActor {
//
//    private Texture texture;
//    private boolean floorContact;
//
//    public PlayerCharacter(Body b, Texture t,String textureFilePath, boolean animated) {
//        super(textureFilePath,b,animated);
//        Objects.requireNonNull(b);
//        this.texture = t;
//        this.floorContact = false;
//    }
//
//    public PlayerCharacter(Body b,String textureFilePath,boolean animated) {
//        super(textureFilePath,b,animated);
//        this.floorContact = false;
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        super.draw(batch, parentAlpha);
//    }
//
//    @Override
//    public void act(float delta)
//    {
//        if (this.floorContact)
//        {
//            System.out.println("Touching ground");
//            this.body.setSpeed(0, 0);
//            this.floorContact = false;
//        }
//    }
//
//    public void floorContact()
//    {
//        this.floorContact = true;
//    }
//}
