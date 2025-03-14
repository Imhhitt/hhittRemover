package dev.smartshub.hhittRemover;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import dev.smartshub.hhittRemover.cleaner.CleanManager;
import dev.smartshub.hhittRemover.command.MainCommand;
import dev.smartshub.hhittRemover.command.MainCommandTabCompleter;
import dev.smartshub.hhittRemover.config.MainConfigManager;
import dev.smartshub.hhittRemover.hook.bstats.Metrics;
import dev.smartshub.hhittRemover.listener.BlockPlaceListener;
import dev.smartshub.hhittRemover.listener.EntityPlaceListener;
import dev.smartshub.hhittRemover.listener.PlayerBucketEmptyListener;
import dev.smartshub.hhittRemover.util.ListenerHelper;
import dev.smartshub.hhittRemover.util.Remove;
import dev.smartshub.hhittRemover.hook.worldguard.FlagManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class HhittRemover extends JavaPlugin {

    private MainConfigManager mainConfigManager;
    private CleanManager cleanManager;
    private Remove removeHelper;
    private final FlagManager flagManager = new FlagManager();
    private WorldGuardPlugin wgPlugin = WorldGuardPlugin.inst();
    private ListenerHelper listenerHelper;

    @Override
    public void onLoad() {
        // Must register flags on load
        flagManager.registerFlags();
    }

    @Override
    public void onEnable() {

        // Check for WorldGuard, if it is not installed, then hhitt-Remover should be disabled
        wgDependencyCheck();

        // Initialize the managers
        initManagers();

        // Register the event listeners
        registerListeners();

        // Register the command
        registerCommands();

        // Hook with bStats
        loadBStats();
    }


    private void wgDependencyCheck(){
        if (wgPlugin == null) {
            getLogger().warning("WorldGuard not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Check if the flag was registered
        if (!flagManager.isFlagRegistered()) {
            getLogger().warning("The custom flag 'hhitt-remover' could not be registered!");
        }
    }

    private void initManagers(){
        mainConfigManager = new MainConfigManager(this);
        mainConfigManager.loadConfig();
        cleanManager = new CleanManager(this);
        removeHelper = new Remove(this, cleanManager);
        listenerHelper = new ListenerHelper(flagManager, cleanManager, wgPlugin, removeHelper);
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(listenerHelper), this);
        getServer().getPluginManager().registerEvents(new EntityPlaceListener(listenerHelper), this);
        getServer().getPluginManager().registerEvents(new PlayerBucketEmptyListener(listenerHelper), this);
    }

    private void registerCommands(){
        getCommand("hhittremover").setExecutor(new MainCommand(this, cleanManager, mainConfigManager));
        getCommand("hhittremover").setTabCompleter(new MainCommandTabCompleter());
    }

    private void loadBStats(){
        int pluginId = 22314;
        Metrics metrics = new Metrics(this, pluginId);
    }

}
