package de.alexjoham.BetterServer;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class DeathEvent implements Listener {

    private final boolean canRespawnInBed;
    public DeathEvent(boolean canRespawnInBed) {
        this.canRespawnInBed = canRespawnInBed;
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        //TODO: Add something to change that
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (!canRespawnInBed || e.getPlayer().getBedSpawnLocation() == null) {
            HashMap<String, String> values = new HashMap<>();
            try {
                FileManager.readFile("plugins" + File.separator + "BetterServer" + File.separator + "spawns.yml").forEach(entry -> {
                    int pos = entry.indexOf(":");
                    values.put(entry.substring(0, pos), entry.substring(pos + 2));
                });
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            if (values.get(e.getPlayer().getWorld().getName() + "-spawn") != null) {
                String location = values.get(e.getPlayer().getWorld().getName() + "-spawn");
                double x = Double.parseDouble(location.substring(0, location.indexOf(" ") - 1));
                location = location.substring(location.indexOf(" ") + 1);
                double y = Double.parseDouble(location.substring(0, location.indexOf(" ") - 1));
                location = location.substring(location.indexOf(" ") + 1);
                double z = Double.parseDouble(location.substring(0, location.indexOf(" ") - 1));
                location = location.substring(location.indexOf(" ") + 1);
                float yaw = Float.parseFloat(location.substring(0, location.indexOf(" ") - 1));
                location = location.substring(location.indexOf(" ") + 1);
                float pitch = Float.parseFloat(location);
                Location l = new Location(e.getPlayer().getWorld(), x, y, z, yaw, pitch);
                e.setRespawnLocation(l);
            }
        }
    }
}
