package fr.iutfbleau.zerotohero.actors.enemies;

import com.badlogic.gdx.math.MathUtils;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.Character;

public interface ICharacterAI {
    void onAddedToCharacter(Character character);
	void onCharacterAddedToRoom(Character character);
    // Update character's state & intended movement direction
    void updateCharacter(Character character, float delta);
    // Set animations, ...
    void running(Character character);
    void idle(Character character);
    void jumping(Character character);
    void onJumpStart(Character character);

    // Called when dead
    default void die(Character character) {
        float maxOffset = 30f;
        float x = character.getBody().getPosition().getX() + MathUtils.random(- maxOffset, maxOffset);
        float y = character.getBody().getPosition().getY() + MathUtils.random(- maxOffset, maxOffset);

        ZeroToHero.getPlayer().getItems().forEach((item) -> {
            item.getProperties().getAction().onEnemyDied(x, y);
        });

        character.getRoom().removeActor(character, character.getBody());
        character.remove();

        int droppedHealth = MathUtils.random(4) - 3;
        if (droppedHealth > 0) {
            character.getRoom().spawnHeart(droppedHealth,
                                           character.getBody().getPosition().getX(),
                                           character.getBody().getPosition().getY());
        } else {
            int droppedGold = MathUtils.random(5);
            if (droppedGold > 0) {
                character.getRoom().spawnGold(droppedGold,
                                              character.getBody().getPosition().getX(),
                                              character.getBody().getPosition().getY());
            }
        }
    }
}
