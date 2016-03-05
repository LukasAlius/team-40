package com.studenthack.team40.team40;


import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by aurélie on 05/03/2016.
 */
public class Party {
    private ArrayList<String> validatedCheckpointsID = new ArrayList<>();
    private Date startDate;
    private Date arrivalDate;

    public void setValidatedCheckpointsID(ArrayList<String> checkpointsID){
        validatedCheckpointsID = checkpointsID;
    }

    public ArrayList<String> getValidatedCheckpointsID(){
        return validatedCheckpointsID;
    }

    public void setStartDate(Date start){
        startDate = start;
    }

    public Date getStartDate(){
        return startDate;
    }

    public void setArrivalDate(Date arrival){
        arrivalDate = arrival;
    }

    public Date getArrivalDate(){
        return arrivalDate;
    }



    public Party(){
        validatedCheckpointsID = new ArrayList<>();
        startDate = new Date();
        arrivalDate = null;
    }
}
