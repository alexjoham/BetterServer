package de.alexjoham.ServerPlugin;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinMessage implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(ChatColor.GREEN + "Wilkommen zurück, " + e.getPlayer().getName() + "!");
    }
}
