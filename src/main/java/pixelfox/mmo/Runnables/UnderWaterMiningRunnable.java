package pixelfox.mmo.Runnables;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pixelfox.mmo.MMO;
import pixelfox.mmo.PlayerData.MMOPlayer;
import pixelfox.mmo.PlayerData.PlayerData;

public class UnderWaterMiningRunnable implements Runnable {

    private final JavaPlugin plugin;

    public UnderWaterMiningRunnable(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            MMOPlayer mmoPlayer = PlayerData.loadPlayerData(player.getUniqueId().toString(), player);

            if (mmoPlayer == null || mmoPlayer.getMiningLevel() < 8) {
                continue;
            }

            if (player.getLocation().getBlock().getType() == Material.WATER || player.getEyeLocation().getBlock().getType() == Material.WATER) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 100, 0, false, false, false));
            }
        }
    }

}
