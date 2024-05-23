package hhitt.org.example.hhittremover.Listeners;

import hhitt.org.example.hhittremover.HhittRemover;
import hhitt.org.example.hhittremover.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import java.util.Objects;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        if (!Utils.canRemoveHere(location)) {
            return;
        }

        Material material = block.getType();
        String worldName = Objects.requireNonNull(location.getWorld()).getName();

        String worldPath = "Worlds." + worldName;
        String materialPath = worldPath + ".Blocks." + material + ".Time";

        HhittRemover instance = HhittRemover.getInstance();
        if (!instance.getConfig().contains(worldPath) || !instance.getConfig().contains(materialPath)) {
            return;
        }

        int time = instance.getConfig().getInt(materialPath);
        Utils.getBlocks().put(location, block);
        Bukkit.getScheduler().runTaskLater(instance, () -> Utils.removeBlock(location), 20L * time);
    }
}