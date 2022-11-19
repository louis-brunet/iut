package fr.iutfbleau.zerotohero.actors.enemies;

import com.badlogic.gdx.math.MathUtils;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.Character;

public class Test implements ICharacterAI {
    private float cycleDuration, cycleTimer;
    
	public Test() {
        this.cycleDuration = MathUtils.random(0.3f, 0.5f);
        this.cycleTimer = 0f;
	}

	@Override
	public void onAddedToCharacter(Character character) {
		character.setMoveLeft(true);
		character.setMoveRight(false);
	}

	@Override
	public void onCharacterAddedToRoom(Character character) {

	}

	@Override
	public void updateCharacter(fr.iutfbleau.zerotohero.actors.Character character, float delta) {
        this.cycleTimer += delta;
        if (this.cycleTimer >= this.cycleDuration) {
        	character.setMoveRight( ! character.isMoveRight());
        	character.setMoveLeft( ! character.isMoveLeft());

            this.cycleTimer %= this.cycleDuration;
        }
        character.setJump(MathUtils.random(255) == 0);
	}

	@Override
	public void running(Character character) {
		character.setCurrentAnimation(EnemyActor.RUN_ANIMATION_ID, true);
	}

	@Override
	public void idle(Character character) {
		character.setCurrentAnimation(EnemyActor.IDLE_ANIMATION_ID, true);
	}

	@Override
	public void jumping(Character character) {
		character.setCurrentAnimation(EnemyActor.JUMP_ANIMATION_ID, false);
	}

	@Override
	public void onJumpStart(Character character) {

	}
}
