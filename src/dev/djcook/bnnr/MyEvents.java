package dev.djcook.bnnr;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class MyEvents implements Listener {

    @EventHandler
    public void mobDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if(entity instanceof Zombie) {
            event.getDrops().add(new ItemStack(Material.DIAMOND,1));
            //entity.getLocation().getWorld().dropItem(entity.getLocation(), new ItemStack(Material.DIAMOND));
        }
    }

    }

