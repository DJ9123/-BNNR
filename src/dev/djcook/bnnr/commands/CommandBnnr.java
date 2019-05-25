package dev.djcook.bnnr.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBnnr implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {

            Bukkit.broadcastMessage("DisplayName: " + ((Player) sender).getDisplayName());
            Bukkit.broadcastMessage("CommandLabel: " + command.getLabel());
            Bukkit.broadcastMessage("Label: " + label);

            for (int i = 0; i < args.length; i++) {
                Bukkit.broadcastMessage("Args" + i + ": " + args[i]);
            }

        }


        return true;
    }

}
