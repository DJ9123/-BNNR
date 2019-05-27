package dev.djcook.bnnr;

import datatypes.$BNNR;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Logger;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class MyEvents implements Listener {

    ValidBannerManager bannerManager = new ValidBannerManager();
    Plugin plugin;

    public MyEvents(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void potionEffect(EntityPotionEffectEvent event) {
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
    public void mobDeath(EntityDeathEvent event) {
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
