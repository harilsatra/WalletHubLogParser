package com.ef;

import models.CLParameters;
import models.DatabaseCredentials;

public class Parser {
    public static void main(String[] args) {
        CLParameters parameters = null;
        if (args.length != 0) {
            parameters = new CLParameters();

            for (String arg : args) {
            		String[] input = arg.split("=");
            		String param = input[0];
            		String value = input[1];
                if (arg != null) {
                		if (param.equals("--accesslog") && value != null) {
                        parameters.inputFilePath = value;
                    } else if (param.equals("--startDate") && value != null) {
                        parameters.stringStartDate = value;
                    } else if (param.equals("--duration") && value != null) {
                        parameters.duration = value;
                    } else if (param.equals("--threshold") && value != null) {
                        parameters.threshold = Integer.parseInt(value);
                    }
                }
            }


            System.out.println("Reading database credentials...");
            DatabaseCredentialsReader databaseCredentialsReader = new DatabaseCredentialsReader();

            DatabaseCredentials databaseCredentials = databaseCredentialsReader.getDatabaseCredentials();

            DatabaseManager databaseManager = new DatabaseManager(databaseCredentials);
            System.out.println("Checking database connectivity and elements...");
            if (databaseManager.checkForExistingDatabaseAndTable()) {
                InputFileProcessor inputFileProcessor = new InputFileProcessor(parameters.inputFilePath, databaseManager);
                inputFileProcessor.process();
                System.out.println("Finding IPs...");
                OutputProcessor outputProcessor = new OutputProcessor(parameters, databaseManager);
                outputProcessor.process();
                databaseManager.truncate();
            };


        }

    }
}
