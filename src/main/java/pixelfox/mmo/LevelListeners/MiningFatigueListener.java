package pixelfox.mmo.LevelListeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import pixelfox.mmo.PlayerData.MMOPlayer;
import pixelfox.mmo.PlayerData.PlayerData;


public class MiningFatigueListener implements Listener {

    @EventHandler
    public void onPlayerPotioneffect(EntityPotionEffectEvent event) {
        if (event.getAction() != EntityPotionEffectEvent.Action.ADDED) {
            return;
        }

        if (event.getModifiedType() != PotionEffectType.MINING_FATIGUE) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        MMOPlayer mmoPlayer = PlayerData.loadPlayerData(player.getUniqueId().toString(), player);

        if (mmoPlayer == null) {
            return;
        }
        if (mmoPlayer.getMiningLevel() >= 4) {
            event.setCancelled(true);
        }
    }

}
