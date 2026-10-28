package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    protected static String username = "crypto";
    protected static String password = "crypto";
    protected static String server = "localhost";
    protected static String port = "3306";
    protected static String database = "crypto";

    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", username);
        connectionProps.put("password", password);

        conn = DriverManager.getConnection("jdbc:mysql://"+server +
                        ":" + port + "/"+database,
                connectionProps);

        return conn;
    }
}
