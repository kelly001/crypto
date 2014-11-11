package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by new_name on 11.11.2014.
 */
public class Database {
    protected String username = "crypto";
    protected String password = "crypto";
    protected String server = "localhost";
    protected String port = "85";

    public Connection getConnection() throws SQLException {

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.username);
        connectionProps.put("password", this.password);

        conn = DriverManager.getConnection("jdbc:mysql://"+this.server +
                        ":" + this.port + "/",
                connectionProps);

        /*if (this.dbms.equals("mysql")) {
            conn = DriverManager.getConnection(
                    "jdbc:" + this.dbms + "://" +
                            this.server +
                            ":" + this.port + "/",
                    connectionProps);
        } else if (this.dbms.equals("derby")) {
            conn = DriverManager.getConnection(
                    "jdbc:" + this.dbms + ":" +
                            this.dbName +
                            ";create=true",
                    connectionProps);
        }*/
        System.out.println("Connected to database");
        return conn;
    }
}
