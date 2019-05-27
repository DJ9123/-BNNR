package games.oppressed.bnnr;

import games.oppressed.bnnr.commands.CommandBnnr;
import games.oppressed.bnnr.events.BnnrEvents;
import games.oppressed.bnnr.events.FlightEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
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
        registerCommands();
        registerEvents(this);
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

    private void registerCommands() {
        this.getCommand("bnnr").setExecutor(new CommandBnnr());
    }

    private void registerEvents(Plugin bnnr) {
        this.getServer().getPluginManager().registerEvents(new BnnrEvents(), bnnr);
        this.getServer().getPluginManager().registerEvents(new FlightEvents(bnnr), bnnr);
    }
}
