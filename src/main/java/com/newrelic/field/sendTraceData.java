package com.newrelic.field;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class sendTraceData extends Thread {
    String insightAPIKey = null;
    String traceAPIFinal = null;
    sendTraceData(String insightAPIKey, String traceAPIFinal){
       this.insightAPIKey = insightAPIKey;
       this.traceAPIFinal = traceAPIFinal;
   }


    public void run() {
        Log.i("dt", insightAPIKey);
        Log.i ("dt", traceAPIFinal);
        try {

            URL urlTraceAPI = new URL("https://trace-api.newrelic.com/trace/v1");
            HttpURLConnection connectionTraceAPI = (HttpURLConnection) urlTraceAPI.openConnection();
            connectionTraceAPI.setRequestProperty("Content-Type", "application/json");
            connectionTraceAPI.setRequestProperty("Api-Key", insightAPIKey);
            connectionTraceAPI.setRequestProperty("Data-Format", "newrelic");
            connectionTraceAPI.setRequestProperty("Data-Format-Version", "1");
            connectionTraceAPI.setRequestMethod("POST");
            connectionTraceAPI.setDoOutput(true);
            OutputStreamWriter out2 = new OutputStreamWriter(connectionTraceAPI.getOutputStream());
            out2.write(traceAPIFinal);
            out2.close();

            Log.i("dt",connectionTraceAPI.getResponseMessage());
            Log.i("dt", String.valueOf(connectionTraceAPI.getResponseCode()));


        }catch(IOException e) {
            e.printStackTrace();
        }
    }

}