package com.studenthack.team40.team40;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity implements View.OnClickListener {

    //button declared

    Button btnNewRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setTitle("Team40 App");

        //New run button
        btnNewRun = (Button)findViewById(R.id.btnNewRun);
        btnNewRun.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btnNewRun:
                btnNewRunClick();
                break;
        }
    }

    private void btnNewRunClick() {
        Intent myIntent = new Intent(HomePage.this, MainActivity.class);
        startActivity(myIntent);
    }
}
