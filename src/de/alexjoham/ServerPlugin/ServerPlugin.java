package de.alexjoham.ServerPlugin;


import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public class ServerPlugin extends JavaPlugin {


    public void onEnable() {
        loadConfigFile();
        getServer().getPluginManager().registerEvents(new JoinMessage(this.getConfig().getString("defaultWelcomeMessage"), this.getConfig().getString("defaultWelcomeMessageColor")), this);
        getServer().getPluginManager().registerEvents(new SkipNightAtNumberOfSleepers(getServer(), this.getConfig().getString("playerNeededToSleep")), this);
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
        configuration.addDefault("defaultWelcomeMessage", "Welcome, {playername}!");
        configuration.addDefault("defaultWelcomeMessageColor", "GREEN");
        configuration.addDefault("playerNeededToSleep", "MAX");
        configuration.options().copyDefaults(true);
        saveConfig();
    }
}
