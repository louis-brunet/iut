package fr.iutfbleau.zerotohero.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool;

import fr.iutfbleau.zerotohero.actors.PhysicsActor;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.room.Room;

import java.util.Objects;
import java.util.function.Supplier;

public class Projectile extends PhysicsActor implements Pool.Poolable {
    public enum Collision {
        PLAYER(new Body.Type[]{ Body.Type.JUMP_THROUGH,
                                Body.Type.PLAYER ,
                                Body.Type.PROJECTILE,
                                Body.Type.NO_CLIP_TILE_ENTITY }),
        ENEMY(new Body.Type[]{ Body.Type.JUMP_THROUGH,
                               Body.Type.ENEMY,
                               Body.Type.PROJECTILE,
                               Body.Type.NO_CLIP_TILE_ENTITY  });

        private final Body.Type[] nonSolidBodyTypes;

        Collision(Body.Type[] nonSolidBodyTypes) {this.nonSolidBodyTypes = nonSolidBodyTypes;}

        public Body.Type[] getNonSolidBodyTypes() {
            return nonSolidBodyTypes;
        }
    }

    public static final short MOVING_ANIMATION_ID = 0;

    private Supplier<Action> effectSupplier;

    public Projectile() {}

    public void init(String name, Body b, Collision collision, Room room,
                     Supplier<Action> effectSupplier) {
        Objects.requireNonNull(b);
        Objects.requireNonNull(collision);
        Objects.requireNonNull(room);
        Objects.requireNonNull(effectSupplier);

        this.setName(name);
        this.setBody(b, collision);
        this.setRoom(room);
        this.effectSupplier = effectSupplier;
    }

    public void setBody(Body body, Collision collision) {
        super.setBody(body);
        body.addContactListener(new ProjectileContactListener(this, collision));
    }

    public Action getEffect() {
        return this.effectSupplier.get();
    }

    public void addEffect(Supplier<Action> effect) {
        Supplier<Action> copy = this.effectSupplier;
        this.effectSupplier = () -> Actions.sequence(copy.get(),
                                                     effect.get());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1f,1f,0.6f,0.7f);
        super.draw(batch, parentAlpha);
        batch.setColor(Color.WHITE);
    }

    @Override
    public void reset() {
        this.effectSupplier = null;
        this.removeAnimations();
        if (this.getRoom() != null) {
            this.getRoom().removeActor(this, this.body);
            this.setRoom(null);
        }
        this.setBody(null);
        this.remove();

        this.getActions().forEach(this::removeAction);
        this.setStage(null);
        this.clear();
    }
}
