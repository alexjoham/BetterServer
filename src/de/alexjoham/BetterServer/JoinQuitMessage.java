package de.alexjoham.BetterServer;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Alexander Joham
 */
public class JoinQuitMessage implements Listener {

    private final JavaPlugin plugin;

    /**
     * Constructor of JoinMessage
     * @param plugin Need for Config
     */
    public JoinQuitMessage(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(ChatColor.valueOf(plugin.getConfig().getString("defaultWelcomeMessageColor")) + getCastedMessage(e));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage(ChatColor.valueOf(plugin.getConfig().getString("defaultQuitMessageColor")) + getCastedMessage(e));
    }

    /**
     * This method searches for {} brackets and calls @code{getValueOf}
     * @param e PlayerJoinEvent
     * @return String message without {}-codes
     */
    public String getCastedMessage(PlayerJoinEvent e) {
        String message = plugin.getConfig().getString("defaultWelcomeMessage");
        return castString(message, e);
    }

    /**
     * This method searches for {} brackets and calls @code{getValueOf}
     * @param e PlayerJoinEvent
     * @return String message without {}-codes
     */
    public String getCastedMessage(PlayerQuitEvent e) {
        String message = plugin.getConfig().getString("defaultQuitMessage");
        return castString(message, e);
    }

    private String castString(String message, PlayerEvent e) {
        if (!message.contains("{")) //No code found
            return message;
        StringBuilder result = new StringBuilder();
        while (message.contains("{")) { //Still code in the String
            if (!message.contains("}")) { //No closing bracket found
                System.err.println("Missing } at message");
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

    /**
     * Returns the String which is represented by the code
     * @param code Input code
     * @param e PlayerJoinEvent
     * @return parsed code to String
     */
    public String getValueOf(String code, PlayerEvent e) {
        switch (code) {
            case "playername":
                return e.getPlayer().getName();
            default:
                return "Unknown code";
        }
    }


}
