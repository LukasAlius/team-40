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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    private Boolean running = true;
    private String output;
    private Resources res;
    private InputStream in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //res = getResources();
        in = getResources().openRawResource(R.raw.checkpoints);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) {
                    running = false;
                    beaconManager.unbind(consumer);
                    Snackbar.make(view, "Stopped", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                } else {
                    running = true;
                    beaconManager.bind(consumer);
                    Snackbar.make(view, "Running", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

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

        beaconManager.bind(this);

        txt = (TextView) findViewById(R.id.mainText);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean IsACheckpoint(Beacon beacon)
    {
        if (!listOfCheckpoints.isEmpty()) {
            for (Checkpoint point : listOfCheckpoints) {
                if (point.getId3().equals(beacon.getId3().toString())) return true;
            }
        }
        return false;
    }
    private Boolean compare() {
        for (Beacon beacon : listOfBeacons) {
            for (Checkpoint c : listOfCheckpoints) {
                Log.d(TAG, c.getId3() + " | " + beacon.getId3());
                if (c.getId3().equals(beacon.getId3())) return true;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                        if(listOfBeacons.contains(oneBeacon)) listOfBeacons.remove(oneBeacon);

                        listOfBeacons.add(oneBeacon);
                    }
                    Log.d(TAG, "distance: " + oneBeacon.getDistance() + " id:" + oneBeacon.getId1() + "/" + oneBeacon.getId2() + "/" + oneBeacon.getId3());
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        txt.setText(listOfBeacons.toString());
                        if (listOfBeacons.size() > 0) {
                            Toast.makeText(getApplicationContext(), "Found a Beacon!", Toast.LENGTH_SHORT).show();
                            beaconManager.unbind(consumer);
                            running = false;
                        }
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

    public String LoadFile() throws IOException
    {
        //Create a InputStream to read the file into
        InputStream iS;

        //get the file as a stream
        iS = this.getResources().openRawResource(R.raw.checkpoints);

        //create a buffer that has the same size as the InputStream
        byte[] buffer = new byte[iS.available()];
        //read the text file as a stream, into the buffer
        iS.read(buffer);
        //create a output stream to write the buffer into
        ByteArrayOutputStream oS = new ByteArrayOutputStream();
        //write this buffer to the output stream
        oS.write(buffer);
        //Close the Input and Output streams
        oS.close();
        iS.close();

        //return the output stream as a String
        return oS.toString();
    }

}
