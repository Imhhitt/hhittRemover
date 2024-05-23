package hhitt.org.example.hhittremover.Utils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import hhitt.org.example.hhittremover.HhittRemover;
import java.util.HashMap;
import java.util.Objects;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;


public class Utils {
    private static final HashMap<Location, Block> blocks = new HashMap<>();

    private static final HashMap<Location, Entity> entities = new HashMap<>();

    public static HashMap<Location, Block> getBlocks() {
        return blocks;
    }

    public static HashMap<Location, Entity> getEntities() {
        return entities;
    }

    public static void removeBlock(Location location) {
        blocks.remove(location);
        if (!canRemoveHere(location))
            return;
        if (location.getBlock().getType() == Material.AIR)
            return;
        Objects.requireNonNull(location.getWorld()).getBlockAt(location).setType(Material.AIR);
    }

    public static void removeEntity(Entity entity) {
        entities.remove(entity.getLocation());
        if (!canRemoveHere(
                entity.getLocation()) || entity.isDead())
            return;
        entity.remove();
    }

    public static boolean canRemoveHere(Location location) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(location));
        return set.testState(null, HhittRemover.CUSTOM_FLAG);
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
