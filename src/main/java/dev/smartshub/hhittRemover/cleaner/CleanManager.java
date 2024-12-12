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
    private final ConcurrentHashMap<Block, Long> blocksToClean = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Entity, Long> entitiesToClean = new ConcurrentHashMap<>();

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

        long blockMs = System.currentTimeMillis() + (getBlockTime(world, blockType) * 1000L);
        blocksToClean.put(block, blockMs);
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

        long entityMs = System.currentTimeMillis() + (getEntityTime(world, entityType) * 1000L);
        entitiesToClean.put(entity, entityMs);
    }

    // Maps getters
    public ConcurrentHashMap<Block, Long> getBlocksToClean() {
        return blocksToClean;
    }

    public ConcurrentHashMap<Entity, Long> getEntitiesToClean() {
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
