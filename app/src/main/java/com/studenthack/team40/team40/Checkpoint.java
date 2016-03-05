package com.studenthack.team40.team40;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.LatLng;

import org.altbeacon.beacon.Beacon;
//import org.altbeacon.beacon.String;

import java.io.Serializable;

public class Checkpoint implements Serializable {
    private LatLng coordinates;
    private String name;
    private double distance;
    private String id1;
    private String id2;
    private String id3;

    public LatLng getCoordinates(){ return coordinates; }
    public void setCoordinates(LatLng co) { coordinates = co; }

    public String getName() { return name; }
    public void setName(String n) { name = n; }

    public double getDistance() { return distance; }
    public void setDistance(double dis) { distance = dis; }

    public String getId1() { return id1; }
    public void setId1(String id) { id1 = id; }

    public String getId2() { return id2; }
    public void setId2(String id) { id2 = id; }

    public String getId3() { return id3; }
    public void setId3(String id) { id3 = id; }

    public Checkpoint(LatLng co, String n, Beacon beacon){
        //coordinates = co;
        name = n;
        distance = beacon.getDistance();
        id1 = beacon.getId1().toString();
        id2 = beacon.getId2().toString();
        id3 = beacon.getId3().toString();
    }

    public Checkpoint(){

    }
}
