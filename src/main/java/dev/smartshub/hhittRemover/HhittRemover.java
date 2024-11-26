package dev.smartshub.hhittRemover;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import dev.smartshub.hhittRemover.cleaner.CleanManager;
import dev.smartshub.hhittRemover.commands.MainCommand;
import dev.smartshub.hhittRemover.commands.MainCommandTabCompleter;
import dev.smartshub.hhittRemover.config.MainConfigManager;
import dev.smartshub.hhittRemover.listeners.BlockPlaceListener;
import dev.smartshub.hhittRemover.listeners.EntityPlaceListener;
import dev.smartshub.hhittRemover.utils.RemoveHelper;
import dev.smartshub.hhittRemover.worldguard.FlagManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class HhittRemover extends JavaPlugin {

    private MainConfigManager mainConfigManager;
    private CleanManager cleanManager;
    private RemoveHelper removeHelper;
    private final FlagManager flagManager = new FlagManager();

    @Override
    public void onLoad() {
        // Must register flags on load
        flagManager.registerFlags();
    }

    @Override
    public void onEnable() {

        // Check for WorldGuard, if it is not installed, then hhitt-Remover should be disabled
        WorldGuardPlugin wgPlugin = WorldGuardPlugin.inst();
        if (wgPlugin == null) {
            getLogger().warning("WorldGuard not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Plugin startup logic
        mainConfigManager = new MainConfigManager(this);
        mainConfigManager.loadConfig();
        cleanManager = new CleanManager(this);
        removeHelper = new RemoveHelper(this, cleanManager);

        // Register the event listeners
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(flagManager, cleanManager, wgPlugin, removeHelper), this);
        getServer().getPluginManager().registerEvents(new EntityPlaceListener(flagManager, cleanManager, wgPlugin, removeHelper), this);

        // Register the command
        getCommand("hhittremover").setExecutor(new MainCommand(this, cleanManager, mainConfigManager));
        getCommand("hhittremover").setTabCompleter(new MainCommandTabCompleter());

        // Check if the flag was registered
        if (!flagManager.isFlagRegistered()) {
            getLogger().warning("The custom flag 'hhitt-remover' could not be registered!");
        }

    }

}
