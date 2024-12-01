package dev.smartshub.hhittRemover.listeners;

import dev.smartshub.hhittRemover.utils.ListenerHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;

public class EntityPlaceListener implements Listener {

    private final ListenerHelper listenerHelper;

    public EntityPlaceListener(ListenerHelper listenerHelper){
        this.listenerHelper = listenerHelper;
    }

    @EventHandler
    public void onEntityPlace(EntityPlaceEvent event) {
        String worldName = event.getBlock().getWorld().getName();
        Entity entity = event.getEntity();

        // If world is not listed in config, then we must return to avoid unnecessary checks
        if(!listenerHelper.isWorldCleanable(worldName)) {
            return;
        }

        // Gets the player
        Player player = event.getPlayer();

        // Checks the flag state
        boolean hasCustomFlag = listenerHelper.checkFlag(player, entity.getLocation());
        if (!hasCustomFlag) {
            return;
        }

        /* Check if the entity is contained at config for this world.
         * If it is not, then we must check if block-place is limited to
         * blocks from list to players, and if it is, cancel event. */
        if(listenerHelper.isEntityCleanable(worldName, entity.getType().name())) {
            listenerHelper.removeEntity(entity);
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
