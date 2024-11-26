package dev.smartshub.hhittRemover.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import dev.smartshub.hhittRemover.cleaner.CleanManager;
import dev.smartshub.hhittRemover.utils.RemoveHelper;
import dev.smartshub.hhittRemover.worldguard.FlagManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;

public class EntityPlaceListener implements Listener {


    private final FlagManager flagManager;
    private final CleanManager cleanManager;
    private final RemoveHelper removeHelper;
    private final WorldGuardPlugin wgPlugin;

    public EntityPlaceListener(FlagManager flagManager, CleanManager cleanManager, WorldGuardPlugin wgPlugin, RemoveHelper removeHelper){
        this.flagManager = flagManager;
        this.cleanManager = cleanManager;
        this.removeHelper = removeHelper;
        this.wgPlugin = wgPlugin;
    }

    @EventHandler
    public void onEntityPlace(EntityPlaceEvent event) {
        String worldName = event.getEntity().getLocation().getWorld().getName();
        Entity entity = event.getEntity();

        // If world is not listed in config, then we must return to avoid unnecessary checks
        if(!cleanManager.isWorldCleanable(worldName)) {
            return;
        }

        // Getting the WorldGuard location
        Location wgLocation = BukkitAdapter.adapt(entity.getLocation());

        // Obtaining the WorldGuard region container and querying the regions
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet regions = query.getApplicableRegions(wgLocation);

        // Checking if the region allows the custom flag
        Player player = event.getPlayer();
        LocalPlayer wgPlayer = wgPlugin.wrapPlayer(player);
        boolean hasCustomFlag = regions.testState(wgPlayer, flagManager.getHhittRemoverFlag());
        if (!hasCustomFlag) {
            return;
        }

        /* Check if the entity is contained at config for this world.
         * If it is not, then we must check if entity-place is limited to
         * entity from list to players, and if it is, cancel event. */
        if(!cleanManager.isEntityCleanable(worldName, entity.getType().name())){
            if(!removeHelper.isPlacingLimited()){
                return;
            }
            if(!player.hasPermission("hhittremover.bypass")) {
                return;
            }
            event.setCancelled(true);
        }

        // Finally remove the entity automatically after a certain amount of time
        removeHelper.removeEntity(entity);


    }
}
