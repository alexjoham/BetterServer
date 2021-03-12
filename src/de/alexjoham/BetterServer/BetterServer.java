package de.alexjoham.BetterServer;


import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Alexander Joham
 */
public class BetterServer extends JavaPlugin {


    public void onEnable() {
        loadConfigFile();
        getServer().getPluginManager().registerEvents(new JoinQuitMessage(this), this);
        getServer().getPluginManager().registerEvents(new SkipNightAtNumberOfSleepers(getServer(), this.getConfig().getString("playersNeededToSleep"), this), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        getCommand("setWorldSpawn").setExecutor(new setWorldSpawn());
        getServer().getConsoleSender().sendMessage("[BetterServer] " + ChatColor.BLUE + "plugin has been successfully enabled");
    }

    public void onDisable() {
        getServer().getConsoleSender().sendMessage("[BetterServer] " + ChatColor.BLUE + "plugin has been successfully disabled");
    }

    /**
     * Load the config file
     */
    private void loadConfigFile() {
        FileConfiguration configuration = this.getConfig();
        configuration.addDefault("defaultWelcomeMessage", "{playername} joined the game");
        configuration.addDefault("defaultWelcomeMessageColor", "YELLOW");
        configuration.addDefault("defaultQuitMessage", "{playername} left the game");
        configuration.addDefault("defaultQuitMessageColor", "YELLOW");
        configuration.addDefault("playersNeededToSleep", "MAX");
        configuration.options().copyDefaults(true);
        saveConfig();
    }
}
