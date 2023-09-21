import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    public static Connection connect() {
        Connection connection;
        try {
            String url = "jdbc:sqlite:src/database.sqlite";
            connection = DriverManager.getConnection(url);
            System.out.println("Connection to " + connection + "has been established.");

        } catch (SQLException e) {
            throw new Error("Problem", e);
        }
        return connection;
    }
}