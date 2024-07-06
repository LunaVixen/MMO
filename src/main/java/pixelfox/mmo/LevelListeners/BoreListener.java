package pixelfox.mmo.LevelListeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.spawner.SpawnerEntry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import pixelfox.mmo.PlayerData.MMOPlayer;
import pixelfox.mmo.PlayerData.PlayerData;

import java.util.*;

public class BoreListener implements Listener {

    private static final long COOLDOWN_TIME = 15 * 1000;
    private final Map<UUID, Long> cooldown = new HashMap<>();
    private static final ThreadLocal<Boolean> isBoreOperation = ThreadLocal.withInitial(() -> false);
    private static final Set<Block> markedBlocks = new HashSet<>();

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (!isPickaxe(itemInHand.getType())) {
            return;
        }

        MMOPlayer mmoPlayer = PlayerData.loadPlayerData(player.getUniqueId().toString(), player);

        if (mmoPlayer == null || mmoPlayer.getMiningLevel() < 5) {
            return;
        }

        if (isInCooldown(player.getUniqueId())) {
            player.sendMessage("§cYou must wait before using this ability again.");
            return;
        }

        isBoreOperation.set(true);
        try {
            bore(player, mmoPlayer);
        } finally {
            isBoreOperation.set(false);
        }
        cooldown.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private boolean isPickaxe(Material type) {
        return type == Material.WOODEN_PICKAXE || type == Material.STONE_PICKAXE || type == Material.IRON_PICKAXE ||
                type == Material.GOLDEN_PICKAXE || type == Material.DIAMOND_PICKAXE || type == Material.NETHERITE_PICKAXE;
    }

    private boolean isInCooldown(UUID playerUUID) {
        return cooldown.containsKey(playerUUID) && (System.currentTimeMillis() - cooldown.get(playerUUID)) < COOLDOWN_TIME;
    }

    private void bore(Player player, MMOPlayer mmoPlayer) {
        BlockFace face = getFacingDirection(player);
        Block startBlock = player.getLocation().getBlock().getRelative(face.getOppositeFace(), 1); // Start one block in front of the player

        int boreLength = 6; // Default bore length
        if (mmoPlayer.getMiningLevel() >= 23) {
            boreLength = 16;
        } else if (mmoPlayer.getMiningLevel() >= 19) {
            boreLength = 12;
        } else if (mmoPlayer.getMiningLevel() >= 16) {
            boreLength = 9;
        }

        int blocksBroken = 0;
        for (int z = 0; z < boreLength; z++) { // Mine based on bore length
            for (int y = 0; y <= 2; y++) { // Mine 1 block up from the player's position
                for (int x = -1; x <= 1; x++) {
                    Block block = startBlock.getRelative(face.getOppositeFace(), z)
                            .getRelative(BlockFace.UP, y).getRelative(getPerpendicularFace(face), x);
                    if (block.getType() != Material.BEDROCK && block.getType() != Material.AIR) {
                        markedBlocks.add(block); // Mark the block
                        block.breakNaturally(player.getInventory().getItemInMainHand());
                        blocksBroken++;
                    }
                }
            }
        }

        // Add experience to the player for the blocks broken
        mmoPlayer.addMiningExperience(blocksBroken);
        PlayerData.savePlayerData(mmoPlayer);

        // Notify the player about the gained experience
        player.sendMessage("§aYou gained " + blocksBroken + " mining experience!");

        // Clear the marked blocks after the operation
        markedBlocks.clear();
    }

    public static boolean isBlockMarked(Block block) {
        return markedBlocks.contains(block);
    }

    public static boolean isBoreOperation() {
        return isBoreOperation.get();
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

    private BlockFace getPerpendicularFace(BlockFace face) {
        switch (face) {
            case NORTH:
            case SOUTH:
                return BlockFace.EAST;
            case EAST:
            case WEST:
                return BlockFace.NORTH;
            default:
                return BlockFace.SELF;
        }
    }
}