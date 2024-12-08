package dev.smartshub.hhittRemover.cleaner;

import dev.smartshub.hhittRemover.HhittRemover;
import dev.smartshub.hhittRemover.cleaner.task.CleaningTask;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class CleanManager {

    private final HhittRemover plugin;
    private final Map<String, Map<String, Integer>> cleanerConfig = new HashMap<>();
    private final HashMap<Block, Integer> blocksToClean = new HashMap<>();
    private final HashMap<Entity, Integer> entitiesToClean = new HashMap<>();

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

        // Iterate over each world in the cleaner section
        for (String world : cleanerSection.getKeys(false)) {
            Map<String, Integer> blockTimes = new HashMap<>();
            Map<String, Integer> entityTimes = new HashMap<>();

            // Here we are going to load the block and entity times for each world
            // First blocks
            ConfigurationSection blockSection = cleanerSection.getConfigurationSection(world + ".blocks");
            if (blockSection != null) {
                for (String block : blockSection.getKeys(false)) {
                    blockTimes.put(block.toUpperCase(), blockSection.getInt(block));
                }
            }
            // Now entities
            ConfigurationSection entitySection = cleanerSection.getConfigurationSection(world + ".entities");
            if (entitySection != null) {
                for (String entity : entitySection.getKeys(false)) {
                    entityTimes.put(entity.toUpperCase(), entitySection.getInt(entity));
                }
            }

            // Combine the block and entity times into a single map
            Map<String, Integer> combinedTimes = new HashMap<>();
            combinedTimes.putAll(blockTimes);
            combinedTimes.putAll(entityTimes);
            cleanerConfig.put(world, combinedTimes);
        }
    }

    public void addBlockToClean(Block block) {
        blocksToClean.put(block, getBlockTime(block.getWorld().getName(), block.getType().name()));
    }

    public void addEntityToClean(Entity entity) {
        entitiesToClean.put(entity, getEntityTime(entity.getWorld().getName(), entity.getType().name()));
    }

    public HashMap<Block, Integer> getBlocksToClean() {
        return blocksToClean;
    }

    public HashMap<Entity, Integer> getEntitiesToClean() {
        return entitiesToClean;
    }

    public boolean isWorldCleanable(String world) {
        return cleanerConfig.containsKey(world);
    }

    public boolean isBlockCleanable(String world, String block) {
        return cleanerConfig.get(world).containsKey(block);
    }

    public boolean isEntityCleanable(String world, String entity) {
        return cleanerConfig.get(world).containsKey(entity);
    }

    public int getBlockTime(String world, String block) {
        return cleanerConfig.get(world).get(block);
    }

    public int getEntityTime(String world, String entity) {
        return cleanerConfig.get(world).get(entity);
    }

    public void startCleaningTask() {
        new CleaningTask(this).runTaskTimer(plugin, 0, 20L);
    }

}
