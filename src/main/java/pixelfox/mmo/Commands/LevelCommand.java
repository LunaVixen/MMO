package pixelfox.mmo.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pixelfox.mmo.PlayerData.MMOPlayer;
import pixelfox.mmo.PlayerData.PlayerData;

public class LevelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("mmo.admin")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length !=1) {
            player.sendMessage("Usage: /level <level>");
            return true;
        }

        int newLevel;
        try {
            newLevel = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid level. Please enter a number.");
            return true;
        }

        String uuid = player.getUniqueId().toString();
        MMOPlayer mmoPlayer = PlayerData.loadPlayerData(uuid, player);

        if (mmoPlayer == null) {
            player.sendMessage("Could not load player data.");
            return true;
        }

        mmoPlayer.setMiningLevel(newLevel);
        PlayerData.savePlayerData(mmoPlayer);

        player.sendMessage("Your mining level has been set to " + newLevel + ".");
        return true;
    }

}
