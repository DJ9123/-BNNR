package dev.djcook.bnnr;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Bnnr extends JavaPlugin {

//    File Paths
    private static String mainDirectory;
    private static String bannersFile;

    private Logger log = Bukkit.getLogger();

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
        bannersFile = mainDirectory + "ValidBanners.yml";

        if (!getDataFolder().exists()) {
            try {
                log.info("Data Folder not found. Generating files..");
                getDataFolder().mkdir();
                new File(getDataFolder(), "ValidBanners.yml").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getBannersFilePath() {
        return bannersFile;
    }
}
