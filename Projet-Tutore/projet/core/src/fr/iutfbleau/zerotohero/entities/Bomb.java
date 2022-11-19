package fr.iutfbleau.zerotohero.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actions.ActionFactory;
import fr.iutfbleau.zerotohero.actors.Character;
import fr.iutfbleau.zerotohero.actors.SolidActor;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.physics.CollisionDirection;
import fr.iutfbleau.zerotohero.stages.RoomRenderer;
import fr.iutfbleau.zerotohero.utils.Coordinates;

public class Bomb extends SolidActor {

	private static final short ANIMATION_ID = 0;
	private static final Coordinates FRICTION = new Coordinates(200, 1000);
	private static final float MIN_SPEED_Y = -1000f;
	
	private BombType type;
	private int power;
	private float range, cooldown;
	private ParticleEffect sparks;
	
	public Bomb(String textureFilePath, Body b, BombType type, int power, float range, float cooldown, Coordinates initialSpeed) {
		super(type.toString().toLowerCase()+"_bomb", b);
		this.type = type;
		this.power = power;
		this.range = range;
		this.cooldown = cooldown;

		this.body.setSpeed(initialSpeed.getX(), initialSpeed.getY());

		ZeroToHero.getAssetManager().addAsset(textureFilePath, Texture.class);
        this.addAnimation(Bomb.ANIMATION_ID,
                          ZeroToHero.getAssetManager().getAsset(textureFilePath, Texture.class),
                          1, 1, 0.1f);
        this.setCurrentAnimation(Bomb.ANIMATION_ID, false);
        
		sparks = new ParticleEffect();
		sparks.load(Gdx.files.internal("particleEffects/BombSparks"),Gdx.files.internal("particleEffects"));
		sparks.getEmitters().first().setPosition(this.getX(), this.getY());
		sparks.start();
	}
	
//	@Override
//	public void act(float delta) {
//		super.act(delta);
//
//		this.cooldown -= delta;
//		if (this.cooldown < 0) {
//			explode();
//		}
//
////		System.out.println(this.cooldown);
//	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		

    	sparks.update(Gdx.graphics.getDeltaTime());
        sparks.draw(batch);
        if (sparks.isComplete())
        	sparks.reset();
	}

	@Override
	protected void addActions(float delta) {
		this.cooldown -= delta;
		if (this.cooldown < 0) {
			explode();
		} else {
			ParallelAction speedActions = Actions.parallel();

			if (this.body.getSpeed().getX() < - delta * Bomb.FRICTION.getX()) {
				speedActions.addAction(ActionFactory.addSpeed(delta * Bomb.FRICTION.getX(), 0f));
			} else if (this.body.getSpeed().getX() > delta * Bomb.FRICTION.getX()) {
				speedActions.addAction(ActionFactory.addSpeed(- delta * Bomb.FRICTION.getX(), 0f));
			} else {
				speedActions.addAction(ActionFactory.setSpeedX(0f));
			}
			if( ! this.isBlocked(CollisionDirection.DOWN)) {
				if (this.body.getSpeed().getY() < Bomb.MIN_SPEED_Y) {
					speedActions.addAction(ActionFactory.setSpeedY(Bomb.MIN_SPEED_Y));
//					speedActions.addAction(ActionFactory.setSpeed(this.body.getSpeed().getXdiuhgiueriugr(), Bomb.MIN_SPEED_Y));
				} else {
					speedActions.addAction(ActionFactory.addSpeed(0f,
																  -delta * Bomb.FRICTION.getY()));
				}
			}
			this.addAction(speedActions);
			sparks.setPosition(this.getX(), this.getY());
		}
	}

	public void explode() {
		this.getRoom()
			.getCloseActors(this, range)
			.stream()
			.forEach(actor -> {
				if (actor instanceof Character)
					((Character) actor).damage(power);
				if (actor instanceof Destroyable)
					if (((Destroyable) actor).canBeDestroyed()) ((Destroyable) actor).destroy(power);
				});

		((RoomRenderer) this.getStage()).shake(0.5f);
		((RoomRenderer) this.getStage()).flash(Color.WHITE);
		((RoomRenderer) this.getStage()).addParticles("particleEffects/BombExplosionSmoke", this.getX()+this.getWidth()/2, this.getY()+this.getHeight()/2);
		((RoomRenderer) this.getStage()).addParticles("particleEffects/BombExplosion1", this.getX()+this.getWidth()/2, this.getY()+this.getHeight()/2);
		((RoomRenderer) this.getStage()).addParticles("particleEffects/BombExplosion2", this.getX()+this.getWidth()/2, this.getY()+this.getHeight()/2);
		this.getRoom().removeActor(this, this.body);
		this.remove();
	}
	
	public BombType getType() {
		return type;
	}
	
	public int getPower() {
		return power;
	}
	
	public float getRange() {
		return range;
	}
	
	public float getCooldown() {
		return cooldown;
	}
	
	public enum BombType {
		NORMAL, FIRE, ICE;
	}
}
