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
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    private final FlagManager flagManager;
    private final CleanManager cleanManager;
    private final RemoveHelper removeHelper;
    private final WorldGuardPlugin wgPlugin;

    public BlockPlaceListener(FlagManager flagManager, CleanManager cleanManager, WorldGuardPlugin wgPlugin, RemoveHelper removeHelper){
        this.flagManager = flagManager;
        this.cleanManager = cleanManager;
        this.removeHelper = removeHelper;
        this.wgPlugin = wgPlugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        String worldName = event.getBlock().getWorld().getName();
        Block block = event.getBlock();

        // If world is not listed in config, then we must return to avoid unnecessary checks
        if(!cleanManager.isWorldCleanable(worldName)) {
            return;
        }

        // Getting the WorldGuard location
        Location wgLocation = new Location(
                BukkitAdapter.adapt(block.getWorld()),
                block.getX(),
                block.getY(),
                block.getZ()
        );

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

        /* Check if the block is contained at config for this world.
         * If it is not, then we must check if block-place is limited to
         * blocks from list to players, and if it is, cancel event. */
        if(!cleanManager.isBlockCleanable(worldName, block.getType().name())){
            if(!removeHelper.isPlacingLimited()){
                return;
            }
            if(!player.hasPermission("hhittremover.bypass")) {
                return;
            }
            event.setCancelled(true);
        }

        // Finally remove the block automatically after a certain amount of time
        removeHelper.removeBlock(block);

    }

}

