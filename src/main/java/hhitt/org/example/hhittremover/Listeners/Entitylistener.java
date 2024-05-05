package hhitt.org.example.hhittremover.Listeners;

import org.bukkit.event.Listener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.plugin.Plugin;
import hhitt.org.example.hhittremover.HhittRemover;
import hhitt.org.example.hhittremover.Utils.Utils;

public class Entitylistener implements Listener {
    @EventHandler
    public void onEntitySpawn(EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (!Utils.canRemoveHere(event.getEntity().getLocation()))
            return;
        String worldName = ((World)Objects.<World>requireNonNull(event.getEntity().getLocation().getWorld())).getName();
        if (!HhittRemover.getInstance().getConfig().contains("Worlds." + worldName))
            return;
        if (!HhittRemover.getInstance().getConfig().contains("Worlds." + worldName + ".Entities." + entity.getType().toString()))
            return;
        List<String> entities = new ArrayList<>(((ConfigurationSection)Objects.<ConfigurationSection>requireNonNull(HhittRemover.getInstance().getConfig().getConfigurationSection("Worlds." + worldName + ".Entities"))).getKeys(false));
        for (String ent : entities) {
            if (ent.equals(entity.getType().toString())) {
                int time = HhittRemover.getInstance().getConfig().getInt("Worlds." + worldName + ".Entities." + ent + ".Time");
                Utils.getEntities().put(entity.getLocation(), entity);
                Bukkit.getScheduler().runTaskLater((Plugin)HhittRemover.getInstance(), () -> Utils.removeEntity(entity), 20L * time);
            }
        }
    }
}
