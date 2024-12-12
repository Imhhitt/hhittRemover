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
        Iterator<Map.Entry<Block, Long>> blockIterator = cleanManager.getBlocksToClean().entrySet().iterator();
        while (blockIterator.hasNext()) {
            Map.Entry<Block, Long> entry = blockIterator.next();
            if (System.currentTimeMillis() >= entry.getValue()) {
                Block block = entry.getKey();
                block.setType(Material.AIR);
                blockIterator.remove();
            }
        }

        // Removing entities
        Iterator<Map.Entry<Entity, Long>> entityIterator = cleanManager.getEntitiesToClean().entrySet().iterator();
        while (entityIterator.hasNext()) {
            Map.Entry<Entity, Long> entry = entityIterator.next();
            if (System.currentTimeMillis() >= entry.getValue()) {
                Entity entity = entry.getKey();
                entity.remove();
                entityIterator.remove();
            }
        }
    }
}




