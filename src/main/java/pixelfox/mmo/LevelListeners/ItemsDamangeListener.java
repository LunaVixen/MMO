package pixelfox.mmo.LevelListeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import pixelfox.mmo.PlayerData.MMOPlayer;
import pixelfox.mmo.PlayerData.PlayerData;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ItemsDamangeListener implements Listener {
    private final Random random = new Random();

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!isPickaxe(item.getType())) {
            return;
        }

        MMOPlayer mmoPlayer = PlayerData.loadPlayerData(player.getUniqueId().toString(), player);

        if (mmoPlayer == null) {
            return;
        }

        int miningLevel = mmoPlayer.getMiningLevel();
        int chance = getDurabilityChance(miningLevel);

        if (chance > 0 && random.nextInt(100) < chance) {
            event.setCancelled(true);
        }
    }

    private boolean isPickaxe(Material type) {
        return type == Material.WOODEN_PICKAXE || type == Material.STONE_PICKAXE || type == Material.IRON_PICKAXE ||
                type == Material.GOLDEN_PICKAXE || type == Material.DIAMOND_PICKAXE || type == Material.NETHERITE_PICKAXE ||
                type == Material.WOODEN_SHOVEL || type == Material.STONE_SHOVEL || type == Material.IRON_SHOVEL ||
                type == Material.GOLDEN_SHOVEL || type == Material.DIAMOND_SHOVEL || type == Material.NETHERITE_SHOVEL;
    }

    private int getDurabilityChance(int miningLevel) {
        if (miningLevel >= 25) {
            return 100;
        } else if (miningLevel >= 21) {
            return 75;
        } else if (miningLevel >= 17) {
            return 50;
        } else if (miningLevel >= 7) {
            return 25;
        }
        return 0;
    }

}
