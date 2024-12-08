package dev.smartshub.hhittRemover.cleaner.task;

import dev.smartshub.hhittRemover.cleaner.CleanManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;

public class CleaningTask extends BukkitRunnable {

    private final CleanManager cleanManager;

    public CleaningTask(CleanManager cleanManager) {
        this.cleanManager = cleanManager;
    }

    @Override
    public void run() {

        // Removing blocks
        Iterator<Map.Entry<Block, Integer>> blockIterator = cleanManager.getBlocksToClean().entrySet().iterator();
        while (blockIterator.hasNext()) {
            Map.Entry<Block, Integer> entry = blockIterator.next();
            Block block = entry.getKey();
            int remainingTime = entry.getValue() - 1;

            if (remainingTime <= 0) {
                block.getLocation().getBlock().setType(Material.AIR);
                cleanManager.removeBlockFromClean(block);
            } else {
                cleanManager.updateBlockTime(block, remainingTime);
            }
        }

        // Removing entities
        Iterator<Map.Entry<Entity, Integer>> entityIterator = cleanManager.getEntitiesToClean().entrySet().iterator();
        while (entityIterator.hasNext()) {
            Map.Entry<Entity, Integer> entry = entityIterator.next();
            Entity entity = entry.getKey();
            int remainingTime = entry.getValue() - 1;

            if (remainingTime <= 0) {
                entity.remove();
                cleanManager.removeEntityFromClean(entity);
            } else {
                cleanManager.updateEntityTime(entity, remainingTime);
            }
        }
    }
}
