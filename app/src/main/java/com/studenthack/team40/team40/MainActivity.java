package com.studenthack.team40.team40;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    public static final String TAG = "BeaconsEverywhere";
    private BeaconManager beaconManager;
    private TextView txt;
    private ArrayList<Beacon> listOfBeacons = new ArrayList<>();
    private ArrayList<Checkpoint> listOfCheckpoints = new ArrayList<>();
    private Region region;
    private GoogleApiClient client;
    private BeaconConsumer consumer = this;
    private Boolean running = false;
    private String output;
    private FloatingActionButton fab;
    private Resources res;
    private InputStream in;
    private Party currentParty;
    private Timer myTimer;
    private int time = 0;

    //image slider
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter adapter = new CustomAdapter(MainActivity.this);
        viewPager.setAdapter(adapter);

        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("New Run");

        myTimer = new Timer();

        //res = getResources();
        in = getResources().openRawResource(R.raw.checkpoints);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) {
                    running = false;
                    beaconManager.unbind(consumer);
                    Snackbar.make(view, "Stopped", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    fab.setImageResource(R.drawable.ic_media_play);
                    Toast.makeText(getApplicationContext(), String.valueOf(time), Toast.LENGTH_SHORT).show();
                    myTimer.cancel();
                } else {
                    running = true;
                    beaconManager.bind(consumer);
                    Snackbar.make(view, "Running", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    fab.setImageResource(R.drawable.ic_media_pause);

                    myTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            TimeMethod();
                        }
                    }, 0, 1000);

                }
            }
        });



        Log.d(TAG, "post deserialization = " + listOfCheckpoints.toString());
        HandleXML obj = new HandleXML();
        obj.fetchXML(in);
        listOfCheckpoints = obj.getListOfCheckpoints();
      //  Log.d(TAG,"post deserialization = " + listOfCheckpoints.toString());
        /*Beacon beacon = new Beacon.Builder()
                .setId1("b9407f30-f5f8-466e-aff9-25556b57fe6d")
                .setId2("14547")
                .setId3("40129")
                .setManufacturer(0x0118) // Radius Networks.  Change this for other beacon layouts
                .setTxPower(-59)
                .setDataFields(Arrays.asList(new Long[]{0l})) // Remove this for beacon layouts without d: fields
                .build();

        listOfCheckpoints.add(new Checkpoint(new LatLng(123.0, 123.0), "Point 1", beacon));*/
        region = new Region("ranged region", Identifier.fromUuid(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D")), null, null);

        beaconManager = BeaconManager.getInstanceForApplication(this);

        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        //beaconManager.bind(this);

        txt = (TextView) findViewById(R.id.mainText);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        currentParty = new Party();
    }

    private void TimeMethod(){
        time++;
        this.runOnUiThread(Timer_Tick);
        Log.d("Timer", Integer.toString(time));
    }

    private Runnable Timer_Tick = new Runnable() {
        @Override
        public void run() {
        }
    };

    private boolean IsACheckpoint(Beacon beacon)
    {
        if (listOfCheckpoints.size()>0) {
            for (Checkpoint point : listOfCheckpoints) {
                if (point.getId3().equals(beacon.getId3().toString())) return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        final Region region = new Region("myBeacons", Identifier.parse("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                try {
                    Log.d(TAG, "didEnterRegion");
                    beaconManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didExitRegion(Region region) {
                try {
                    Log.d(TAG, "didExitRegion");
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (Beacon oneBeacon : beacons) {
                    Log.d(TAG, "The ID3 is = " + oneBeacon.getId3().toString() + IsACheckpoint(oneBeacon));
                    if (IsACheckpoint(oneBeacon)) {
                        if (listOfBeacons.contains(oneBeacon)) listOfBeacons.remove(oneBeacon);

                        listOfBeacons.add(oneBeacon);
                    }
                    Log.d(TAG, "distance: " + oneBeacon.getDistance() + " id:" + oneBeacon.getId1() + "/" + oneBeacon.getId2() + "/" + oneBeacon.getId3());
                }
                runOnUiThread(new Runnable() {
                    public void run() {

                        if (listOfBeacons.size() > 0) {
                            txt.setText(listOfBeacons.toString());
                            //beaconManager.unbind(consumer);
                            //running = false;
                            currentParty.update(listOfBeacons);
                            if(currentParty.getIsUpdated()) {
                                Toast.makeText(getApplicationContext(), "Found a Beacon!", Toast.LENGTH_SHORT).show();
                                currentParty.setIsUpdated(false);
                            }
                            //fab.setImageResource(R.drawable.ic_media_pause);

                        }
                        else txt.setText("Hello world");
                    }
                });

            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(region);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.studenthack.team40.team40/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.studenthack.team40.team40/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


}
