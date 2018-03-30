package com.ef;

import java.io.*;

public class InputFileProcessor {
    private String inputFilePath;
    private BufferedReader bufferedReader;
    DatabaseManager databaseManager;

    public InputFileProcessor(String filePath, DatabaseManager databaseManager) {
        this.inputFilePath = filePath;
        this.databaseManager = databaseManager;
    }

    void process() {
        try {
            FileInputStream fstream = new FileInputStream(this.inputFilePath);
            bufferedReader = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            while ((strLine = bufferedReader.readLine()) != null) {
                String[] temp = strLine.split("\\|");
                temp[0] = temp[0].substring(0, 19);
                this.databaseManager.insert(temp[0], temp[1]);
            }
            fstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
