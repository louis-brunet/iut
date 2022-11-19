package fr.iutfbleau.zerotohero.actors.enemies;

import java.util.Objects;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.physics.AxisAlignedBoundingBox;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.physics.BoundingShape;
import fr.iutfbleau.zerotohero.registries.EnemyProperties;
import fr.iutfbleau.zerotohero.room.Room;

/**
 * A factory class to create enemy actors.
 */
public class EnemyFactory {
    /**
     * Creates an enemy whose type is chosen randomly from the given EnemySpawn's possible
     * types.
     *
     * @param room the room in which to spawn the enemy
     * @param spawn the EnemySpawn used to determine an enemy type
     * @param random the Random used when selecting an enemy type
     * @return the created EnemyActor
     */
    public static EnemyActor randomEnemy(Room room, EnemySpawn spawn, Random random) {
        EnemyProperties toSpawn = spawn.getRandomEnemy(random);
        Objects.requireNonNull(toSpawn);
        return EnemyFactory.createEnemy(toSpawn, spawn.getPosition(), room);
    }

    /**
     * Creates an enemy of the given type, spawned in the given room, at the given position.
     *
     * @param properties the enemy's type
     * @param spawn the enemy's center bottom position
     * @param room the room in which to spawn the enemy
     * @return the created EnemyActor
     */
	public static EnemyActor createEnemy(EnemyProperties properties, Coordinates spawn, Room room) {
    	BoundingShape shape = new AxisAlignedBoundingBox(
                new Coordinates(spawn.getX() ,
                                spawn.getY() + properties.getHeight() / 2f),
                properties.getWidth() / 2f, properties.getHeight() / 2f);
        Body body = new Body(Body.Type.ENEMY,
                             true,
                             shape,
                             new Coordinates(spawn.getX(),
                                             spawn.getY() + properties.getHeight() / 2f));

        ICharacterAI enemyAI = null;
		try {
			enemyAI = properties.getAI().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
			e.printStackTrace();
		}
		if (enemyAI == null) {
			System.err.println("Error with "+properties.getId()+", cannot create enemy !");
			return null;
		}
		EnemyActor enemy = new EnemyActor(properties, body, enemyAI);
        
		room.getWorld().addBody(body);
		
        enemy.setSize(properties.getWidth(), properties.getHeight());
        enemy.setOffsetX(-properties.getWidth()/2);

        ZeroToHero.getAssetManager().addAsset(properties.getMoveImagePath(), Texture.class);
        enemy.addAnimation(EnemyActor.RUN_ANIMATION_ID,
		        		   ZeroToHero.getAssetManager().getAsset(properties.getMoveImagePath(), Texture.class),
		        		   (int) properties.getMoveImageSize().getY(),
		        		   (int) properties.getMoveImageSize().getX(), 0.05f);

        ZeroToHero.getAssetManager().addAsset(properties.getJumpImagePath(), Texture.class);
        enemy.addAnimation(EnemyActor.JUMP_ANIMATION_ID,
                           ZeroToHero.getAssetManager().getAsset(properties.getJumpImagePath(), Texture.class),
                           (int) properties.getJumpImageSize().getY(),
                           (int) properties.getJumpImageSize().getX(), 0.05f);

        ZeroToHero.getAssetManager().addAsset(properties.getIdleImagePath(), Texture.class);
        enemy.addAnimation(EnemyActor.IDLE_ANIMATION_ID,
                           ZeroToHero.getAssetManager().getAsset(properties.getIdleImagePath(), Texture.class),
                           (int) properties.getIdleImageSize().getY(),
                           (int) properties.getIdleImageSize().getX(), 1f);
        enemy.setCurrentAnimation(EnemyActor.IDLE_ANIMATION_ID, false);
        
        enemy.setScale(properties.getSize());
        enemy.setRoom(room);

        enemyAI.onCharacterAddedToRoom(enemy);
        return enemy;
    }
}
