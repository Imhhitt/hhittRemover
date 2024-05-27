package hhitt.org.example.hhittremover.Listeners;

import hhitt.org.example.hhittremover.HhittRemover;
import hhitt.org.example.hhittremover.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        handleBlockPlace(event.getPlayer(), block, block.getType());
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());
        Player player = event.getPlayer();
        Material material = event.getBucket() == Material.WATER_BUCKET ? Material.WATER : (event.getBucket() == Material.LAVA_BUCKET ? Material.LAVA : null);
        if (material != null) {
            handleBlockPlace(player, block, material);
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Block block = event.getToBlock();
        Material material = event.getBlock().getType();
        if (material == Material.WATER || material == Material.LAVA) {
            handleBlockPlace(null, block, material);
        }
    }

    private void handleBlockPlace(Player player, Block block, Material material) {
        if (!Utils.canRemoveHere(block.getLocation()))
            return;
        String worldName = Objects.requireNonNull(block.getLocation().getWorld()).getName();
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
