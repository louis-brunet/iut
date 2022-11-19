//package fr.iutfbleau.zerotohero.physics.example.PlayerTesting;
//
//import fr.iutfbleau.zerotohero.entities.PlayerCharacter;
//import fr.iutfbleau.zerotohero.physics.Body;
//import fr.iutfbleau.zerotohero.physics.ContactListener;
//
//public class PlayerContactListener implements ContactListener {
//    private PlayerCharacter player;
//    private Body body;
//    public PlayerContactListener(PlayerCharacter p,Body b)
//    {
//        super();
//        this.player = p;
//        this.body = b;
//    }
//
//    @Override
//    public void onContact(Body b) {
//        System.out.println(this.body.getType().name() + " collided with " + b.getType().name());
//        this.player.floorContact();
//    }
//
//    @Override
//    public void onContactEnded(Body b) {}
//}
