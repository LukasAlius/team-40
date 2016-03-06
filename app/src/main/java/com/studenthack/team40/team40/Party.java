package com.studenthack.team40.team40;


import android.util.Log;

import org.altbeacon.beacon.Beacon;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Party {
    private ArrayList<String> validatedCheckpointsID = new ArrayList<>();
    private Date startDate;
    private Date arrivalDate;
    private Boolean isUpdated;

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

    public void setIsUpdated(Boolean bool) {isUpdated = bool;}

    public Boolean getIsUpdated() {return isUpdated;}



    public Party(){
        validatedCheckpointsID = new ArrayList<>();
        startDate = null;
        arrivalDate = null;
    }

    public void update(ArrayList<Beacon> points){
        boolean isValidated;
        for (Beacon point: points)
        {
            isValidated = false;
            for (String validatedPointID: validatedCheckpointsID)
            {
                if(point.getId3().toString().equals(validatedPointID)) isValidated = true;
            }
            if(!isValidated) {
                validatedCheckpointsID.add(point.getId3().toString());
                isUpdated = true;
            }
        }
        Log.d("TAG", validatedCheckpointsID.toString());
    }

    public void reset(){
        validatedCheckpointsID = new ArrayList<>();
        startDate = null;
        arrivalDate = null;
    }

    public void start() {
        startDate = new Date();
    }
}
