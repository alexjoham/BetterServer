package de.alexjoham.ServerPlugin;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinMessage implements Listener {

    private String message;
    private ChatColor color;

    /**
     * Constructor of JoinMessage
     * @param message default message
     * @param color ChatColor of the message
     */
    public JoinMessage(String message, String color) {
        this.message = message;
        this.color = ChatColor.valueOf(color);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(color + getCastedMessage(e));
    }

    /**
     * This method searches for {} brackets and calls @code{getValueOf}
     * @param e PlayerJoinEvent needed for playername
     * @return String message without {}-codes
     */
    public String getCastedMessage(PlayerJoinEvent e) {
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
    public String getValueOf(String code, PlayerJoinEvent e) {
        switch (code) {
            case "playername":
                return e.getPlayer().getName();
            default:
                return "Unknown code";
        }
    }
}
