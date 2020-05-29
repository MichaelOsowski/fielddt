package com.newrelic.field;

import android.util.Base64;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

import static android.util.Base64.NO_WRAP;

public class NrDistributedTrace {
    private String appName = "";
    private String appID = "";
    private String accountId = "";
    private String insightAPIKey = "";
    private String requestURL = "";
    private String sessionId = null;
    private long startTime;
    private long stopTime;
    private String traceId = null;
    private String spanId = null;
    private JSONObject nrTraceApiAttribures = new JSONObject();
    private JSONObject nrTraceApiCAtributes = new JSONObject();
    private JSONObject nrOtherAttibutes = new JSONObject();
    private JSONObject nrTraceApiLead = new JSONObject();
    private JSONObject nrTraceCommon = new JSONObject();
    private JSONArray nrTraceApiSpans = new JSONArray();



    public NrDistributedTrace(String NrMobileAppID, String accountId,String NrInsightAPIKey, String sessionId, String appName){
     this.appID = NrMobileAppID;
     this.accountId = accountId;
     this.insightAPIKey = NrInsightAPIKey;
     this.sessionId = sessionId;
     this.appName = appName;

 }

    public String startTrace(String requestURL)  {

        startTime = System.currentTimeMillis();
        String headerDTEncoded = null;
        byte[] headerByte = null;
        String[] requestURLarray = null;
        traceId = getUniqueID(0);
        spanId = getUniqueID(16);
        if (requestURL != null){
            try {
                requestURL = URLDecoder.decode(requestURL, "UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (requestURL.contains("?")){
                requestURLarray = requestURL.split("\\?");
                requestURL = requestURLarray[0];

            }
        }
        this.requestURL = requestURL;
        String headerDT = "{" +
                "\"v\":" + "[0,1]," +
                "\"d\": {" +
                "\"ty\": \"App\","      +
                "\"ac\": \""+ accountId +"\"," +
                "\"ap\": \""+ appID +"\"," +
                "\"id\": \""+ spanId + "\"," +
                "\"tr\": \""+traceId+ "\"," +
                "\"ti\": "+ startTime + ""+
                "} " +
                "}";

        try{
            headerByte = headerDT.getBytes("UTF-8");
        } catch(Exception e){
            e.printStackTrace();
        }

        headerDTEncoded = Base64.encodeToString(headerByte,NO_WRAP);

        return headerDTEncoded;
    }

    public void addCustom(String attributeName, String value) throws JSONException {

        nrTraceApiAttribures.put(attributeName, value);
    }

    public void addCustom(String attributeName, int value) throws JSONException {

        nrTraceApiAttribures.put(attributeName, value);
    }

    public void addCustom(String attributeName, long value) throws JSONException {

        nrTraceApiAttribures.put(attributeName, value);
    }

    public void addCustom(String attributeName, Float value) throws JSONException {

        nrTraceApiAttribures.put(attributeName, value);
    }


 public void stopTrace(){


     stopTime = System.currentTimeMillis();
     String traceAPIFinal = "";
     double timeResult;
     timeResult = stopTime - startTime;
     timeResult = timeResult/1000;



     try{
         nrTraceApiAttribures.put("duration.ms", timeResult);
         if (sessionId != null) {
             nrTraceApiAttribures.put("sessionId", sessionId);
         }
         nrTraceApiAttribures.put("source", "mobile");
         nrTraceApiAttribures.put("name", requestURL);

         nrTraceApiLead.put("trace.id",traceId);
         nrTraceApiLead.put("id", spanId);
         nrTraceApiLead.put("attributes", nrTraceApiAttribures);
         nrTraceApiSpans.put(nrTraceApiLead);


         nrTraceApiCAtributes.put("service.name", appName);

         nrOtherAttibutes.put ("attributes",nrTraceApiCAtributes);
         nrTraceCommon.put("common", nrOtherAttibutes);
         nrTraceCommon.put("spans", nrTraceApiSpans);
         traceAPIFinal = "[" +  nrTraceCommon.toString() + "]";
         Log.i("dt",traceAPIFinal);

     }catch(JSONException e) {
         e.printStackTrace();
     }

     sendTraceData sdt = new sendTraceData(insightAPIKey,traceAPIFinal);
     sdt.run();


 }

 private String getUniqueID(int length){
     String uniqueID = UUID.randomUUID().toString();
     String Smalluuid = uniqueID.replace("-", "");
     String lowercased = Smalluuid.toLowerCase();
     String resultuuid = lowercased.substring(length,lowercased.length());
     return resultuuid;
 }

}