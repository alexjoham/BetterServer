package de.alexjoham.ServerPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public class ServerPlugin extends JavaPlugin {


    public void onEnable() {
        getServer().getPluginManager().registerEvents(new JoinMessage(), this);
    }

    public void onDisable() {

    }
}
