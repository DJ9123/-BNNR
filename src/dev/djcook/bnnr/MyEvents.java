package dev.djcook.bnnr;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Vindicator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class MyEvents implements Listener {

        @EventHandler
        public void mobDeath(EntityDeathEvent event) {
            LivingEntity entity = event.getEntity();

            if(entity instanceof Pillager || entity instanceof Evoker || entity instanceof Vindicator) {
                Material headEquipment = entity.getEquipment().getHelmet().getType();

                if (headEquipment.equals(Material.WHITE_BANNER)) {
                    Bukkit.broadcastMessage("Killed a captain!");
                    int bannerIndex = -1;

//                    It can be a different index if they drop a crossbow -DJC
                    for (int i = 0; i < event.getDrops().size(); i++) {
                        if (event.getDrops().get(i).getType().equals(Material.WHITE_BANNER)) {
                            bannerIndex = i;
                        }
                    }

                    ItemStack banner = event.getDrops().get(bannerIndex).clone();
                    ItemMeta bannerMeta = banner.getItemMeta();

                    bannerMeta.setDisplayName(UUID.randomUUID().toString());
                    banner.setItemMeta(bannerMeta);

                    event.getDrops().remove(bannerIndex);

                    Bukkit.broadcastMessage("Created banner with name: " + banner.getItemMeta().getDisplayName());

                    event.getDrops().add(banner);
                }

            }
        }

    }

