package pixelfox.mmo;

import org.bukkit.plugin.java.JavaPlugin;
import pixelfox.mmo.Commands.CaveCommand;
import pixelfox.mmo.Commands.LevelCommand;
import pixelfox.mmo.Commands.StatsCommand;
import pixelfox.mmo.LevelListeners.*;
import pixelfox.mmo.Runnables.NightVisionRunnable;
import pixelfox.mmo.Runnables.UnderWaterMiningRunnable;
import pixelfox.mmo.Utils.DatabaseManager;

public class MMO extends JavaPlugin {
    private static MMO instance;

    @Override
    public void onEnable() {
        instance = this;

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        initializeDatabase();
        registerListeners();
        registerCommands();
        startTasks();

        getLogger().info("MMO plugin enabled successfully.");
    }

    @Override
    public void onDisable() {
        getLogger().info("MMO plugin disabled.");
    }

    private void initializeDatabase() {
        try {
            DatabaseManager.initialize();
            getLogger().info("Database initialized successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to initialize database: " + e.getMessage());
        }
    }

    private void registerListeners() {
        try {
            getServer().getPluginManager().registerEvents(new MiningListener(), this);
            getLogger().info("MiningListener loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to load MiningListener: " + e.getMessage());
        }

        try {
            getServer().getPluginManager().registerEvents(new CrouchTorchListener(), this);
            getLogger().info("CrouchTorchListener loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to load CrouchTorchListener: " + e.getMessage());
        }

        try {
            getServer().getPluginManager().registerEvents(new ItemsDamangeListener(), this);
            getLogger().info("ItemDamageListener loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to load ItemDamageListener: " + e.getMessage());
        }

        try {
            getServer().getPluginManager().registerEvents(new BoreListener(), this);
            getLogger().info("BoreListener loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to load BoreListener: " + e.getMessage());
        }

        try {
            getServer().getPluginManager().registerEvents(new CrouchMineListener(), this);
            getLogger().info("CrouchMineListener loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to load CrouchMineListener: " + e.getMessage());
        }

        try {
            getServer().getPluginManager().registerEvents(new MiningFatigueListener(), this);
            getLogger().info("MiningFatigueListener loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to load MiningFatigueListener: " + e.getMessage());
        }
        try {
            getServer().getPluginManager().registerEvents(new SaturationListener(), this);
            getLogger().info("SaturationListener loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to load SaturationListener: " + e.getMessage());
        }
        try {
            getServer().getPluginManager().registerEvents(new ObsidianListener(this), this);
            getLogger().info("ObsidianListener loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to load ObsidianListener: " + e.getMessage());
        }
        try {
            getServer().getPluginManager().registerEvents(new DynamiteListener(this), this);
            getLogger().info("DynamiteListener loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to load DynamiteListener: " + e.getMessage());
        }
    }

    private void registerCommands() {
        try {
            this.getCommand("level").setExecutor(new LevelCommand());
            getLogger().info("LevelCommand loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to load LevelCommand: " + e.getMessage());
        }

        try {
            this.getCommand("stats").setExecutor(new StatsCommand());
            getLogger().info("StatsCommand loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to load StatsCommand: " + e.getMessage());
        }
        try {
            this.getCommand("cave").setExecutor(new CaveCommand(this));
            getLogger().info("CaveCommand loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to load CaveCommand: " + e.getMessage());
        }
    }

    private void startTasks() {
        getServer().getScheduler().runTaskTimer(this, new UnderWaterMiningRunnable(this), 0L, 100L);
        getServer().getScheduler().runTaskTimer(this, new NightVisionRunnable(this), 0L, 100L);
        getLogger().info("NightVisionRunnable and UnderWaterMiningTask tasks started successfully");
    }

    public static MMO getInstance() {
        return instance;
    }
}