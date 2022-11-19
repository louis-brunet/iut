package fr.iutfbleau.zerotohero.actors;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.enemies.ICharacterAI;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.entities.Bomb.BombType;
import fr.iutfbleau.zerotohero.screens.GameplayScreen;
import fr.iutfbleau.zerotohero.utils.InputAction;
import fr.iutfbleau.zerotohero.weapons.Projectile;

public class PlayerAI implements ICharacterAI {
    private static final String JUMP_SOUND_FILE_NAME = "jump.wav";
	private Player player;

	public PlayerAI() {
    }
	
	public void setPlayer(Player player) {
		this.player = player;
	}

    @Override
    public void onAddedToCharacter(Character character) { }

    @Override
    public void onCharacterAddedToRoom(Character character) {

    }

    @Override
    public void updateCharacter(Character character, float delta) {
        character.setMoveRight(player.getInputMapper().isTriggered(InputAction.MOVE_RIGHT));
        character.setMoveLeft(player.getInputMapper().isTriggered(InputAction.MOVE_LEFT));

        character.setFallThrough(player.getInputMapper().isTriggered(InputAction.FALL_THROUGH));
        character.setJump(player.getInputMapper().isTriggered(InputAction.JUMP));

        if (player.getInputMapper().isTriggered(InputAction.INTERACT))
            character.getRoom().interactWithClosestInteractable();

        if (player.getInputMapper().isTriggered(InputAction.DROP_BOMB)) {
            if (player.getAmountOfBombs() > 0) {
            	player.removeBombs(1);
                character.getRoom().placeBomb(BombType.NORMAL, 1, 2, 3,
                                         character.body.getTotalSpeed());
                ((GameplayScreen) ZeroToHero.getCurrentScreen()).getUI()
                                                                .addMessage("Bombe lachée !", Color.GOLD, 2f);
            } else {
                ((GameplayScreen) ZeroToHero.getCurrentScreen()).getUI()
                                                                .addMessage("Pas de bombe !", Color.RED, 2f);
            }
        }

        if (player.getWeapon() != null) {
        	player.getWeapon().setAnchor(character.getBody().getPosition().getX(),
                                  character.getBody().getPosition().getY());
            Coordinates worldCursor = player.getInputMapper().getCursorWorldCoordinates();

            boolean use = player.getInputMapper().isTriggered(InputAction.USE_WEAPON);
            Projectile fired = player.getWeapon().update(delta, worldCursor.getX(), worldCursor.getY(), use);

            if (fired != null) {
                this.player.getItems().forEach( item -> {
                    item.getProperties().getAction().onShoot(fired);
                });
            }
        }

        // debug
        player.handleDebugInput();


        player.updateColor();
    }
    
    @Override
    public void running(Character character) {
    	character.setCurrentAnimation(Player.RUN_ANIMATION_ID, true);
    }

    @Override
    public void idle(Character character) {
    	character.setCurrentAnimation(Player.IDLE_ANIMATION_ID, true);
    }

    @Override
    public void jumping(Character character) {
        character.setCurrentAnimation(Player.JUMP_ANIMATION_ID, false);
	}

    @Override
    public void onJumpStart(Character character) {
	    ZeroToHero.getAudioPlayer().playSound(PlayerAI.JUMP_SOUND_FILE_NAME);
    }

    @Override
    public void die(Character character) {
	    Screen s = ZeroToHero.getCurrentScreen();
	    if (s instanceof GameplayScreen)
            ((GameplayScreen) s).onPlayerDeath();
    }
}
