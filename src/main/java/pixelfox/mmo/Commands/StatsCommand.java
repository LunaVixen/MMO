package pixelfox.mmo.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pixelfox.mmo.PlayerData.MMOPlayer;
import pixelfox.mmo.PlayerData.PlayerData;
import pixelfox.mmo.Utils.LevelingSystem;

public class StatsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();
        MMOPlayer mmoPlayer = PlayerData.loadPlayerData(uuid, player);

        if (mmoPlayer == null) {
            player.sendMessage("Could not load player Data.");
            return true;
        }
        int currentLevel = mmoPlayer.getMiningLevel();
        int currentXP = mmoPlayer.getMiningExperience();
        int xpNeeded = mmoPlayer.getExperienceNeededForNextLevel();

        player.sendMessage("§7§lMining Stats:");
        player.sendMessage("§7Current Level: " + currentLevel);
        player.sendMessage("§7Current XP: " + currentXP);
        player.sendMessage("§7XP Needed for Next Level: " + xpNeeded);

        return true;
    }
}
