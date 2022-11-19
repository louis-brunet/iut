package fr.iutfbleau.zerotohero.actors.enemies;

import com.badlogic.gdx.graphics.Color;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.physics.Body;

public class ShieldSkeleton /*extends EnemyActor*/{ // TODO !!!!!!!!!!!!!!!!!!! commenté temporairement pour éviter erreurs @ compilation

    public static final short RUN_ANIMATION_ID = 0;
    public static final short JUMP_ANIMATION_ID = 1;
    public static final short IDLE_ANIMATION_ID = 2;

    private static final Color COLOR = new Color(0.4f,0,0f,1f);
    private static final int STARTING_HEALTH = 30;
    private static final int MAX_HEALTH = 100;
    private static final int STARTING_SHIELDS = 0;
    private static final int MAX_SHIELDS = 5;
    private static final Coordinates MAX_SPEED = new Coordinates(200, 600);
    private static final float ACCELERATION = 500f;
    private static final float FRICTION = 1500f;
    private static final float JUMP_SPEED = 0f;
    private static final float GRAVITY = 2000f;

    private static final String ACTOR_NAME = "ShieldSkeleton";

    private float cycleDuration, cycleTimer;

    public ShieldSkeleton(Body b)
    {
//        super(ShieldSkeleton.ACTOR_NAME, b, ShieldSkeleton.STARTING_HEALTH, ShieldSkeleton.MAX_HEALTH,
//                ShieldSkeleton.STARTING_SHIELDS, ShieldSkeleton.MAX_SHIELDS,
//                ShieldSkeleton.MAX_SPEED, ShieldSkeleton.ACCELERATION, ShieldSkeleton.FRICTION,
//                ShieldSkeleton.FRICTION, ShieldSkeleton.JUMP_SPEED, ShieldSkeleton.GRAVITY, ShieldSkeleton.COLOR);
    }

//	@Override TODO !!!!!!!!!!!!!!!!!!! commenté temporairement pour éviter erreurs @ compilation
	public void updateCharacter(float delta) {
		// TODO Auto-generated method stub

	}

//	@Override TODO !!!!!!!!!!!!!!!!!!! commenté temporairement pour éviter erreurs @ compilation
	public void running() {
		// TODO Auto-generated method stub

	}

//	@Override TODO !!!!!!!!!!!!!!!!!!! commenté temporairement pour éviter erreurs @ compilation
	public void idle() {
		// TODO Auto-generated method stub

	}

//	@Override
	public void jumping() {
		// TODO Auto-generated method stub

	}
}
