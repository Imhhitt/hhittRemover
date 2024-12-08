package dev.smartshub.hhittRemover.utils;

import dev.smartshub.hhittRemover.HhittRemover;
import dev.smartshub.hhittRemover.cleaner.CleanManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;


public class RemoveUtils {

    private final HhittRemover plugin;
    private final CleanManager cleanManager;

    public RemoveUtils(HhittRemover plugin, CleanManager cleanManager) {
        this.plugin = plugin;
        this.cleanManager = cleanManager;
    }

    /**
     * Check if the plugin is limiting which blocks can be placed
     * @return true if is enabled, false otherwise
     */
    public boolean isPlacingLimited() {
        return plugin.getConfig().getBoolean("flag-limit-placing");
    }


    /**
     * Remove a block after a certain amount of time
     */
    public void removeBlock(Block block) {
        cleanManager.addBlockToClean(block);
    }


    /**
     * Remove an entity after a certain amount of time
     */
    public void removeEntity(Entity entity) {
        cleanManager.addEntityToClean(entity);
    }
}
