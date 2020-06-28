/** class specially designed to assist MainActivity
 * keep MainActivity clean
 * specifically contains volley requests
 * **/


package com.ace.pandemic;

import android.content.Context;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kosalgeek.android.caching.FileCacher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static android.widget.Toast.LENGTH_SHORT;


public class covidApi extends Thread
{
    public static String totalCases="",newCases="",totalRecovered="",totalDeaths="",newDeaths="";
    //textView Ref
    static TextView INFOONE,INFOTWO,INFOTEXT,INFOTEXT2;

    private static String url="https://disease.sh/v2/all";

    /**Helps to init data and set the default INFOONE and INFOTWO
     * **/

    covidApi(final Context ct, final FileCacher<String> stringCacher)//ct -> object of MainActivity(Context)
    {
        final StringRequest request = new StringRequest(url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try
                {
                    try {
                        //adding new cache
                        stringCacher.clearCache();
                        stringCacher.writeCache(response);
                }
                    catch (IOException exp)
                    {
                        Log.i("thiss","Error in cache writing");
                    }
                    //extracting data from JSON format
                    JSONObject object=new JSONObject(response);

                    totalCases = object.getString("cases");
                    newCases = object.getString("todayCases");
                    totalRecovered = object.getString("recovered");
                    totalDeaths = object.getString("deaths");
                    newDeaths = object.getString("todayDeaths");

                    //setting init textViews else Loading will be displayed.
                    INFOONE.setText(totalCases);
                    INFOTWO.setText(newCases);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //No internet Toast mandatory
                Toast.makeText(ct,"No Internet Connection", LENGTH_SHORT).show();
                Log.i("thiss","reacing here");
                if(stringCacher.hasCache())
                {
                    //setting cache
                    try
                    {
                        String chacheResponse = stringCacher.readCache();

                        //extracting data from JSON format
                        JSONObject object=new JSONObject(chacheResponse);
                        totalCases = object.getString("cases");
                        newCases = object.getString("todayCases");
                        totalRecovered = object.getString("recovered");
                        totalDeaths = object.getString("deaths");
                        newDeaths = object.getString("todayDeaths");

                        //setting init textViews
                        INFOONE.setText(totalCases);
                        INFOTWO.setText(newCases);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
                Log.i("thiss","failure to retrieve data covid api");

            }
        });

        RequestQueue queue = Volley.newRequestQueue(ct);
        queue.add(request);
    }
    /**
     * Sets INFOTEXTs and INFO as per button number
     * **/
    static void dynamicInfoDisplay(int buttonNumber,Context ct)
    {
        switch (buttonNumber)
        {
            case 1 : INFOTEXT.setText("Confirmed Cases");
                     INFOTEXT2.setText("Confirmed Cases");
                     INFOONE.setText(totalCases);
                     INFOTWO.setText(newCases);
                     break;
            case 2 : INFOTEXT.setText("Recovered ");
                     INFOTEXT2.setText("Recovered");
                     INFOONE.setText(totalRecovered);
                     setNewRecovered(INFOTWO,ct);
                     break;
            case 3 : INFOTEXT.setText("COVID Deaths");
                     INFOTEXT2.setText("COVID Deaths");
                     INFOONE.setText(totalDeaths);
                     INFOTWO.setText(newDeaths);
                     break;
            default: break;
        }
    }
    /**
     * Sets the data for Intro
     * **/
    static void setIntroCases(final TextView textView,final Context ct)
    {
        final StringRequest request =new StringRequest(url,new com.android.volley.Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject object=new JSONObject(response);
                    //setting the TextView
                    textView.setText(object.getString("cases"));
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("thiss","Failure to retrieve data for intro");
                Toast.makeText(ct,"No Internet Connection", LENGTH_SHORT).show();
                //taking at least totalCases
                textView.setText(covidApi.totalCases);
            }
        });
        RequestQueue queue = Volley.newRequestQueue(ct);
        queue.add(request);
    }
    /**
     * UR what goes ur BAPUS
     * this method not for shiros
     * **/
    public static void setNewRecovered(final TextView INFOTWO, Context ct)
    {
        String recoverUrl="https://disease.sh/v2/all";


        final StringRequest request =new StringRequest(recoverUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    INFOTWO.setText(object.getString("todayRecovered"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                INFOTWO.setText("N/A");
            }
        });
        RequestQueue queue = Volley.newRequestQueue(ct);
        queue.add(request);
    }
}
