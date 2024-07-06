package pixelfox.mmo.PlayerData;

import org.bukkit.entity.Player;
import pixelfox.mmo.Utils.LevelingSystem;

public class MMOPlayer {
    private String uuid;
    private String name;
    private int miningLevel;
    private int miningExperience;
    private Player bukkitPlayer;

    public MMOPlayer(String uuid, String name, int miningLevel, int miningExperience, Player bukkitPlayer) {
        this.uuid = uuid;
        this.name = name;
        this.miningLevel = miningLevel;
        this.miningExperience = miningExperience;
        this.bukkitPlayer = bukkitPlayer;
    }

    // Another constructor for creating a new player without a Player object
    public MMOPlayer(String uuid, String name, int miningLevel, int miningExperience) {
        this(uuid, name, miningLevel, miningExperience, null);
    }

    // Getters and setters
    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getMiningLevel() {
        return miningLevel;
    }

    public int getMiningExperience() {
        return miningExperience;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public void setMiningLevel(int miningLevel) {
        this.miningLevel = miningLevel;
    }

    public void setMiningExperience(int miningExperience) {
        this.miningExperience = miningExperience;
    }

    public void setBukkitPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }

    // Add experience and level up if necessary
    public void addMiningExperience(int amount) {
        this.miningExperience += amount;
        while (this.miningExperience >= LevelingSystem.miningExp(this.miningLevel + 1)) {
            this.miningExperience -= LevelingSystem.miningExp(this.miningLevel + 1);
            this.miningLevel++;
            if (bukkitPlayer != null) {
                bukkitPlayer.sendMessage("§aCongratulations! You have reached Mining Level " + this.miningLevel + "!");
            }
        }
    }
    public int getExperienceNeededForNextLevel() {
        return LevelingSystem.getNextLevelExperience(this.miningLevel) - this.miningExperience;
    }

    public boolean hasReachLevel(int level) {
        return this.miningLevel >= level;
    }
}

