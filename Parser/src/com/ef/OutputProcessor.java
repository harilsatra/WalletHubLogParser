package com.ef;

import models.CLParameters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OutputProcessor {
    CLParameters parameters;
    DatabaseManager databaseManager;

    public OutputProcessor(CLParameters parameters, DatabaseManager databaseManager) {
        this.parameters = parameters;
        this.databaseManager = databaseManager;
    }

    public void process() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
            Date startDate = dateFormat.parse(this.parameters.stringStartDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            if (this.parameters.duration.equals("daily")) {
                calendar.add(Calendar.DATE, 1);
                this.parameters.endDate = calendar.getTime();
            } else if (this.parameters.duration.equals("hourly")) {
                calendar.add(Calendar.HOUR, 1);
                this.parameters.endDate = calendar.getTime();
            }

            List<String> blocked_ips = this.databaseManager.getResult(startDate, this.parameters.endDate, this.parameters.threshold);
            for (String ip : blocked_ips) {
                String[] temp = ip.split(" ");
                System.out.println(temp[0]);
                this.databaseManager.insertBlockedSql(temp[0], "Accessed " + temp[1] + " times (more than or equal to threshold of " + this.parameters.threshold + ") from " + startDate + " to " + this.parameters.endDate + ".");
            }

        } catch (ParseException e) {
            throw new RuntimeException("Cannot parse start date.", e);
        }
    }
}
