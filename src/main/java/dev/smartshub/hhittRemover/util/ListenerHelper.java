package dev.smartshub.hhittRemover.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import dev.smartshub.hhittRemover.cleaner.CleanManager;
import dev.smartshub.hhittRemover.hook.worldguard.FlagManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ListenerHelper {

    private final FlagManager flagManager;
    private final CleanManager cleanManager;
    private final Remove remove;
    private final WorldGuardPlugin wgPlugin;

    public ListenerHelper(FlagManager flagManager, CleanManager cleanManager, WorldGuardPlugin wgPlugin, Remove remmoveUtils) {
        this.flagManager = flagManager;
        this.cleanManager = cleanManager;
        this.remove = remmoveUtils;
        this.wgPlugin = wgPlugin;
    }


    /**
     * Check if the flag is registered.
     *
     * @return true if the flag is registered.
     */
    public boolean checkFlag(Player player, Location location) {
        com.sk89q.worldedit.util.Location wgLocation = new com.sk89q.worldedit.util.Location(
                BukkitAdapter.adapt(location.getWorld()),
                location.getX(),
                location.getY(),
                location.getZ()
        );

        // Obtaining the WorldGuard region container and querying the regions
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet regions = query.getApplicableRegions(wgLocation);

        // Checking if the region allows the custom flag
        LocalPlayer wgPlayer = wgPlugin.wrapPlayer(player);
        return regions.testState(wgPlayer, flagManager.getHhittRemoverFlag());
    }

    public boolean  isBlockCleanable(String worldName, String blockName){
        return cleanManager.isBlockCleanable(worldName, blockName);
    }

    public boolean isEntityCleanable(String worldName, String entityName){
        return cleanManager.isEntityCleanable(worldName, entityName);
    }

    public boolean isPlacingAllowed(){
       return remove.isPlacingLimited();
    }

    public void removeBlock(Block block){
        remove.removeBlock(block);
    }

    public void removeEntity(Entity entity){
        remove.removeEntity(entity);
    }

    public boolean isWorldCleanable(String worldName){
        return cleanManager.isWorldCleanable(worldName);
    }

}
