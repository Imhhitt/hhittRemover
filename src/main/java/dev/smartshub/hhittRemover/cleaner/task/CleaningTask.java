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
        // Using iterators to remove elements from the map while iterating

        Iterator<Map.Entry<Block, Integer>> blockIterator = cleanManager.getBlocksToClean().entrySet().iterator();

        while (blockIterator.hasNext()) {
            Map.Entry<Block, Integer> entry = blockIterator.next();
            Block block = entry.getKey();
            int time = entry.getValue();
            if (time <= 0) {
                block.getLocation().getBlock().setType(Material.AIR);
                blockIterator.remove();
                continue;
            }
            entry.setValue(time - 1);
        }

        Iterator<Map.Entry<Entity, Integer>> entityIterator = cleanManager.getEntitiesToClean().entrySet().iterator();

        while (entityIterator.hasNext()) {
            Map.Entry<Entity, Integer> entry = entityIterator.next();
            Entity entity = entry.getKey();
            int time = entry.getValue();
            if (time <= 0) {
                entity.remove();
                entityIterator.remove();
                continue;
            }
            entry.setValue(time - 1);
        }
    }
}


