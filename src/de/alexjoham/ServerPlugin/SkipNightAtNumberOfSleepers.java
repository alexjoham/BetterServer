package de.alexjoham.ServerPlugin;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.*;

public class SkipNightAtNumberOfSleepers implements Listener {

    private Server server;
    private String playerNeededToSleep;
    private HashMap<World, List<Player>> sleepingPlayers;

    public SkipNightAtNumberOfSleepers(Server server, String playerNeededToSleep) {
        this.server = server;
        this.playerNeededToSleep = playerNeededToSleep;
        sleepingPlayers = new HashMap<>();
    }

    @EventHandler
    public void onPlayerLyingDown(PlayerBedEnterEvent e) {

        World world = e.getPlayer().getWorld();
        if (e.getBedEnterResult().compareTo(PlayerBedEnterEvent.BedEnterResult.OK) == 0) {
            List<Player> list = sleepingPlayers.get(e.getPlayer().getWorld());
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(e.getPlayer());
            sleepingPlayers.put(e.getPlayer().getWorld(), list);
        }
        if (playerNeededToSleep.equals("MAX")) {
            if (sleepingPlayers.get(world).size() == server.getOnlinePlayers().stream().filter(entry -> !world.getName().contains("_nether")).toArray().length)
                e.getPlayer().getWorld().setTime(1000);
        } else if (playerNeededToSleep.contains("%")) {
            int percent = 0;
            try {
                percent = Integer.parseInt(playerNeededToSleep.substring(0, playerNeededToSleep.length() - 2));
            } catch (NumberFormatException exception) {
                server.getLogger().logp(Level.SEVERE, this.toString(), "onPlayerLyingDown(PlayerBedEnterEvent e)", "Integer excpected, but was: " + playerNeededToSleep.substring(0, playerNeededToSleep.length() - 2), new Throwable(exception.getCause()));
            }
            numberIsNull(e, percent);
        } else {
            int number = 0;
            try {
                number = Integer.parseInt(playerNeededToSleep);
            } catch (NumberFormatException exception) {
                server.getLogger().logp(Level.SEVERE, this.toString(), "onPlayerLyingDown(PlayerBedEnterEvent e)", "Integer excpected, but was: " + playerNeededToSleep.substring(0, playerNeededToSleep.length() - 2), new Throwable(exception.getCause()));
            }
            numberIsNull(e, number);
        }
    }

    private void numberIsNull(PlayerBedEnterEvent e, int number) {
        World world = e.getPlayer().getWorld();
        if (number != 0) {
            int sleeping = e.getPlayer().getWorld().getPlayers().stream().filter(OfflinePlayer::isOnline).toArray().length / sleepingPlayers.get(e.getPlayer().getWorld()).size();
            if (sleeping >= number) {
                e.getPlayer().getWorld().setTime(1000);
            }
        } else {
            if (sleepingPlayers.get(world).size() == server.getOnlinePlayers().stream().filter(entry -> !entry.getPlayer().getWorld().getName().contains("_nether")).toArray().length)
                e.getPlayer().getWorld().setTime(1000);
        }
    }

    @EventHandler
    public void onPlayerStandingUp(PlayerBedLeaveEvent e) {
        List<Player> list;
        list = sleepingPlayers.get(e.getPlayer().getWorld());
        list.remove(e.getPlayer());
        sleepingPlayers.put(e.getPlayer().getWorld(), list);
    }
}