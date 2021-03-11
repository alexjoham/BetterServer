package de.alexjoham.BetterServer;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.logging.Level;

public class setWorldSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof ConsoleCommandSender) {
            commandSender.getServer().getLogger().log(Level.SEVERE, "This command can only be executed by a player!");
            return false;
        }
        Player player = Objects.requireNonNull(commandSender.getServer().getPlayer(commandSender.getName()));
        if(FileManager.createFile("plugins"+File.separator+"BetterServer"+File.separator+"spawns.yml")) {
            commandSender.getServer().getLogger().log(Level.INFO, "file spawns.yml has been successfully created!");
        }
        try {
            FileManager.writeFile("plugins"+File.separator+"BetterServer"+File.separator+"spawns.yml", player.getWorld().getName()+"-spawn" ,player.getLocation().getX() + " " + player.getLocation().getY() + " " + player.getLocation().getZ() + " " + player.getLocation().getYaw() + " " + player.getLocation().getPitch());
        } catch (IOException e) {
            e.printStackTrace();
        }
        commandSender.sendMessage(ChatColor.RED + "Spawn for the world " + player.getWorld().getName() + " has been successfully created!");
        return true;
    }
}
