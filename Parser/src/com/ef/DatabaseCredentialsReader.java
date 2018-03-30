package com.ef;

import models.DatabaseCredentials;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DatabaseCredentialsReader {

    final private static String CONFIG_FILE_PATH = "Credentials.txt";
    private static BufferedReader bufferedReader;
    private DatabaseCredentials databaseCredentials;

    public DatabaseCredentials getDatabaseCredentials() {
        try {
            FileInputStream fstream = new FileInputStream(CONFIG_FILE_PATH);
            bufferedReader = new BufferedReader(new InputStreamReader(fstream));
            databaseCredentials = new DatabaseCredentials();
            String strLine;

            while ((strLine = bufferedReader.readLine()) != null)   {
                String[] temp =strLine.split("=");
                if(temp[0].trim().equals("Username")) {
                		//System.out.println(temp[1].trim());
                    databaseCredentials.setUserName(temp[1].trim());
                } else if(temp[0].trim().equals("Password")) {
                    databaseCredentials.setPassword(temp[1].trim());
                    //System.out.println(temp[1].trim());
                } else if(temp[0].trim().equals("Address")) {
                    databaseCredentials.setiPAddress(temp[1].trim());
                    //System.out.println(temp[1].trim());
                }
            }
            fstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return databaseCredentials;
    }
}
