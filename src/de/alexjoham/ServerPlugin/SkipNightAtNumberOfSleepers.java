package de.alexjoham.ServerPlugin;

import org.apache.logging.log4j.Level;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class SkipNightAtNumberOfSleepers implements Listener {

    private Server server;
    private String playerNeededToSleep;

    public SkipNightAtNumberOfSleepers(Server server, String playerNeededToSleep) {
        this.server = server;
        this.playerNeededToSleep = playerNeededToSleep;
    }

    private int counter = 0;

    @EventHandler
    public void onPlayerLyingDown(PlayerBedEnterEvent e) {
        if (e.getBedEnterResult().compareTo(PlayerBedEnterEvent.BedEnterResult.OK) == 0)
            counter++;
        if (playerNeededToSleep.equals("MAX")) {
            if (counter == server.getOnlinePlayers().stream().filter(entry -> !entry.getPlayer().getWorld().getName().contains("_nether")).toArray().length)
                e.getPlayer().getWorld().setTime(1000);
        } else if(playerNeededToSleep.contains("%")) {
            int percent = 0;
            try {
                percent = Integer.parseInt(playerNeededToSleep.substring(0, playerNeededToSleep.length()-2));
            } catch(NumberFormatException exception) {
                //server.getLogger().logp(Level.ERROR,);
            }
        }
    }
}
