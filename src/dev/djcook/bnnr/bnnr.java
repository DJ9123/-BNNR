package dev.djcook.bnnr;

import datatypes.$BNNR;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

public class Bnnr extends JavaPlugin {

//    File Paths
    private static String mainDirectory;
    private static String bannersFile;

    @Override
    public void onEnable() {
        getLogger().info("howdy");

        setupFilePaths();

        this.getServer().getPluginManager().registerEvents(new MyEvents(), this);

    }

    @Override
    public void  onDisable() {

    }

    private void setupFilePaths() {
        mainDirectory = getDataFolder().getPath() + File.separator;
        bannersFile = mainDirectory + "banners.yml";
    }

    public static String getBannersFilePath() {
        return bannersFile;
    }
}
