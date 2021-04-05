package me.zoon20x.levelpoints.utils;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class MySQL {

    public String host;
    public int port;
    public String database;
    public String username;
    public String password;
    public String table;
    public boolean ssl;
    public Connection con;

    public MySQL(String host, int port, String database, String username, String password, String table, boolean ssl){
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.table = table;
        this.ssl = ssl;
        connect();

    }



    public void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=" + ssl, username, password);
                LevelPoints.getDebug(DebugSeverity.NORMAL, "MySQL Connected");
                createTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return (con == null ? false : true);
    }

    public Connection getConnection() {
        return con;
    }

    public void createTable(){
        try {
            PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + table + "`" +
                    " (`UUID` varchar(200)," +
                    " `NAME` varchar(200), `LEVEL` INT(10)," +
                    " EXP DOUBLE(10,2), PRESTIGE INT(10)," +
                    " ACTIVEBOOSTER DOUBLE(10,2)," +
                    " BOOSTEROFF varchar(200)," +
                    " BOOSTERS TEXT(60000))");
            statement.executeUpdate();
            LevelPoints.getDebug(DebugSeverity.NORMAL, table + " table created");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean playerExists(UUID uuid){
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if(results.next()){
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateSQLData(UUID uuid){
        PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(uuid);
        try {
            PreparedStatement statement = getConnection().prepareStatement("UPDATE " + table + " SET PRESTIGE=? WHERE UUID=?");
            statement.setString(1, String.valueOf(data.getPrestige()));
            statement.setString(2, uuid.toString());
            statement.executeUpdate();

            statement = getConnection().prepareStatement("UPDATE " + table + " SET LEVEL=? WHERE UUID=?");
            statement.setString(1, String.valueOf(data.getLevel()));
            statement.setString(2, uuid.toString());
            statement.executeUpdate();

            statement = getConnection().prepareStatement("UPDATE " + table + " SET EXP=? WHERE UUID=?");
            statement.setString(1, String.valueOf(data.getExp()));
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void updateServerData(UUID uuid){
        try{
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(uuid);
            data.setLevel(results.getInt("LEVEL"));
            data.setExp(results.getDouble("EXP"));
            data.setPrestige(results.getInt("PRESTIGE"));

        }catch (SQLException e){
            e.printStackTrace();
        }

        LevelPoints.getTopListSettings().generateTopCache(50);
    }


    public void createPlayer(UUID uuid){
        try{
            PreparedStatement insert = getConnection().prepareStatement("INSERT INTO " + table + " (UUID,NAME,LEVEL,EXP,PRESTIGE,ACTIVEBOOSTER,BOOSTEROFF,BOOSTERS) VALUE (?,?,?,?,?,?,?,?)");
            PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(uuid);

            insert.setString(1, uuid.toString());
            insert.setString(2, data.getName());
            insert.setString(3, String.valueOf(data.getLevel()));
            insert.setString(4, String.valueOf(data.getExp()));
            insert.setString(5, String.valueOf(data.getPrestige()));
            insert.setString(6, String.valueOf(1));
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
            insert.setString(7, format.format(new Date()));
            insert.setString(8, String.valueOf(new ArrayList<String>()));
            insert.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }



}