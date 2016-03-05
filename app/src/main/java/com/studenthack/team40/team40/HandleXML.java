package com.studenthack.team40.team40;

import android.app.Activity;
import android.util.Log;
import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;

import org.altbeacon.beacon.Identifier;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class HandleXML extends Activity{
    private ArrayList<Checkpoint> listOfCheckpoints = new ArrayList<>();
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    public HandleXML(){

    }

    public ArrayList<Checkpoint> getListOfCheckpoints() { return listOfCheckpoints; }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text = null;
        Checkpoint ch = new Checkpoint();
        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();

                switch (event){
                    case XmlPullParser.START_TAG:
                        switch (name) {
                            case "Checkpoint":
                                ch = new Checkpoint();
                                break;
                            case "name":
                                ch.setName(myParser.getAttributeValue(null, "value"));
                                break;
                            case "coordinates":
                                ch.setCoordinates(new LatLng(Double.valueOf(myParser.getAttributeValue(null, "lat")),
                                        Double.valueOf(myParser.getAttributeValue(null, "long"))));
                            case "id1":
                                ch.setId1(myParser.getAttributeValue(null, "value"));
                                break;
                            case "id2":
                                ch.setId2(myParser.getAttributeValue(null, "value"));
                                break;
                            case "id3":
                                ch.setId3(myParser.getAttributeValue(null, "value"));
                                break;
                            case "distance":
                                ch.setDistance(Double.valueOf(myParser.getAttributeValue(null, "value")));
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "Checkpoint":
                                this.listOfCheckpoints.add(ch);
                                break;
                        }break;
                }
                event = myParser.next();
            }
            parsingComplete = false;
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchXML(InputStream in_s){
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXMLAndStoreIt(parser);

        } catch (XmlPullParserException e) {

            e.printStackTrace();
        }
    }
}