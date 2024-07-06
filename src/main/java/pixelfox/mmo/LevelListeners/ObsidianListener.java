package pixelfox.mmo.LevelListeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pixelfox.mmo.MMO;
import pixelfox.mmo.PlayerData.MMOPlayer;
import pixelfox.mmo.PlayerData.PlayerData;

import java.util.List;

public class ObsidianListener implements Listener {

    private final JavaPlugin plugin;

    public ObsidianListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.OBSIDIAN) {
            return;
        }

        MMOPlayer mmoPlayer = PlayerData.loadPlayerData(player.getUniqueId().toString(), player);
        if (mmoPlayer == null || mmoPlayer.getMiningLevel() < 13) {
            return;
        }

        event.setCancelled(true);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (block.getType() == Material.OBSIDIAN) {
                block.breakNaturally(player.getInventory().getItemInMainHand());

            }
        }, 50L);
    }
}
