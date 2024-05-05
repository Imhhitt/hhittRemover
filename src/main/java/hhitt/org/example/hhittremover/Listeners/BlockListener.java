package hhitt.org.example.hhittremover.Listeners;

import hhitt.org.example.hhittremover.HhittRemover;
import hhitt.org.example.hhittremover.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (!Utils.canRemoveHere(block.getLocation()))
            return;
        Material material = block.getType();
        String worldName = Objects.requireNonNull(event.getBlock().getLocation().getWorld()).getName();
        if (!HhittRemover.getInstance().getConfig().contains("Worlds." + worldName))
            return;
        if (!HhittRemover.getInstance().getConfig().contains("Worlds." + worldName + ".Blocks." + material + ".Time"))
            return;
        List<String> materials = new ArrayList<>(Objects.requireNonNull(HhittRemover.getInstance().getConfig().getConfigurationSection(
                "Worlds." + worldName + ".Blocks")).getKeys(false));
        for (String mat : materials) {
            if (mat.equals(material.toString())) {
                int time = HhittRemover.getInstance().getConfig().getInt("Worlds." + worldName + ".Blocks." + material + ".Time");
                Utils.getBlocks().put(block.getLocation(), block);
                Bukkit.getScheduler().runTaskLater(HhittRemover.getInstance(), () -> Utils.removeBlock(block.getLocation()), 20L * time);
            }
        }
    }
}
