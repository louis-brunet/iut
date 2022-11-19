package fr.iutfbleau.zerotohero.actors;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;

import fr.iutfbleau.zerotohero.actions.ActionFactory;
import fr.iutfbleau.zerotohero.actors.enemies.ICharacterAI;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.physics.CollisionDirection;
import fr.iutfbleau.zerotohero.utils.ViewDirection;

public abstract class Character extends SolidActor implements StatContainer {
    private final Map<Stat, Object> statValues;
    private boolean moveLeft, moveRight, jump;
    private ViewDirection facing;
    private float invincibility;
    private final float maxInvincibility;
    private ICharacterAI ai;

    public Character(String name, Body b, int startingHealth, int maxMaxHealth,
                     int shields, int maxShields, Coordinates maxSpeed,
                     float accelerationX, float groundFrictionX,
                     float airFrictionX, float jumpSpeed, float gravity,
                     float invicibilityDuration, ICharacterAI ai) {
        this(name, b, startingHealth, maxMaxHealth, shields, maxShields, maxSpeed,
             accelerationX, groundFrictionX, airFrictionX, jumpSpeed, gravity,
             invicibilityDuration, null, ai);
    }

    // facing right
    public Character(String name, Body b, int startingHealth, int maxMaxHealth, int shields,
                     int maxShields, Coordinates maxSpeed, float accelerationX,
                     float groundFrictionX, float airFrictionX, float jumpSpeed,
                     float gravity, float invincibilityDuration, Color color, ICharacterAI ai) {
        super(name, b);
        this.setColor(color);

        this.statValues = new EnumMap<Stat, Object>(Stat.class);
        this.facing = ViewDirection.RIGHT;
        this.invincibility = 0f;
        this.maxInvincibility = invincibilityDuration;
//        this.wasJumping = false;

        this.setStatValue(Stat.HEALTH, startingHealth);
        this.setStatValue(Stat.MAX_HEARTS, startingHealth);
        this.setStatValue(Stat.MAX_HEALTH, maxMaxHealth);
        this.setStatValue(Stat.SHIELDS, shields);
        this.setStatValue(Stat.MAX_SHIELDS, maxShields);
        this.setStatValue(Stat.MAX_SPEED, maxSpeed);
        this.setStatValue(Stat.MAX_SPEED_FACTOR, 1f);
        this.setStatValue(Stat.JUMP_SPEED, jumpSpeed);
        this.setStatValue(Stat.ACCELERATION, new Coordinates(accelerationX, 0f));
        this.setStatValue(Stat.FRICTION_GROUND, groundFrictionX);
        this.setStatValue(Stat.FRICTION_AIR, airFrictionX);
        this.setStatValue(Stat.GRAVITY, gravity);
        
        this.body.addContactListener(new DamageContactListener(this));

        this.setAi(ai);
    }

    @Override
    public <V> void setStatValue(Stat stat, V value) {
        this.statValues.put(stat, stat.getValueClass().cast(value));
    }

    @Override
    public <V> V getStatValue(Stat stat, Class<V> valueClass) {
        return valueClass.cast(this.statValues.get(stat));
    }

    @Override
    public boolean containsStat(Stat stat) {
        return this.statValues.containsKey(stat);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (this.getColor() != null) {
            batch.setColor(this.getColor());
            super.draw(batch, parentAlpha);
            batch.setColor(Color.WHITE);
        } else
            super.draw(batch, parentAlpha);
    }

