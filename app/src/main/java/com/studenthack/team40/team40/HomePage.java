package com.studenthack.team40.team40;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;


public class HomePage extends AppCompatActivity implements View.OnClickListener {

    //button declared

    Button btnNewRun;
    Button btnRecentRuns;
    Button btnRecords;
    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("gameSetting", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setTitle("Team40 App");

        //New run button
        btnNewRun = (Button)findViewById(R.id.btnNewRun);
        btnNewRun.setOnClickListener(this);
        //recent run button
        btnRecentRuns = (Button)findViewById(R.id.btnRecentRuns);
        btnRecentRuns.setOnClickListener(this);
        //records button
        btnRecords = (Button)findViewById(R.id.btnRecords);
        btnRecords.setOnClickListener(this);
        //continue
        btnContinue = (Button)findViewById(R.id.btnContinue);


        if (sharedPreferences.getBoolean("GameOn", false))
            btnContinue.setVisibility(View.INVISIBLE);
        else
            btnContinue.setVisibility(View.VISIBLE);

    }



    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btnNewRun:
                btnNewRunClick();
                break;
            case R.id.btnRecentRuns:
                btnRecentRunsClick();
                break;
            case R.id.btnRecords:
                btnRecordsClick();
                break;
        }
    }

    private void btnRecordsClick() {
        Intent myIntent = new Intent(HomePage.this, Records.class);
        startActivity(myIntent);
    }

    private void btnRecentRunsClick() {
        Intent myIntent = new Intent(HomePage.this, RecentRuns.class);
        startActivity(myIntent);
    }

    private void btnNewRunClick() {
        Intent myIntent = new Intent(HomePage.this, MainActivity.class);
        startActivity(myIntent);
    }
}
