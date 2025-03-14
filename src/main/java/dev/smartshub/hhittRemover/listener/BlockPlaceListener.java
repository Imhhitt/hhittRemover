package dev.smartshub.hhittRemover.listener;

import dev.smartshub.hhittRemover.util.ListenerHelper;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {


    private final ListenerHelper listenerHelper;

    public BlockPlaceListener(ListenerHelper listenerHelper){
        this.listenerHelper = listenerHelper;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        String worldName = event.getBlock().getWorld().getName();
        Block block = event.getBlock();

        // If world is not listed in config, then we must return to avoid unnecessary checks
        if(!listenerHelper.isWorldCleanable(worldName)) {
            return;
        }

        // Gets the player
        Player player = event.getPlayer();

        // Checks the flag state
        boolean hasCustomFlag = listenerHelper.checkFlag(player, block.getLocation());
        if (!hasCustomFlag) {
            return;
        }

        /* Check if the block is contained at config for this world.
         * If it is not, then we must check if block-place is limited to
         * blocks from list to players, and if it is, cancel event. */
        if(listenerHelper.isBlockCleanable(worldName, block.getType().name())) {
            listenerHelper.removeBlock(block);
            return;
        }

        if(!listenerHelper.isPlacingAllowed()){
            return;
        }

        if(!player.hasPermission("hhittremover.bypass")){
            event.setCancelled(true);
        }

    }

}

