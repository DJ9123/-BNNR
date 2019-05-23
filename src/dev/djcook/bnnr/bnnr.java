package dev.djcook.bnnr;

import org.bukkit.plugin.java.JavaPlugin;

public class bnnr extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("howdy");
        this.getServer().getPluginManager().registerEvents(new MyEvents(), this);

    }

    @Override
    public void  onDisable() {


    }
}
