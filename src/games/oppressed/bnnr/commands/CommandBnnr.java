package games.oppressed.bnnr.commands;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

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
                                player.sendMessage("Usage /bnnr giveFlight <username> <time (e.g. \"1\")>");
                            }
                            break;
                        case "removeflight":
                            if (args.length == 2) {
                                removeFlight(player, args);
                            } else {
                                player.sendMessage("Usage /bnnr removeFlight <username>");
                            }
                            break;
                        case "createbank":
                            if (args.length == 1) {
                                createBank(player, args);
                            } else {
                                player.sendMessage("Usage /bnnr createBank");
                            }
                            break;
                        case "givepotion":
                            if (args.length == 3) {
                                createPotion(player, args);
                            } else {
                                player.sendMessage("Usage /bnnr givepotion <username> <time (e.g.\"1\">");
                            }
                            break;
                        default:
                            player.sendMessage("Invalid command.");
                    }

                } else {
                    player.sendMessage("Usage /bnnr <command>");
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

            PotionEffect weakness = new PotionEffect(PotionEffectType.WEAKNESS, 1200 * flightMinutes - 600, 0, false, false, false);
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

    private void createBank(Player player, String[] args) {

    }

    private void createPotion(Player sender, String[] args) {
        boolean isValid = isCreatePotionValid(sender, args);

        if (isValid) {
            String receiverName = args[1];
            Player receiver = Bukkit.getPlayer(receiverName);
            int duration = Integer.parseInt(args[2]);

            ItemStack potion = new ItemStack(Material.POTION);
            PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
            potionMeta.clearCustomEffects();
            potionMeta.setColor(Color.AQUA);

            PotionEffect weakness = new PotionEffect(PotionEffectType.WEAKNESS, 1200 * duration - 600, 0, false, false, false);
            potionMeta.addCustomEffect(weakness, true);

            potionMeta.setDisplayName("Potion of Flight");

            potion.setItemMeta(potionMeta);

            receiver.getInventory().addItem(potion);
            receiver.sendMessage("Enjoy your " + duration + " minute potion!");
        }
    }

    private boolean isCreatePotionValid(Player sender, String[] args) {
        boolean isValid = false;
        String receiverName = args[1];
        Player receiver = Bukkit.getPlayer(receiverName);
        int duration = Integer.parseInt(args[2]);

        if (receiver == null) {
            sender.sendMessage("Player was not found.");
        } else if (duration < 0) {
            sender.sendMessage("Enter an integer greater than 0 for potion duration.");
        } else {
            isValid = true;
        }

        return isValid;
    }


}
