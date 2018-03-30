package com.ef;

import models.DatabaseCredentials;

import java.util.Date;
import java.util.List;

public class DatabaseManager {
    DatabaseCredentials databaseCredentials;
    MySQLDatabase mySQLDatabase;

    public DatabaseManager(DatabaseCredentials databaseCredentials) {
        this.databaseCredentials = databaseCredentials;
    }

    private boolean connect() {
        this.mySQLDatabase = new MySQLDatabase(this.databaseCredentials);

        try {
            this.mySQLDatabase.connectToServer() ;
        } catch (Exception e) {
            System.out.println("Could not connect to the database server using the given credentials.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean checkForExistingDatabaseAndTable() {
        if (this.connect()){
            this.mySQLDatabase.createDatabase();

            try {
                if(this.mySQLDatabase.connectToDatabase()){
                    this.mySQLDatabase.createTables();
                }
                return true;
            } catch (Exception e) {
                System.out.println("There was a problem while working on the created database.");
                return false;
            }
        }
        return false;
    }

    public void insert(String time, String ip) {
        this.mySQLDatabase.insertSql(time, ip);
    }

    public List<String> getResult(Date startDate, Date endDate, int threshold) {
        return this.mySQLDatabase.getResult(startDate, endDate, threshold);
    }

    public void insertBlockedSql(String s, String s1) {
        this.mySQLDatabase.insertBlockedSql(s, s1);
    }
    
    public void truncate() {
    		this.mySQLDatabase.truncate();
    }
}
