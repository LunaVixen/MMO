package pixelfox.mmo.PlayerData;

import org.bukkit.entity.Player;
import pixelfox.mmo.MMO;

import java.sql.*;



public class PlayerData {
    private static final String DB_PATH = MMO.getInstance().getDataFolder().getPath() + "/playerdata.db";

    public static void savePlayerData(MMOPlayer player) {
        String sql = "REPLACE INTO player_levels (uuid, name, mining_level, mining_experience) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, player.getUuid());
            pstmt.setString(2, player.getName());
            pstmt.setInt(3, player.getMiningLevel());
            pstmt.setInt(4, player.getMiningExperience());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static MMOPlayer loadPlayerData(String uuid, Player bukkitPlayer) {
        String sql = "SELECT * FROM player_levels WHERE uuid = ?";
        MMOPlayer player = null;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, uuid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                player = new MMOPlayer(
                        rs.getString("uuid"),
                        rs.getString("name"),
                        rs.getInt("mining_level"),
                        rs.getInt("mining_experience"),
                        bukkitPlayer
                );
            } else {
                player = new MMOPlayer(uuid, bukkitPlayer.getName(), 0, 0, bukkitPlayer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return player;
    }
}
