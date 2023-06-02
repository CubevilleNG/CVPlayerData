package org.cubeville.cvplayerdata.playerdata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NameRecordDao extends DaoBase {

    private static NameRecordDao instance;
    public static NameRecordDao getInstance() { return instance; }

    public NameRecordDao(String dbUser, String dbPassword, String dbDatabase) {
        super(dbUser, dbPassword, dbDatabase);
        instance = this;
    }

    public synchronized List<NameRecord> getNameRecords(UUID player) {
        Connection connection = getConnection();
        List<NameRecord> ret = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT UNIX_TIMESTAMP(n.name_time) as name_time, n.name as name, n.uuid as uuid FROM namerecord n WHERE n.uuid = ?");
            statement.setString(1, player.toString());
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while(rs.next()) {
                long nameTime = rs.getLong("name_time") * 1000;
                String name = rs.getString("name");
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                NameRecord nR = new NameRecord(uuid, name);
                nR.setNameTime(nameTime);
                ret.add(nR);
            }
            statement.close();
        } catch(SQLException e) {
            throw new RuntimeException("Could not load name record entries!", e);
        }
        return ret;
    }

    public synchronized void addNameEntry(NameRecord record) {
        Connection connection = getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO namerecord (uuid, name, name_time) VALUES (?, ?, NOW())");
            statement.setString(1, record.getUuid().toString());
            statement.setString(2, record.getName());
            statement.executeUpdate();
            statement.close();
        } catch(SQLException e) {
            throw new RuntimeException("Could not save name record entry!", e);
        }
    }
}
