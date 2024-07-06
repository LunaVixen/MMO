package pixelfox.mmo.Runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pixelfox.mmo.PlayerData.MMOPlayer;
import pixelfox.mmo.PlayerData.PlayerData;

public class NightVisionRunnable implements Runnable {
    private final JavaPlugin plugin;
    public NightVisionRunnable(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        //plugin.getLogger().info("NightVisionRunnable is Running");
        for (Player player: Bukkit.getOnlinePlayers()) {
            MMOPlayer mmoPlayer = PlayerData.loadPlayerData(player.getUniqueId().toString(), player);

            if (mmoPlayer == null) {
                plugin.getLogger().info("Player data is null for player: " + player.getName());
                continue;
            }

            if (mmoPlayer == null || mmoPlayer.getMiningLevel() < 9) {
                continue;
            }

            if (player.getLocation().getY() < 30) {
                //plugin.getLogger().info("Player " + player.getName() + " is below Y level 30. Applying Night Vision.");
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 1, false, false, false));
            }
        }
    }
}
