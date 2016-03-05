package com.studenthack.team40.team40;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.LatLng;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Identifier;

import java.io.Serializable;

public class Checkpoint implements Serializable {
    //private LatLng coordinates;
    private String name;
    private double distance;
    private Identifier id1;
    private Identifier id2;
    private Identifier id3;

    /*public LatLng getCoordinates(){ return coordinates; }
    public void setCoordinates(LatLng co) { coordinates = co; }*/

    public String getName() { return name; }
    public void setName(String n) { name = n; }

    public double getDistance() { return distance; }
    public void setDistance(double dis) { distance = dis; }

    public Identifier getId1() { return id1; }
    public void setId1(Identifier id) { id1 = id; }

    public Identifier getId2() { return id2; }
    public void setId2(Identifier id) { id2 = id; }

    public Identifier getId3() { return id3; }
    public void setId3(Identifier id) { id3 = id; }

    public Checkpoint(LatLng co, String n, Beacon beacon){
        //coordinates = co;
        name = n;
        distance = beacon.getDistance();
        id1 = beacon.getId1();
        id2 = beacon.getId2();
        id3 = beacon.getId3();
    }
}
