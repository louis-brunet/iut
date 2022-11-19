//package fr.iutfbleau.zerotohero.physics.example.PlayerTesting;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import fr.iutfbleau.zerotohero.utils.Coordinates;
//import fr.iutfbleau.zerotohero.physics.AxisAlignedBoundingBox;
//import fr.iutfbleau.zerotohero.physics.Body;
//import fr.iutfbleau.zerotohero.physics.World;
//import fr.iutfbleau.zerotohero.entities.PlayerCharacter;
//
//public class TestPlayerStage extends Stage {
//
//    private World world;
//
//    public TestPlayerStage() {
//        super();
//        this.world = new World();
//
//        Body b = this.createPlatform("RECTANGLE" , Gdx.graphics.getWidth()/2,
//                Gdx.graphics.getHeight()/2, 60f, 15f);
//
//        this.world.addBody(b);
//
//        // b is platform, not player body. Intended ?
//        this.addActor(new PlayerCharacter(b, "badlogic.jpg", false));
//
//        //
//
//        Body playerBody = this.createPlayerBody("RECTANGLE", Gdx.graphics.getWidth()/2,
//                (Gdx.graphics.getHeight()/2)+60f, 15f, 30f,new Coordinates(0f, -5f));
//
//        PlayerCharacter player = new PlayerCharacter(playerBody,"badlogic.jpg", false);
//        PlayerContactListener tba = new PlayerContactListener(player,playerBody);
//        this.addActor(player);
//
//        if(player.getBody() == playerBody) System.out.println("IT IS THE SAME WTF");
//
//        playerBody.addContactListener(tba);
//        // Expression lambda
//        // Pareil que  (body -> this.testOnContact(body))
//        // Pareil que ( new ContactListener() {@Override public void onContact(Body b) {testOnContact(b);}} )
////        playerBody.addContactListener(this::testOnContact);
//
//        this.world.addBody(player.getBody());
//
//    }
//
//    private Body createPlatform(String name, float x, float y, float halfX, float halfY) {
//        return new Body(
//                Body.Type.GROUND,
//                false,
//                new AxisAlignedBoundingBox(new Coordinates(x, y), halfX, halfY),
//                new Coordinates(x, y));
//    }
//
//    private Body createPlayerBody(String name, float x, float y, float halfX, float halfY, Coordinates speed) {
//        return new Body(
//                Body.Type.PLAYER,
//                true,
//                new AxisAlignedBoundingBox(new Coordinates(x, y), halfX, halfY),
//                new Coordinates(x, y),
//                speed);
//    }
//
//    @Override
//    public void act(float delta) {
//        super.act(delta);
//        this.world.update(delta);
//    }
//}
