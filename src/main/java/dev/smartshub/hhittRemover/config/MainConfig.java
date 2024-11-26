package dev.smartshub.hhittRemover.config;

import dev.smartshub.hhittRemover.HhittRemover;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MainConfig {

    private final HhittRemover plugin;
    private final String fileName;
    private FileConfiguration fileConfiguration = null;
    private File file = null;
    private final String folderName;

    public MainConfig(String fileName, String folderName, HhittRemover plugin){
        this.fileName = fileName;
        this.folderName = folderName;
        this.plugin = plugin;
    }

    public void registerConfig(){
        if(folderName != null){
            file = new File(plugin.getDataFolder() +File.separator + folderName, fileName);
        }else{
            file = new File(plugin.getDataFolder(), fileName);
        }

        if(!file.exists()){
            if(folderName != null){
                plugin.saveResource(folderName+File.separator+fileName, false);
            }else{
                plugin.saveResource(fileName, false);
            }
        }

        fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            reloadConfig();
        }
        return fileConfiguration;
    }

    public void reloadConfig() {
        if (fileConfiguration == null) {
            if(folderName != null){
                file = new File(plugin.getDataFolder() +File.separator + folderName, fileName);
            }else{
                file = new File(plugin.getDataFolder(), fileName);
            }

        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);

        if(file != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(file);
            fileConfiguration.setDefaults(defConfig);
        }
    }
}

