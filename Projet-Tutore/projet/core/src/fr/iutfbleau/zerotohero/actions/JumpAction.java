//package fr.iutfbleau.zerotohero.actions;
//
//import fr.iutfbleau.zerotohero.actors.PhysicsActor;
//
//public class JumpAction extends SetSpeedAction {
//
//    public JumpAction() {
//        this(0f);
//    }
//
//    public JumpAction (float jumpSpeed) {
//        this.setYValue(jumpSpeed);
//    }
//
//    @Override
//    public boolean act(float delta) {
//        this.setXValue(((PhysicsActor) this.getActor()).getBody().getSpeed().getX());
//        return super.act(delta);
//    }
//}
