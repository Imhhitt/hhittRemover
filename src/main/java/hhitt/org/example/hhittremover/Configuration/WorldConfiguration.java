package hhitt.org.example.hhittremover.Configuration;

import hhitt.org.example.hhittremover.HhittRemover;
import org.bukkit.configuration.file.YamlConfiguration;

public class WorldConfiguration extends ConfigMaster {
    public WorldConfiguration(String configurationName) {
        super(configurationName, "" + HhittRemover.getInstance().getDataFolder() + "/Worlds");
        if (isFirstTime()) {
            YamlConfiguration configuration = getConfiguration();
            configuration.addDefault("Blocks.EXAMPLE.Time", Integer.valueOf(10));
            configuration.addDefault("Entities.EXAMPLE.Time", Integer.valueOf(10));
            configuration.options().copyDefaults(true);
            save();
        }
    }
}
