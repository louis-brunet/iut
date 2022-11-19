package fr.iutfbleau.zerotohero.actors.enemies;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.registries.EnemyProperties;

/**
 * An EnemySpawn has a position and a map of enemy types to weights. The weights are used
 * to assign a probability to the possible spawnable enemy types.
 */
public class EnemySpawn {
    /** The spawn point. Spawned enemies' center bottom position. */
    private final Coordinates position;
    /** Enemy type weights used to determine probabilities */
    private final Map<EnemyProperties, Integer> enemyRegistryWeights;

    /**
     * Creates an EnemySpawn at the given position, and with the given enemy type weights.
     *
     * @param pos the spawned enemies' center bottom position
     * @param enemyRegistryWeights map of enemy properties id to spawn probability weights
     */
    public EnemySpawn(Coordinates pos, Map<String, Integer> enemyRegistryWeights) {
        this.position = pos;
        
        this.enemyRegistryWeights = new HashMap<EnemyProperties, Integer>();
        enemyRegistryWeights.forEach((key, value) -> {
            EnemyProperties properties = ZeroToHero.enemyRegistry.getProperties(key);
            Objects.requireNonNull(properties, "Enemy properties don't exist for "+key);
        	this.enemyRegistryWeights.put(properties, value);
        });
    }

    /**
     * Returns the position where the spawned enemies' center bottom should be.
     * @return the position where the spawned enemies' center bottom should be
     */
    public Coordinates getPosition() {
        return position;
    }

    /**
     * Uses the map of enemy properties to weights to determine their spawn probability.
     * Uses the given Random to choose an enemy type to spawn, using the computed
     * probabilities.
     *
     * @param random the Random to use
     * @return the chosen enemy properties
     */
    public EnemyProperties getRandomEnemy(Random random) {
        int totalWeights = this.enemyRegistryWeights.values()
                                                	.stream()
                                                	.mapToInt(Integer::intValue)
                                                	.sum();
        if (totalWeights > 0) {
	        int randomInt = random.nextInt(totalWeights);
	        int offset;
	        int i = 0;
	        for(Map.Entry<EnemyProperties, Integer> weight: this.enemyRegistryWeights.entrySet()) {
	            offset = i;
	            while(i < weight.getValue() + offset) {
                    Objects.requireNonNull(weight.getKey());
	                if ( randomInt == i )
	                    return weight.getKey();
	
	                i++;
	            }
	        }
        }

        return null;
    }
}
