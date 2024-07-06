package pixelfox.mmo.LevelListeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import pixelfox.mmo.MMO;
import pixelfox.mmo.PlayerData.MMOPlayer;
import pixelfox.mmo.PlayerData.PlayerData;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

public class CrouchMineListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (!player.isSneaking() || !isPickaxe(itemInHand.getType())) {
            return;
        }

        MMOPlayer mmoPlayer = PlayerData.loadPlayerData(player.getUniqueId().toString(), player);

        if (mmoPlayer == null || mmoPlayer.getMiningLevel() < 6) {
            return;
        }

        mine3x3(player, mmoPlayer, event.getBlock());
    }

    private boolean isPickaxe(Material type) {
        return type == Material.WOODEN_PICKAXE || type == Material.STONE_PICKAXE || type == Material.IRON_PICKAXE ||
                type == Material.GOLDEN_PICKAXE || type == Material.DIAMOND_PICKAXE || type == Material.NETHERITE_PICKAXE;
    }

    private void mine3x3(Player player, MMOPlayer mmoPlayer, Block centerBlock) {
        Set<Block> minedBlocks = new HashSet<>();
        BlockFace face = getFacingDirection(player);

        for (int y = -1; y <= 1; y++) { // Mine 3 blocks tall centered on the mined block
            for (int offset = -1; offset <= 1; offset++) { // Mine 3 blocks wide centered on the mined block
                Block block;
                if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
                    block = centerBlock.getRelative(offset, y, 0);
                } else {
                    block = centerBlock.getRelative(0, y, offset);
                }
                if (block.getType() != Material.BEDROCK && block.getType() != Material.AIR) {
                    block.breakNaturally(player.getInventory().getItemInMainHand());
                    minedBlocks.add(block);
                }
            }
        }

        // Add experience to the player for the blocks broken
        mmoPlayer.addMiningExperience(minedBlocks.size());
        PlayerData.savePlayerData(mmoPlayer);


    }

    private BlockFace getFacingDirection(Player player) {
        float yaw = player.getLocation().getYaw();
        if (yaw < 0) {
            yaw += 360;
        }
        yaw %= 360;

        if (yaw >= 45 && yaw < 135) {
            return BlockFace.EAST;
        } else if (yaw >= 135 && yaw < 225) {
            return BlockFace.SOUTH;
        } else if (yaw >= 225 && yaw < 315) {
            return BlockFace.WEST;
        } else {
            return BlockFace.NORTH;
        }
    }
}