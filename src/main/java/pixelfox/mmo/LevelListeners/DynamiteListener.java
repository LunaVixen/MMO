package pixelfox.mmo.LevelListeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pixelfox.mmo.MMO;
import pixelfox.mmo.PlayerData.MMOPlayer;
import pixelfox.mmo.PlayerData.PlayerData;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DynamiteListener implements Listener {

    private final JavaPlugin plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final Map<UUID, Long> lastLeftClick = new HashMap<>();
    private final Map<UUID, Long> lastRightClick = new HashMap<>();

    private static final long COOLDOWN_TIME = 30 * 1000;
    private static final long DOUBLE_CLICK_THRESHOLD = 500;


    public DynamiteListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.LEFT_CLICK_AIR && event.getAction() == Action.LEFT_CLICK_BLOCK) {
            lastLeftClick.put(player.getUniqueId(), System.currentTimeMillis());
        } else if (event.getAction() == Action.RIGHT_CLICK_AIR && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            lastRightClick.put(player.getUniqueId(), System.currentTimeMillis());
        } else {
            return;
        }

        if (isDoubleClick(player) && isHoldingPickaxe(player)) {
            MMOPlayer mmoPlayer = PlayerData.loadPlayerData(player.getUniqueId().toString(), player);
            if (mmoPlayer == null || mmoPlayer.getMiningLevel() < 14) {
                return;
            }

            UUID playerId = player.getUniqueId();
            long currentTime = System.currentTimeMillis();

            cooldowns.put(playerId, currentTime);

            TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
            @NotNull Vector direction = player.getLocation().getDirection().multiply(1.5);
            tnt.setVelocity(direction);
            tnt.setFuseTicks(80);
        }

    }

    private boolean isDoubleClick(Player player) {
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (lastLeftClick.containsKey(playerId) && lastRightClick.containsKey(playerId)) {
            long leftClickTime = lastLeftClick.get(playerId);
            long rightClickTime = lastRightClick.get(playerId);

            if (Math.abs(leftClickTime - rightClickTime) <= DOUBLE_CLICK_THRESHOLD) {
                lastLeftClick.remove(playerId);
                lastRightClick.remove(playerId);
                return  true;
            }
        }
        return false;
    }

    private boolean isHoldingPickaxe(Player player){
        Material itemType = player.getInventory().getItemInMainHand().getType();
        return itemType == Material.WOODEN_PICKAXE || itemType == Material.STONE_PICKAXE || itemType == Material.IRON_PICKAXE ||
                itemType == Material.GOLDEN_PICKAXE || itemType == Material.DIAMOND_PICKAXE || itemType == Material.NETHERITE_PICKAXE;
    }


}
