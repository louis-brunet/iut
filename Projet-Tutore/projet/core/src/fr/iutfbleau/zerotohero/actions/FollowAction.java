//package fr.iutfbleau.zerotohero.actions;
//
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.scenes.scene2d.Action;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import fr.iutfbleau.zerotohero.actors.PhysicsActor;
//import fr.iutfbleau.zerotohero.utils.Coordinates;
//import fr.iutfbleau.zerotohero.physics.Body;
//
//import java.util.Objects;
//
//public class FollowAction extends Action {
//    private Body body, bodyToFollow;
//    private float totalSpeed;
//
//    private Coordinates direction;
//    private Vector2 speed;
//
//    public FollowAction() {
//        this.body = null;
//        this.bodyToFollow = null;
//    }
//
//    public void init(Body toFollow, float speed) {
//        this.bodyToFollow = toFollow;
//        this.totalSpeed = speed;
//
//        this.direction = new Coordinates();
//        this.speed = null;
//    }
//
//    @Override
//    public void reset() {
//        super.reset();
//        this.body = null;
//        this.bodyToFollow = null;
//        this.direction = null;
//        this.speed = null;
//        this.totalSpeed = 0f;
//    }
//
//    @Override
//    public void setActor(Actor actor) {
//        super.setActor(actor);
//        if (actor != null) {
//            this.body = ((PhysicsActor) this.actor).getBody();
//        } else {
//            this.body = null;
//        }
//    }
//
//    @Override
//    public boolean act(float delta) {
//        Objects.requireNonNull(this.body);
//        Objects.requireNonNull(this.bodyToFollow);
//
//        // TODO
//        direction.setX(this.bodyToFollow.getPosition().getX() - this.body.getPosition().getX());
//        direction.setY(this.bodyToFollow.getPosition().getY() - this.body.getPosition().getY());
//        speed = direction.toVector2().nor().scl(this.totalSpeed);
//
//        this.getActor().addAction(ActionFactory.setSpeed(speed.x, speed.y));
//
//        return false;
//    }
//}
