package pixelfox.mmo.Utils;

import pixelfox.mmo.MMO;

import java.io.File;
import java.sql.*;

public class DatabaseManager {

    private static final String DB_FILENAME = "playerdata.db";

    public static void initialize() {
        File dbFile = new File(MMO.getInstance().getDataFolder(), DB_FILENAME);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getPath());
             Statement stmt = conn.createStatement()) {

            // Create table if not exists
            String sql = "CREATE TABLE IF NOT EXISTS player_levels (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "uuid TEXT NOT NULL UNIQUE, " +
                    "name TEXT NOT NULL, " +
                    "mining_level INTEGER NOT NULL DEFAULT 0, " +
                    "mining_experience INTEGER NOT NULL DEFAULT 0);";
            stmt.execute(sql);

            // Add columns if they don't exist
            addColumnIfNotExists(conn, "player_levels", "mining_level", "INTEGER NOT NULL DEFAULT 0");
            addColumnIfNotExists(conn, "player_levels", "mining_experience", "INTEGER NOT NULL DEFAULT 0");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addColumnIfNotExists(Connection conn, String tableName, String columnName, String columnDefinition) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getColumns(null, null, tableName, columnName);
        if (!rs.next()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnDefinition);
            }
        }
    }

}
