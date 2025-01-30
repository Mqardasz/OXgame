package lab.oxgame.datasource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBinitializer {

    public static void initialize() {
        try (Connection conn = DataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS rozgrywka (" +
                    "rozgrywka_id INTEGER IDENTITY PRIMARY KEY, " +
                    "gracz_x VARCHAR(255) NOT NULL, " +
                    "gracz_o VARCHAR(255) NOT NULL, " +
                    "zwyciezca VARCHAR(255) NOT NULL, " +
                    "dataczas_rozgrywki TIMESTAMP NOT NULL" +
                    ")";
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing database", e);
        }
    }
}