    @Override
    protected void addActions(float delta) {
        this.invincibility = Math.max(0f, this.invincibility - delta);

        if (this.ai != null)
            this.ai.updateCharacter(this, delta);
        
        Coordinates maxSpeed = new Coordinates(this.getStatValue(Stat.MAX_SPEED, Coordinates.class));
        maxSpeed.setX(maxSpeed.getX() * this.getStatValue(Stat.MAX_SPEED_FACTOR, Float.class));

        Coordinates acceleration = this.getStatValue(Stat.ACCELERATION,Coordinates.class);
        Float friction = this.isBlocked(CollisionDirection.DOWN) ?
                         this.getStatValue(Stat.FRICTION_GROUND, Float.class) :
                         this.getStatValue(Stat.FRICTION_AIR, Float.class);
        Float gravity = this.getStatValue(Stat.GRAVITY, Float.class);

        ParallelAction speedActions = Actions.parallel();
        ViewDirection newFacing = this.facing;


        if (this.moveRight && !this.moveLeft) {
            speedActions.addAction(ActionFactory.addSpeed(delta * acceleration.getX(),
                                                          0f));
            newFacing = ViewDirection.RIGHT;

            if (this.isBlocked(CollisionDirection.DOWN))
                this.ai.running(this);

        }else if (this.moveLeft && !this.moveRight) {
            speedActions.addAction(ActionFactory.addSpeed(-delta * acceleration.getX(),
                                                          0f));
            newFacing = ViewDirection.LEFT;


            if (this.isBlocked(CollisionDirection.DOWN))
                this.ai.running(this);

        }else if (this.body.getSpeed().getX() > delta * friction)
            speedActions.addAction(ActionFactory.addSpeed(- delta * friction, 0f));
        else if (this.body.getSpeed().getX() < - delta * friction)
            speedActions.addAction(ActionFactory.addSpeed(delta * friction, 0f));
        else {
            speedActions.addAction(ActionFactory.setSpeedX(0f));

            if ( this.isBlocked(CollisionDirection.DOWN) )
                this.ai.idle(this);
        }

        if(this.jump && this.isBlocked(CollisionDirection.DOWN)) {
            speedActions.addAction(ActionFactory.setSpeedY(this.getStatValue(Stat.JUMP_SPEED,
                                                                             Float.class)));
            this.ai.onJumpStart(this);
        } else if ( ! this.isBlocked(CollisionDirection.DOWN) ) {
            speedActions.addAction(ActionFactory.addSpeed(0f,- delta * gravity));

            this.ai.jumping(this);
        }

        speedActions.addAction(ActionFactory.clampSpeed(-maxSpeed.getX(), maxSpeed.getX(),
                                                        -maxSpeed.getY(), maxSpeed.getY()));
        this.addAction(speedActions);

        if ( ! this.facing.equals(newFacing)) {
            this.flipX = ! this.flipX;
            this.facing = newFacing;
        }

//        this.wasJumping = this.jump;
    }

    public void addStatValue(Stat stat, int amount, int minimum) {
        this.addStatValue(stat, amount, minimum, -1);
    }

    public void addStatValue(Stat stat, int amount, int minimum, int maximum) {
        if (maximum >= 0 && minimum > maximum)
            throw new IllegalArgumentException("min > max");


        int added = amount + this.getStatValue(stat, Integer.class);
        int newValue;

        if (maximum >= 0)
            newValue = Math.max(minimum, Math.min(maximum, added));
        else
            newValue = Math.max(minimum, added);

        this.setStatValue(stat, newValue);
    }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public boolean isMoveLeft() {
        return moveLeft;
    }

    public boolean isMoveRight() {
        return moveRight;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public int getHealth() {
        return this.getStatValue(Stat.HEALTH, Integer.class);
    }

    public boolean isAlive() {
        return this.getHealth() > 0;
    }

    public void heal(int amount, boolean noMaxHealth) {
        if (noMaxHealth)
            this.addStatValue(Stat.HEALTH, amount, 0, this.getMaxHealth());
        else
            this.addStatValue(Stat.HEALTH, amount, 0, this.getMaxHearts());
    }
    
    public ViewDirection getFacing() {
		return facing;
	}

    public void damage(int amount) {
        if (amount < 0)
            throw  new IllegalArgumentException("Damage amount can't be negative.");
        if (this.invincibility > 0f) {
            System.out.println("Invincibility frames prevented damage.");
            return;
        }

        int	shieldAmount = Math.min(amount, this.getShields());
        int heartAmount = amount-shieldAmount;

        this.removeShields(shieldAmount);
        this.heal( - heartAmount, true);
        this.invincibility = this.maxInvincibility;
        if (invincibility > 0f)
            this.addAction(ActionFactory.showInvincibility(this.maxInvincibility, 0.1f));

        if ( ! this.isAlive()) {
            this.ai.die(this);
        }

//        System.out.println(getName()+" took "+amount+" dmg ("+heartAmount+" hearts, "+shieldAmount+" shields)");
    }

    public int getMaxHearts() {
        return this.getStatValue(Stat.MAX_HEARTS, Integer.class);
    }

    public void addMaxHearts(int amount) {
        this.addStatValue(Stat.MAX_HEARTS, amount, this.getMaxHearts(), this.getMaxHearts());
    }

    public int getMaxHealth() {
        return this.getStatValue(Stat.MAX_HEALTH, Integer.class);
    }

    public void addShields(int amount) {
        this.addStatValue(Stat.SHIELDS, amount, 0, this.getMaxShields());
    }

    public void removeShields(int amount) {
        this.addShields( - amount);
    }

    public int getShields() {
        return this.getStatValue(Stat.SHIELDS, Integer.class);
    }

    public int getMaxShields() {
        return this.getStatValue(Stat.MAX_SHIELDS, Integer.class);
    }

	public void propulse(float x, float y, ViewDirection direction) {
		this.body.addSpeed((direction == ViewDirection.LEFT) ? -x : x, y);
	}

    public void setAi(ICharacterAI ai) {
        this.ai = ai;
        if (ai != null)
            ai.onAddedToCharacter(this);
    }
}
