/**The brain of the app controls everything
 * this used covidApi as assistant
 * IntroScreen actually appears after MainActivity
 * this
 */
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
    Button confirmedButton,recoveredButton, deathButton,graphButton;

    //stores the current screen number to be displayed for mainActivity by default 1
    static int currentScreen = 1;
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
        graphButton = findViewById(R.id.graphButton);

        //setting data onCLick
        confirmedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentScreen = 1;
                covidApi.dynamicInfoDisplay(1,ct); }});
        recoveredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentScreen = 2;
                covidApi.dynamicInfoDisplay(2,ct);
            }
        });
        deathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentScreen = 3;
                covidApi.dynamicInfoDisplay(3,ct);
            }
        });

        //setting the specific activity for specific button presses

        // this code uses the currentScreen int initiated by case type on click to set the specific graph
        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent graphScreenIntent;
                switch (currentScreen)
                {
                    case 1 : graphScreenIntent = new Intent(ct, graph_activity.class);
                             startActivity(graphScreenIntent);
                             break;
                    case 2 : graphScreenIntent = new Intent(ct, recoveredGraph.class);
                             startActivity(graphScreenIntent);
                             break;
                    case 3 : graphScreenIntent = new Intent(ct, deathGraph.class);
                             startActivity(graphScreenIntent);
                             break;
                    default: break;
                }
            }
        });
        startActivity(intent);
    }
    /**Helps to retry the collection of data from covidApi
     * **/
    public void  retryNet(View view)
    {
        final Context ct=this;
        covidApi.updateData(ct,stringCacher,currentScreen);
    }
    /**
     * affected by flex
     */
    /*public void getGraph(View view)
    {

        //creating intent object
        Intent intent =new Intent(this,graph_activity.class);
        //message to be passed

        //My bapu doesn't know
        intent.putExtra(key,covidApi.totalCases);

        //starting new Activity
        startActivity(intent);
    }*/
}
