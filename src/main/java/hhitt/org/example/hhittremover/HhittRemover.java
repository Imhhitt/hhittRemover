package hhitt.org.example.hhittremover;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import hhitt.org.example.hhittremover.Configuration.ReloadConfigCommand;
import hhitt.org.example.hhittremover.Configuration.WorldConfiguration;
import hhitt.org.example.hhittremover.Listeners.BlockListener;
import hhitt.org.example.hhittremover.Listeners.Entitylistener;
import hhitt.org.example.hhittremover.Utils.Utils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class HhittRemover extends JavaPlugin {
    private static HhittRemover instance;

    public static StateFlag CUSTOM_FLAG;

    public static HashMap<String, WorldConfiguration> configs = new HashMap<>();

    public void onLoad() {
        super.onLoad();
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("hhitt-remover", false);
            registry.register(flag);
            CUSTOM_FLAG = flag;
        } catch (FlagConflictException exception) {
            Flag<?> flag = registry.get("hhitt-remover");
            if (flag instanceof StateFlag)
                CUSTOM_FLAG = (StateFlag)flag;
        }
    }

    public void onEnable() {
        super.onEnable();
        instance = this;
        saveDefaultConfig();
        registerListener((Listener)new BlockListener(), (Listener)new Entitylistener());
        sendHeader();
        send("&dhhitt-Remover started working.");
        Objects.requireNonNull(getCommand("hhittremover-reload")).setExecutor(new ReloadConfigCommand(this));
    }

    public void onDisable() {
        super.onDisable();
        for (Location location : Utils.getBlocks().keySet())
            Utils.removeBlock(location);
        for (Entity entity : Utils.getEntities().values())
            Utils.removeEntity(entity);
    }

    public void sendHeader() {
        send("");
    }

    public static void send(String message) {
        Bukkit.getConsoleSender().sendMessage(Utils.colorize(message));
    }

    public void registerListener(Listener... listeners) {
        Arrays.<Listener>stream(listeners).forEach(listener -> getInstance().getServer().getPluginManager().registerEvents(listener, (Plugin)getInstance()));
    }

    public static HhittRemover getInstance() {
        return instance;
    }
}
