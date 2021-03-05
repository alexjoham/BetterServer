package de.alexjoham.ServerPlugin;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinMessage implements Listener {

    private String message;
    private ChatColor color;

    public JoinMessage(String message, String color) {
        this.message = message;
        this.color = ChatColor.valueOf(color);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(color + getCastedMessage(e));
    }

    public String getCastedMessage(PlayerJoinEvent e) {
        if (!message.contains("{"))
            return message;
        StringBuilder result = new StringBuilder();
        while (message.contains("{")) {
            if (!message.contains("}")) {
                break;
            }
            result.append(message, 0, message.indexOf("{")).append(getValueOf(message.substring(message.indexOf("{") + 1, message.indexOf("}")), e));
            message = message.substring(message.indexOf("}") + 1);
            if (message.length() == 0)
                break;
        }
        result.append(message);
        return result.toString();
    }

    public String getValueOf(String code, PlayerJoinEvent e) {
        switch (code) {
            case "playername":
                return e.getPlayer().getName();
            default:
                return "Unknown code";
        }
    }
}
