package com.ef;

import models.DatabaseCredentials;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MySQLDatabase {
    final private static String DRIVER = "com.mysql.jdbc.Driver";
    final private static String DATABASE_NAME = "wallet_hub_dev";
    private DatabaseCredentials credentials;

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private String rootURL;
    private String databaseURL;
    //final private String DRIVER = "com.mysql.jdbc.Driver";

    MySQLDatabase(DatabaseCredentials databaseCredentials) {
        this.credentials = databaseCredentials;
        this.rootURL = String.format("jdbc:mysql://%s:3306/", databaseCredentials.getiPAddress());
        //System.out.println(this.rootURL);
    }

    public boolean connectToServer() throws Exception {
        try {
            Class.forName(DRIVER);
            connect = DriverManager.getConnection(rootURL, this.credentials.getUserName(), this.credentials.getPassword());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver not in classpath: " + DRIVER, e);
        }
        return true;
    }

    public void createDatabase() {
        System.out.println("Creating/Opening Database...");
        try {
            if (connect != null) {
                statement = connect.createStatement();
                int myResult = statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);
                databaseURL = rootURL + DATABASE_NAME;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean connectToDatabase() throws Exception {
        try {
            Class.forName(DRIVER);
            String newDatabaseURL = databaseURL + "?autoReconnect=true&useSSL=false";
            connect = DriverManager.getConnection(newDatabaseURL, this.credentials.getUserName(), this.credentials.getPassword());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver not in classpath: " + DRIVER, e);
        }
        return true;
    }

    public void createTables() {
        System.out.println("Creating Tables");
        String access_log = "CREATE TABLE IF NOT EXISTS access_log ("
                + "time DATETIME,"
                + "ip VARCHAR(20))";
        String blacklist = "CREATE TABLE IF NOT EXISTS blacklist ("
                + "ip VARCHAR(20) UNIQUE,"
                + "comment VARCHAR(200))";
        try {
            statement = connect.createStatement();
            statement.executeUpdate(access_log);
            statement = connect.createStatement();
            statement.executeUpdate(blacklist);
        } catch (SQLException e) {
            throw new RuntimeException("An error has occurred on Table Creation " + DRIVER, e);
        }
    }

    protected void insertSql(String time, String ip) {
        String sql = "INSERT INTO `access_log` (`time`, `ip`) VALUES (?,?);";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedTimeStamp = dateFormat.parse(time);
            Timestamp timestamp = new Timestamp(parsedTimeStamp.getTime());
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.setString(2, ip);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Cannot perform your query statement: " + sql, e);
        } catch (ParseException e) {
            throw new RuntimeException("ERROR: Cannot parse time: ", e);
        }
    }

    protected List<String> getResult(Date startDate, Date endDate, int threshold) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "SELECT count(*) as count, ip FROM access_log WHERE time BETWEEN ? AND ? GROUP BY ip HAVING count >= ?";
        List<String> result = new LinkedList<String>();
        try {
            preparedStatement = connect.prepareStatement(sql);
            Timestamp start = new Timestamp(startDate.getTime());
            Timestamp end = new Timestamp(endDate.getTime());
            preparedStatement.setTimestamp(1, start);
            preparedStatement.setTimestamp(2, end);
            preparedStatement.setInt(3, threshold);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getString("ip") + " " + resultSet.getInt("count"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Cannot perform your query statement: " + sql, e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    protected void insertBlockedSql(String ip, String comment) {
        String sql = "INSERT IGNORE INTO `blacklist` (`ip`, `comment`) VALUES (?,?);";
        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, ip);
            preparedStatement.setString(2, comment);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Cannot perform your query statement: " + sql, e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void truncate() {
    		String truncate_log = "TRUNCATE `access_log`";
    		//String truncate_blacklist = "TRUNCATE `blacklist`";
    		try {
				statement = connect.createStatement();
				statement.executeUpdate(truncate_log);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        
    }
    


}
