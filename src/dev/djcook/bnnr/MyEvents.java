package dev.djcook.bnnr;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Logger;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class MyEvents implements Listener {

        @EventHandler
        public void mobDeath(EntityDeathEvent event) {
            Logger log = Bukkit.getLogger();
            LivingEntity entity = event.getEntity();

            if(entity instanceof Pillager || entity instanceof Evoker || entity instanceof Vindicator) {
                Material headEquipment = entity.getEquipment().getHelmet().getType();

//                Is Captain
                if (headEquipment.equals(Material.WHITE_BANNER)) {
                    int bannerIndex = -1;

//                    Find index of banner in entity drops
                    for (int i = 0; i < event.getDrops().size(); i++) {
                        if (event.getDrops().get(i).getType().equals(Material.WHITE_BANNER)) {
                            bannerIndex = i;
                        }
                    }

//                    Clone banner for renaming
                    ItemStack banner = event.getDrops().get(bannerIndex).clone();
                    ItemMeta bannerMeta = banner.getItemMeta();

//                    Rename clone
                    bannerMeta.setDisplayName(UUID.randomUUID().toString());
                    banner.setItemMeta(bannerMeta);

//                    Remove old banner
                    event.getDrops().remove(bannerIndex);

//                    Add new banner
                    event.getDrops().add(banner);

//                    Check if player killed captain
                    Player killer = event.getEntity().getKiller();
                    if (killer != null) {
                        log.info(killer.getDisplayName() + " killed a captain");
                        log.info("Created $BNNR with UUID: " + banner.getItemMeta().getDisplayName());
                        killer.sendMessage("You killed a captain! Created banner with ID: " + banner.getItemMeta().getDisplayName());
                    }

                }

            }
        }
    }
