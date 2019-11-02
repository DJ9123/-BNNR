package games.oppressed.bnnr.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

public class FlightEvents implements Listener {
    Plugin plugin;
    Logger log = Bukkit.getLogger();

    public FlightEvents(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void checkFlightOnPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Iterator<PotionEffect> iterator = player.getActivePotionEffects().iterator();

        boolean hasFlightEffect = false;
        boolean hasSlowFall = false;

        while (iterator.hasNext()) {
            PotionEffect potionEffect = iterator.next();
            if (potionEffect.getType().equals(PotionEffectType.WEAKNESS) && potionEffect.getAmplifier() == 0) {
                hasFlightEffect = true;
            } else if (potionEffect.getType().equals(PotionEffectType.SLOW_FALLING)) {
                hasSlowFall = true;
            }
        }

        if (hasSlowFall && !hasFlightEffect) {
            player.teleport(getHighestLocationBelowPlayer(player.getLocation()));
            player.removePotionEffect(PotionEffectType.SLOW_FALLING);
            log.info("Removed SLOW_FALLING from: " + player.getDisplayName());
        }
    }

    private Location getHighestLocationBelowPlayer(Location location) {
        Location downOneY = location.subtract(0.0, 1.0, 0.0);

        if (downOneY.getBlock().getType().equals(Material.AIR)) {
            return getHighestLocationBelowPlayer(downOneY);
        } else {
            return location.add(0.0, 1.0, 0.0);
        }
    }

    @EventHandler
    public void checkFlightOnPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Collection<PotionEffect> effects = player.getActivePotionEffects();
        Iterator<PotionEffect> iterator = effects.iterator();

        boolean hasFlightEffect = false;
        boolean hasSlowFall = false;
        int flightDuration = 0;

        while (iterator.hasNext()) {
            PotionEffect potionEffect = iterator.next();
            if (potionEffect.getType().equals(PotionEffectType.WEAKNESS) && potionEffect.getAmplifier() == 0) {
                flightDuration = potionEffect.getDuration() / 20;
                hasFlightEffect = true;
            } else if (potionEffect.getType().equals(PotionEffectType.SLOW_FALLING)) {
                hasSlowFall = true;
            }
        }

        if (hasFlightEffect) {
            flightDuration += hasSlowFall ? 0 : 30;

            int minutes = flightDuration / 60;
            int seconds = flightDuration % 60;
            String remainingTime = "Flight enabled for ";

            if (minutes > 0 && seconds > 0) {
                remainingTime += minutes + " minute(s) and " + seconds + " second(s).";
            } else if (minutes > 0) {
                remainingTime += minutes + " minute(s)";
            } else {
                remainingTime += seconds + " second(s)";
            }

            player.sendMessage(remainingTime);
            player.setAllowFlight(true);
        }

    }

    @EventHandler
    public void removeFlightPotionEvent(EntityPotionEffectEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();
        EntityPotionEffectEvent.Action action = event.getAction();

        if (entity instanceof Player && (action == EntityPotionEffectEvent.Action.CLEARED || action == EntityPotionEffectEvent.Action.REMOVED) && event.getModifiedType().getName().equals("WEAKNESS")) {
            Player player = (Player) entity;

            String receiverName = player.getDisplayName();
            Player receiver = Bukkit.getPlayer(receiverName);

//            If been alive longer than minimum flight time
            if (receiver.getTicksLived() >= 1200 && action == EntityPotionEffectEvent.Action.REMOVED) {

                Collection<PotionEffect> effects = receiver.getActivePotionEffects();
                Iterator<PotionEffect> iterator = effects.iterator();
                boolean hasSlowFall = false;

                while (iterator.hasNext()) {
                    if (iterator.next().getType().equals(PotionEffectType.SLOW_FALLING)) {
                        hasSlowFall = true;
                    }
                }

                if (!hasSlowFall) {
//                    Send warning and add slow fall
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            PotionEffect slowFall = new PotionEffect(PotionEffectType.SLOW_FALLING, 1200, 1);
                            receiver.addPotionEffect(slowFall);
                            PotionEffect weakness = new PotionEffect(PotionEffectType.WEAKNESS, 600, 0, false, false, false);
                            receiver.addPotionEffect(weakness);
                            receiver.sendMessage(String.format("You have 30 seconds of flight time remaining. You have been given the slow fall effect for 1 minute."));
                        }
                    }, 1L);

                } else {
//                    Disable flight
                    receiver.sendMessage("Flight has been disabled. Hope you land safely :)");
                    receiver.setAllowFlight(false);
                }


            } else {
//                Died recently or cleared the effect
                receiver.setAllowFlight(false);
            }
        }
    }
}
