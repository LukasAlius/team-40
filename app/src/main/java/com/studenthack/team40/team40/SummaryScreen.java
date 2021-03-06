package com.studenthack.team40.team40;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SummaryScreen extends AppCompatActivity implements View.OnClickListener{

    ImageButton btnHome;

    int x = 14;
    private int time;
    private int beaconsNum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_screen);
        setTitle("Your Race Summary");

        TextView txtTime = (TextView)findViewById(R.id.txtTime);
        TextView txtBeacons = (TextView)findViewById(R.id.txtCheckpoints);

        time = getIntent().getExtras().getInt("Time");
        beaconsNum = getIntent().getExtras().getInt("BeaconsNum");

        txtTime.setText("" + time);
        txtBeacons.setText("" + beaconsNum);
        btnHome = (ImageButton)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnHome:
                btnHomeClick();
                break;
        }
    }

    private void btnHomeClick() {

        //finish();
        Intent intent = new Intent(SummaryScreen.this, HomePage.class);
        startActivity(intent);
    }
}
