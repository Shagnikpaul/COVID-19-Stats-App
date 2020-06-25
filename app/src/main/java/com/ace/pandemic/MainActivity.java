package com.ace.pandemic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kosalgeek.android.caching.FileCacher;

public class MainActivity extends AppCompatActivity {

    public static final String key="WHATGOESYOURBAPUS";

    TextView infoOne,infoTwo,infoText;
    Button confirmedButton,recoveredButton, deathButton;
    //cacher object
    FileCacher<String> stringCacher=new FileCacher<>(MainActivity.this,"sometext.txt");
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //intent for IntroSCreen
        Intent intent =new Intent(this,IntroScreen.class);

        //setting main xml
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //copying main Activity textView objects to covidapi
        covidApi.INFOONE = findViewById(R.id.INFOONE);
        covidApi.INFOTWO = findViewById(R.id.INFOTWO);
        covidApi.INFOTEXT = findViewById(R.id.totalSubHeading);
        covidApi.INFOTEXT2 = findViewById(R.id.newSubHeading);

        new covidApi(this,stringCacher);

        //making a context object
        final Context ct=this;

        confirmedButton = findViewById(R.id.ConfirmedButton);
        recoveredButton = findViewById(R.id.recoveredButton);
        deathButton = findViewById(R.id.deathButton);

        //setting data onCLick
        confirmedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { covidApi.dynamicInfoDisplay(1,ct); }});
        recoveredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { covidApi.dynamicInfoDisplay(2,ct);
            }
        });
        deathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { covidApi.dynamicInfoDisplay(3,ct);
            }
        });

        startActivity(intent);
    }
    public void  retryNet(View view)
    {
        new covidApi(this,stringCacher);
    }
    public void getGraph(View view)
    {

        //creating intent object
        Intent intent =new Intent(this,graph_activity.class);
        //message to be passed

        //My bapu doesn't know
        intent.putExtra(key,covidApi.totalCases);

        //starting new Activity
        startActivity(intent);
    }
}
