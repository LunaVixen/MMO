package pixelfox.mmo.LevelListeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import pixelfox.mmo.PlayerData.MMOPlayer;
import pixelfox.mmo.PlayerData.PlayerData;

public class SaturationListener implements Listener {

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        MMOPlayer mmoPlayer = PlayerData.loadPlayerData(player.getUniqueId().toString(), player);

        if (mmoPlayer == null || mmoPlayer.getMiningLevel() < 12) {
            return;
        }

        double currentSaturation = player.getSaturation();
        double newSaturation = currentSaturation * 1.25;
        player.setSaturation((float) newSaturation);
    }
}
