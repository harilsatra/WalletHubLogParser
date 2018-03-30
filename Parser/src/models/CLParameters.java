package models;

import java.util.Date;

public class CLParameters {
    public String stringStartDate;
    public String duration;
    public int threshold;
    public Date startDate;
    public Date endDate;
    public String inputFilePath;
    
    public CLParameters() {
    		this.inputFilePath = "access.log";
    }
}
