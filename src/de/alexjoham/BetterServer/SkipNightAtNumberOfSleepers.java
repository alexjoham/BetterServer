package de.alexjoham.BetterServer;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.*;

/**
 * @author Alexander Joham
 */
public class SkipNightAtNumberOfSleepers implements Listener {


    private final Server server;

    //Players needed to sleep to change the time to day
    private final String playerNeededToSleep;

    //All sleeping players on the whole server
    private final HashMap<World, List<Player>> sleepingPlayers;

    //Needed to check if countdown is already running
    private boolean enoughPlayers;

    //Needed for the setDay method
    private PlayerBedEnterEvent e;

    //Plugin for runTaskLater()
    private final Plugin plugin;

    //Runnable variable to cancel it when a player gets up to early
    private BukkitRunnable countdown;

    //Number of players that are more than needed to change the time to day
    private int morePlayersThanNeeded;

    /**
     * @param server server the plugin runs on
     * @param playerNeededToSleep String which contains the number of players need to sleep (or percentage)
     * @param plugin this plugin
     */
    public SkipNightAtNumberOfSleepers(Server server, String playerNeededToSleep, Plugin plugin) {
        this.server = server;
        this.playerNeededToSleep = playerNeededToSleep;
        sleepingPlayers = new HashMap<>();
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLyingDown(PlayerBedEnterEvent event) {

        this.e = event;

        World world = e.getPlayer().getWorld();

        if (e.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)) { //Player can sleep

            List<Player> list = sleepingPlayers.get(e.getPlayer().getWorld()); //list of all players that are currently sleeping in this world

            if (list == null) { //this is the first time someone lies down
                list = new ArrayList<>();
            }

            list.add(e.getPlayer()); //player is sleeping

            sleepingPlayers.put(e.getPlayer().getWorld(), list); //add the sleeping players to the hashmap

            if (playerNeededToSleep.equals("MAX")) { //MAX -> no changes all players need to sleep
                numberIsNull();
            } else if (playerNeededToSleep.contains("%")) { //in the config is a percentage given

                int percent = 0;

                try {
                    percent = Integer.parseInt(playerNeededToSleep.substring(0, playerNeededToSleep.length() - 1)); //get the number that is in the config
                } catch (NumberFormatException exception) {
                    //no number is before the %
                    server.getLogger().logp(Level.SEVERE, this.toString(), "onPlayerLyingDown(PlayerBedEnterEvent e)", "Integer before % was expected, but was: " + playerNeededToSleep.substring(0, playerNeededToSleep.length() - 1), new Throwable(exception.getCause()));
                }

                if (percent == 0 || percent > 100) { //0% or >100% means MAX
                    numberIsNull();
                } else {
                    //get the percentage of sleeping players
                    float sleeping = ((float) sleepingPlayers.get(e.getPlayer().getWorld()).size()) / ((float) e.getPlayer().getWorld().getPlayers().stream().filter(OfflinePlayer::isOnline).toArray().length);
                    if (sleeping * 100 >= percent) {
                        setDay(world); //enough players are sleeping
                    }
                }

            } else { //a number is the third option you can use in the config

                int number = 0;

                try {
                    number = Integer.parseInt(playerNeededToSleep);
                } catch (NumberFormatException exception) {
                    server.getLogger().logp(Level.SEVERE, this.toString(), "onPlayerLyingDown(PlayerBedEnterEvent e)", "Integer expected, but was: " + playerNeededToSleep.substring(0, playerNeededToSleep.length() - 2), new Throwable(exception.getCause())); //no number is given
                }

                if (number == 0) //0 -> MAX
                    numberIsNull();
                else if (number > e.getPlayer().getWorld().getPlayers().stream().filter(OfflinePlayer::isOnline).toArray().length) { //number > online players
                    numberIsNull();
                } else {

                    int sleeping = sleepingPlayers.get(e.getPlayer().getWorld()).size(); //get sleeping players

                    if (sleeping >= number) {
                        setDay(world); //enough players are sleeping
                    }
                }
            }
        }
    }

    /**
     * Player gets up -> needs to be removed from the HashMap
     * @param event
     */
    @EventHandler
    public void onPlayerGettingUp(PlayerBedLeaveEvent event) {

        sleepingPlayers.get(event.getPlayer().getWorld()).remove(event.getPlayer());

        if(enoughPlayers) {

            if(morePlayersThanNeeded == 0) {

                countdown.cancel(); //Not enough players are sleeping -> cancel time change

                enoughPlayers = false;
            }
            else //More players are sleeping than needed
                morePlayersThanNeeded--;
        }
    }

    /**
     * method that checks if all players are sleeping and changes the time to day if all players are sleeping
     */
    private void numberIsNull() {

        World world = e.getPlayer().getWorld();

        if (sleepingPlayers.get(world).size() == server.getOnlinePlayers().stream().filter(entry -> !Objects.requireNonNull(entry.getPlayer()).getWorld().getName().contains("_nether")).toArray().length) {
            setDay(world);
        }
    }

    /**
     * methods sets time to day after ~4.5 seconds
     *
     * @param world needed to send a message to all players in the world where the time has been changed
     */
    private void setDay(World world) {

        if (!enoughPlayers) {

            enoughPlayers = true;

            countdown = new BukkitRunnable() {

                @Override
                public void run() {
                    setDay();
                    enoughPlayers = false;
                    world.getPlayers().forEach(player -> player.sendMessage("Enough players have slept. Good morning!")); //message to all players that are currently in this world
                }
            };
            countdown.runTaskLater(plugin, 90);//delay -> server ticks (20 server ticks = 1s)
        } else { //Already enough players are sleeping
            morePlayersThanNeeded++;
        }
    }

    /**
     * Set the time to day
     */
    private void setDay() {
        e.getPlayer().getWorld().setTime(1000);
        if(e.getPlayer().getWorld().hasStorm()) { //Normally minecraft changes the weather while sleeping
            e.getPlayer().getWorld().setStorm(false); //But the player doesnt really sleep so the plugin has to change the weather
        }
    }


}
