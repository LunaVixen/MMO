package pixelfox.mmo.LevelListeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import pixelfox.mmo.PlayerData.MMOPlayer;
import pixelfox.mmo.PlayerData.PlayerData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrouchTorchListener implements Listener {

    private final Map<UUID, Long> cooldown = new HashMap<>();
    private static final long COOLDOWN_TIME = 10 * 1000;

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) {
            return;
        }

        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (isInCooldown(playerUUID)) {
            return;
        }

        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            return;
        }

        if (!isPickaxe(itemInHand)) {
            return;
        }

        String uuid = player.getUniqueId().toString();
        MMOPlayer mmoPlayer = PlayerData.loadPlayerData(uuid, player);

        if (mmoPlayer != null && mmoPlayer.getMiningLevel() >= 1) {
            summonTorch(player);
            cooldown.put(playerUUID, System.currentTimeMillis());
        }

    }

    private boolean isPickaxe(ItemStack item) {
        Material type = item.getType();
        return type == Material.WOODEN_PICKAXE || type == Material.STONE_PICKAXE || type == Material.IRON_PICKAXE
                || type == Material.GOLDEN_PICKAXE || type == Material.DIAMOND_PICKAXE || type == Material.NETHERITE_PICKAXE;

    }

    private void summonTorch(Player player) {
        player.getWorld().getBlockAt(player.getLocation()).setType(Material.TORCH);
    }

    private boolean isInCooldown(UUID playerUUID) {
        if (!cooldown.containsKey(playerUUID)) {
            return false;
        }
        long lastUsed = cooldown.get(playerUUID);
        return (System.currentTimeMillis() - lastUsed) < COOLDOWN_TIME;
    }

}
