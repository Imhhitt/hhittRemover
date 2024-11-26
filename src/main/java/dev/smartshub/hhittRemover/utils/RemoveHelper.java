package dev.smartshub.hhittRemover.utils;

import dev.smartshub.hhittRemover.HhittRemover;
import dev.smartshub.hhittRemover.cleaner.CleanManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;


public class RemoveHelper {

    private final HhittRemover plugin;
    private final CleanManager cleanManager;

    public RemoveHelper(HhittRemover plugin, CleanManager cleanManager) {
        this.plugin = plugin;
        this.cleanManager = cleanManager;
    }

    public boolean isPlacingLimited() {
        return plugin.getConfig().getBoolean("flag-limit-placing");
    }

    // Delete a block after a certain amount of time
    public void removeBlock(Block block) {
        int delayInSeconds = cleanManager.getBlockTime(block.getLocation().getWorld().getName(), block.getType().name());
        new BukkitRunnable() {
            @Override
            public void run() {
                block.getLocation().getBlock().setType(Material.AIR);
            }
        }.runTaskLater(plugin, delayInSeconds * 20L);
    }

    // Remove an entity after a certain amount of time
    public void removeEntity(Entity entity) {
        int delayInSeconds = cleanManager.getEntityTime(entity.getWorld().getName(), entity.getType().name());
        new BukkitRunnable() {
            @Override
            public void run() {
                entity.remove();
            }
        }.runTaskLater(plugin, delayInSeconds * 20L);
    }
}