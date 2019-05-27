package dev.djcook.bnnr;

import datatypes.$BNNR;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Logger;

public class MyEvents implements Listener {

    ValidBannerManager bannerManager = new ValidBannerManager();
    Plugin plugin;

    public MyEvents(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void checkFlightOnPlayerJoinEvent(PlayerJoinEvent event) {
        Logger log = Bukkit.getLogger();
        Player player = event.getPlayer();

        Collection<PotionEffect> effects = player.getActivePotionEffects();
        Iterator<PotionEffect> iterator = effects.iterator();

        boolean hasFlightEffect = false;
        boolean hasSlowFall = false;
        int flightDuration = 0;

        while (iterator.hasNext()) {
            PotionEffect potionEffect = iterator.next();
            if (potionEffect.getType().equals(PotionEffectType.WEAKNESS) && potionEffect.getAmplifier() == 10) {
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
        Logger log = Bukkit.getLogger();
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
                            PotionEffect weakness = new PotionEffect(PotionEffectType.WEAKNESS, 600, 10, false, false, false);
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

    @EventHandler
    public void killCaptainEvent(EntityDeathEvent event) {
        Logger log = Bukkit.getLogger();
        LivingEntity entity = event.getEntity();

        if(entity instanceof Pillager || entity instanceof Evoker || entity instanceof Vindicator) {
            Material headEquipment = entity.getEquipment().getHelmet().getType();

//            Is Captain
            if (headEquipment.equals(Material.WHITE_BANNER)) {
                int bannerIndex = -1;

//                Find index of banner in entity drops
                for (int i = 0; i < event.getDrops().size(); i++) {
                    if (event.getDrops().get(i).getType().equals(Material.WHITE_BANNER)) {
                        bannerIndex = i;
                    }
                }

//                Clone banner for renaming
                ItemStack banner = event.getDrops().get(bannerIndex).clone();
                ItemMeta bannerMeta = banner.getItemMeta();

//                Rename clone
                bannerMeta.setDisplayName(UUID.randomUUID().toString());
                banner.setItemMeta(bannerMeta);

//                Remove old banner
                event.getDrops().remove(bannerIndex);

//                Add new banner
                event.getDrops().add(banner);

                $BNNR bannerEntry = new $BNNR();
                bannerEntry.BannerID = UUID.fromString(banner.getItemMeta().getDisplayName());

//                Check if player killed captain
                Player killer = event.getEntity().getKiller();
                if (killer != null) {
                    log.info(killer.getDisplayName() + " killed a captain");
                    log.info("Created $BNNR with UUID: " + banner.getItemMeta().getDisplayName());
                    bannerEntry.OriginalOwnerName = killer.getDisplayName();
                    bannerEntry.OriginalOwnerID = killer.getUniqueId();

                    killer.sendMessage("You killed a captain! Created banner with ID: " + banner.getItemMeta().getDisplayName());
                }

                bannerManager.newBanner(bannerEntry);

            }
        }
    }
}
