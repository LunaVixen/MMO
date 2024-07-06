package pixelfox.mmo.Commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pixelfox.mmo.PlayerData.MMOPlayer;
import pixelfox.mmo.PlayerData.PlayerData;

import java.util.Random;

public class CaveCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public CaveCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by player.");
            return true;
        }

        Player player = (Player) sender;
        MMOPlayer mmoPlayer = PlayerData.loadPlayerData(player.getUniqueId().toString(), player);

        if (mmoPlayer == null || mmoPlayer.getMiningLevel() < 11) {
            //player.sendMessage("You need to be at least level 11 in mining to use this command.");
            return true;
        }

        Location caveLocation = findNearbyCave(player.getLocation(), 100, 50);
        if (caveLocation != null) {
            player.teleport(caveLocation);
            player.sendMessage("The Cave Gremlin has bestowed a cave location");
        } else {
            player.sendMessage("The Cave Gremlin is keeping the caves hidden you");
        }

        return true;
    }

    private Location findNearbyCave(Location startLocation, int radius, int depth) {
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(radius * 2) - radius;
            int z = random.nextInt(radius * 2) - radius;
            int y = random.nextInt(depth);

            Location testLocation = startLocation.clone().add(x, -y, z);
            if (isCave(testLocation)) {
                return testLocation;
            }
        }

        return null;
    }

    private boolean isCave(Location location) {
        Material type = location.getBlock().getType();
        return type == Material.AIR || type == Material.CAVE_AIR;
    }

}
