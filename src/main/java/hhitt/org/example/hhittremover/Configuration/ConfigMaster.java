package hhitt.org.example.hhittremover.Configuration;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigMaster {

    private YamlConfiguration configuration;
    private String reloadMessage;

    private File file;

    private boolean firstTime = false;

    public ConfigMaster(String configurationName, String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            if (!file.mkdirs())
                return;
            this.file = new File(dir, configurationName + ".yml");
            if (!this.file.exists()) {
                this.firstTime = true;
                try {
                    if (!this.file.createNewFile())
                        return;
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            this.configuration = YamlConfiguration.loadConfiguration(this.file);
        }

        reloadMessage = configuration.getString("Messages.Reload");
    }

    public void reload() {
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void save() {
        try {
            this.configuration.save(this.file);
            reload();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public YamlConfiguration getConfiguration() {
        return this.configuration;
    }

    public boolean isFirstTime() {
        return this.firstTime;
    }

    public String getReloadMessage(){
        return reloadMessage;
    }
}
