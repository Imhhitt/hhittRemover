package dev.smartshub.hhittRemover.config;

import dev.smartshub.hhittRemover.HhittRemover;
import org.bukkit.configuration.file.FileConfiguration;

public class MainConfigManager {

    // Manejar la config.yml
    private final MainConfig configFile;


    // Cargar la configuración
    public MainConfigManager(HhittRemover plugin) {
        configFile = new MainConfig("config.yml", null, plugin);
        configFile.registerConfig();
        loadConfig();
    }

    // Cargar los mensajes de la config en variables
    public void loadConfig(){
        FileConfiguration configuration = configFile.getConfig();
    }

    public void reloadConfig(){
        configFile.reloadConfig();
        loadConfig();
    }


}