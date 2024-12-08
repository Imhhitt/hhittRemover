package dev.smartshub.hhittRemover.cleaner;

import dev.smartshub.hhittRemover.HhittRemover;
import dev.smartshub.hhittRemover.cleaner.task.CleaningTask;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CleanManager {
    private final HhittRemover plugin;
    private final Map<String, Map<String, Integer>> cleanerConfig = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Block, Integer> blocksToClean = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Entity, Integer> entitiesToClean = new ConcurrentHashMap<>();

    public CleanManager(HhittRemover plugin) {
        this.plugin = plugin;
        loadConfig();
        startCleaningTask();
    }

    /**
     * Load the cleaner configuration from the config.yml file
     */
    public void loadConfig() {
        // Get the cleaner section from the configuration
        ConfigurationSection cleanerSection = plugin.getConfig().getConfigurationSection("cleaner");
        if (cleanerSection == null) {
            plugin.getLogger().warning("No 'Cleaner' section found in the configuration!");
            return;
        }

        // Clear existing configuration
        cleanerConfig.clear();

        // Iterate over each world in the cleaner section
        for (String world : cleanerSection.getKeys(false)) {
            Map<String, Integer> blockTimes = new HashMap<>();
            Map<String, Integer> entityTimes = new HashMap<>();

            // Load block times for the world
            ConfigurationSection blockSection = cleanerSection.getConfigurationSection(world + ".blocks");
            if (blockSection != null) {
                for (String block : blockSection.getKeys(false)) {
                    blockTimes.put(block.toUpperCase(), blockSection.getInt(block));
                }
            }

            // Load entity times for the world
            ConfigurationSection entitySection = cleanerSection.getConfigurationSection(world + ".entities");
            if (entitySection != null) {
                for (String entity : entitySection.getKeys(false)) {
                    entityTimes.put(entity.toUpperCase(), entitySection.getInt(entity));
                }
            }

            // Combine the block and entity times
            Map<String, Integer> combinedTimes = new HashMap<>();
            combinedTimes.putAll(blockTimes);
            combinedTimes.putAll(entityTimes);
            cleanerConfig.put(world, combinedTimes);
        }
    }

    /**
     * Add a block to cleaning queue with its specific clean time
     *
     * @param block Block to be cleaned
     */
    public void addBlockToClean(Block block) {
        String world = block.getWorld().getName();
        String blockType = block.getType().name();

        if (!isBlockCleanable(world, blockType)) {
            return;
        }

        blocksToClean.put(block, getBlockTime(world, blockType));
    }

    /**
     * Add an entity to cleaning queue with its specific clean time
     *
     * @param entity Entity to be cleaned
     */
    public void addEntityToClean(Entity entity) {
        String world = entity.getWorld().getName();
        String entityType = entity.getType().name();

        if (!isEntityCleanable(world, entityType)) {
            return;
        }

        entitiesToClean.put(entity, getEntityTime(world, entityType));
    }

    /**
     * Removes a specific block from cleaning queue
     * @param block Block to remove
     */
    public void removeBlockFromClean(Block block) {
        blocksToClean.remove(block);
    }

    /**
     * Removes a specific entity from cleaning queue
     * @param entity Entity to remove
     */
    public void removeEntityFromClean(Entity entity) {
        entitiesToClean.remove(entity);
    }

    /**
     * Update the remaining time for a block
     * @param block Block to update
     * @param remainingTime New remaining time
     */
    public void updateBlockTime(Block block, int remainingTime) {
        if (remainingTime > 0) {
            blocksToClean.put(block, remainingTime);
        } else {
            blocksToClean.remove(block);
        }
    }

    /**
     * Update the remaining time for an entity
     * @param entity Entity to update
     * @param remainingTime New remaining time
     */
    public void updateEntityTime(Entity entity, int remainingTime) {
        if (remainingTime > 0) {
            entitiesToClean.put(entity, remainingTime);
        } else {
            entitiesToClean.remove(entity);
        }
    }

    // Maps getters
    public ConcurrentHashMap<Block, Integer> getBlocksToClean() {
        return blocksToClean;
    }

    public ConcurrentHashMap<Entity, Integer> getEntitiesToClean() {
        return entitiesToClean;
    }

    // Other methods not so relevant for the comparison
    public boolean isWorldCleanable(String world) {
        return cleanerConfig.containsKey(world);
    }

    public boolean isBlockCleanable(String world, String block) {
        return cleanerConfig.containsKey(world) &&
                cleanerConfig.get(world).containsKey(block.toUpperCase());
    }

    public boolean isEntityCleanable(String world, String entity) {
        return cleanerConfig.containsKey(world) &&
                cleanerConfig.get(world).containsKey(entity.toUpperCase());
    }

    public int getBlockTime(String world, String block) {
        return cleanerConfig.get(world).get(block.toUpperCase());
    }

    public int getEntityTime(String world, String entity) {
        return cleanerConfig.get(world).get(entity.toUpperCase());
    }

    public void startCleaningTask() {
        new CleaningTask(this).runTaskTimer(plugin, 0, 20L);
    }
}
