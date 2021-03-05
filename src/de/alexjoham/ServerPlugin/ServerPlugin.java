package de.alexjoham.ServerPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ServerPlugin extends JavaPlugin {


    public void onEnable() {
        loadConfigFile();
        JoinMessage joinMessage = new JoinMessage(this.getConfig().getString("defaultWelcomeMessage"), this.getConfig().getString("defaultWelcomeMessageColor"));
        getServer().getPluginManager().registerEvents(joinMessage, this);
    }

    public void onDisable() {

    }

    /**
     * Load the config file
     */
    private void loadConfigFile() {
        FileConfiguration configuration = this.getConfig();
        configuration.addDefault("defaultWelcomeMessage", "Welcome, {playername}!");
        configuration.addDefault("defaultWelcomeMessageColor", "GREEN");
        configuration.options().copyDefaults(true);
        saveConfig();
    }
}
