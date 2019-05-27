package dev.djcook.bnnr.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CommandBnnr implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

//            Placeholder until we get permissions added
            if (player.getDisplayName().equals("DJ9123") || player.getDisplayName().equals("sprinklotad")) {
                if (args.length > 0) {

                    switch (args[0].toLowerCase()) {
                        case "giveflight":
                            if (args.length == 3) {
                                giveFlight(player, args);
                            } else {
                                player.sendMessage("Usage /banner giveFlight <username> <time (e.g. \"1\")>");
                            }
                            break;
                        case "removeflight":
                            if (args.length == 2) {
                                removeFlight(player, args);
                            } else {
                                player.sendMessage("Usage /banner removeFlight <username>");
                            }
                            break;
                        default:
                            player.sendMessage("Invalid command.");
                    }

                } else {
                    player.sendMessage("Usage /banner <command>");
                }

            } else {
                sender.sendMessage("You do not have permission to use this command.");
            }


        }


        return true;
    }

    private void giveFlight(Player sender, String[] args) {
        boolean isValid = isGiveFlightValid(sender, args);


        if (isValid) {
            String receiverName = args[1];
            Player receiver = Bukkit.getPlayer(receiverName);
            int flightMinutes = Integer.parseInt(args[2]);

            receiver.setAllowFlight(true);
            sender.sendMessage("Enabled flight for " + receiverName + ".");

            PotionEffect weakness = new PotionEffect(PotionEffectType.WEAKNESS, 1200 * flightMinutes - 600, 10, false, false, false);
            receiver.addPotionEffect(weakness);
            receiver.sendMessage("Flight enabled for " + flightMinutes + " minute(s).");
        }

    }

    private boolean isGiveFlightValid(Player sender, String[] args) {
        boolean isValid = false;
        String receiverName = args[1];
        Player receiver = Bukkit.getPlayer(receiverName);
        int flightMinutes = Integer.parseInt(args[2]);

        if (receiver == null) {
            sender.sendMessage("Player was not found.");
        } else if (receiver.getAllowFlight()) {
            sender.sendMessage("Player already has flight enabled.");
        } else if (flightMinutes < 0) {
            sender.sendMessage("Enter an integer greater than 0 for flight time.");
        } else {
            isValid = true;
        }

        return isValid;
    }

    private void removeFlight(Player sender, String[] args) {
        boolean isValid = isRemoveFlightValid(sender, args);

        if (isValid) {
            String receiverName = args[1];
            Player receiver = Bukkit.getPlayer(receiverName);

            receiver.setAllowFlight(false);
            sender.sendMessage("Disabled flight for " + receiverName + ".");
        }
    }

    private boolean isRemoveFlightValid(Player sender, String[] args) {
        boolean isValid = false;
        String receiverName = args[1];
        Player receiver = Bukkit.getPlayer(receiverName);

        if (receiver == null) {
            sender.sendMessage("Player was not found.");
        } else if (!receiver.getAllowFlight()) {
            sender.sendMessage("Player already has flight disabled.");
        }
        else {
            isValid = true;
        }

        return isValid;
    }


}
