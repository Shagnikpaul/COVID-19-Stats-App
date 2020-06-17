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
    static TextView INFOONE,INFOTWO,INFOTEXT;

    private static String url="https://disease.sh/v2/all";

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


                    //displaying data
                    INFOONE.setText("New Cases : "+newCases);

                    INFOTWO.setText("Total Cases : "+totalCases);

                    INFOTEXT.setText("Confirmed");




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
                        //displaying data
                        INFOONE.setText("New Cases : "+newCases);
                        INFOTWO.setText("Total Cases : "+totalCases);
                        INFOTEXT.setText("Confirmed");
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
    static void dynamicInfoDisplay(int buttonNumber)
    {
        switch (buttonNumber)
        {
            case 1 : INFOTEXT.setText("Confirmed");
                     INFOONE.setText("Total Confirmed cases "+totalCases);
                     INFOTWO.setText("New Confiremed Cases "+newCases);
                     break;
            case 2 : INFOTEXT.setText("Recovered");
                     INFOONE.setText("Total Recovered cases "+totalRecovered);
                     INFOTWO.setText("NOT AVAILABLE");
                     break;
            case 3 : INFOTEXT.setText("Deaths");
                     INFOONE.setText("Total Deaths "+totalDeaths);
                     INFOTWO.setText("New Deaths "+newDeaths);
                     break;
            default: break;
        }
    }
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
}
