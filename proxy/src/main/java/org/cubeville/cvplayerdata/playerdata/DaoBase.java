package org.cubeville.cvplayerdata.playerdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DaoBase {

    String dbUser;
    String dbPassword;
    String dbDatabase;

    Connection connection;

    public DaoBase(String dbUser, String dbPassword, String dbDatabase) {
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.dbDatabase = dbDatabase;
    }

    protected Connection getConnection() {
        if(connection != null) {
            try {
                Statement statement = connection.createStatement();
                statement.execute("SELECT 1");
                return connection;
            } catch(SQLException e) {
                try { connection.close(); } catch(SQLException ignored) {}
            }
        }
        openConnection();
        return connection;
    }

    private void openConnection() {
        Properties info = new Properties();
        info.put("autoReconnect", "false");
        info.put("user", dbUser);
        info.put("password", dbPassword);
        info.put("useUnicode", "true");
        info.put("characterEncoding", "utf8");
        String url = "jdbc:mysql://localhost:3306/" + dbDatabase + "?useSSL=false";
        connection = null;
        try {
            connection = DriverManager.getConnection(url, info);
        } catch(SQLException e) {
            throw new RuntimeException("Can't open SQL connection to " + url, e);
        }
    }
}
