package pixelfox.mmo.LevelListeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import pixelfox.mmo.MMO;
import pixelfox.mmo.PlayerData.MMOPlayer;
import pixelfox.mmo.PlayerData.PlayerData;

import java.util.HashSet;
import java.util.Set;

public class MiningListener implements Listener {

    private static final Set<Material> ORES = Set.of(
            Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE,
            Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.LAPIS_ORE,
            Material.REDSTONE_ORE, Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE,
            Material.DEEPSLATE_COAL_ORE, Material.DEEPSLATE_IRON_ORE, Material.DEEPSLATE_GOLD_ORE,
            Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.DEEPSLATE_LAPIS_ORE,
            Material.DEEPSLATE_REDSTONE_ORE, Material.ANCIENT_DEBRIS, Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE);

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        Player bukkitPlayer = event.getPlayer();
        String uuid = bukkitPlayer.getUniqueId().toString();

        MMOPlayer player = PlayerData.loadPlayerData(uuid, bukkitPlayer);

        if (player == null) {
            player = new MMOPlayer(uuid, bukkitPlayer.getName(), 0, 0, bukkitPlayer);
        }

        if (player.getMiningLevel() >= 10) {
            dropEXPorb(event.getBlock(), bukkitPlayer);
        }

        PlayerData.savePlayerData(player);

        if (player.getMiningLevel() >= 2 && isOre(event.getBlock().getType())) {
            veinMiner(event.getBlock(), bukkitPlayer);
        }
    }

    private boolean isOre(Material type) {
        return ORES.contains(type);
    }

    private void veinMiner(Block block, Player player) {
        Set<Block> minedBlocks = new HashSet<>();
        recursivelyMineOres(block, player, minedBlocks);
    }

    private void recursivelyMineOres(Block block, Player player, Set<Block> minedBlocks) {
        if (!isOre(block.getType()) || minedBlocks.contains(block)) {
            return;
        }

        minedBlocks.add(block);
        block.breakNaturally(player.getInventory().getItemInMainHand());

        for (Block attachedBlock : getAttachedBlocks(block)) {
            recursivelyMineOres(attachedBlock, player, minedBlocks);
        }
    }

    private Set<Block> getAttachedBlocks(Block block) {
        Set<Block> attachedBlocks = new HashSet<>();
        int[][] offsets = {
                {1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}, // Adjacent blocks
                {1, 1, 0}, {1, -1, 0}, {-1, 1, 0}, {-1, -1, 0}, // Diagonal on XY plane
                {1, 0, 1}, {1, 0, -1}, {-1, 0, 1}, {-1, 0, -1}, // Diagonal on XZ plane
                {0, 1, 1}, {0, 1, -1}, {0, -1, 1}, {0, -1, -1}, // Diagonal on YZ plane
                {1, 1, 1}, {1, 1, -1}, {1, -1, 1}, {1, -1, -1}, // Corner blocks
                {-1, 1, 1}, {-1, 1, -1}, {-1, -1, 1}, {-1, -1, -1}
        };

        for (int[] offset : offsets) {
            attachedBlocks.add(block.getRelative(offset[0], offset[1], offset[2]));
        }

        return attachedBlocks;
    }

    private void dropEXPorb(Block block, Player player) {
        ExperienceOrb orb = block.getWorld().spawn(block.getLocation(), ExperienceOrb.class);
        orb.setExperience(1);
    }
}