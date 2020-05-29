Adding Distibuted tracing to New Rleic for Android
Classes send the correct trace data to New Rleic and a formated newrelic header to pass to other support New Relic Services. This dies require the developer to add the tracing to the code.  The example I will use is with OKHttp but should work with all of the HTTP libraries.

 - mobileApplicationID is found when a mobile app is created and the URL last set of numbers is the mobileApplicationID.
 -  accountId is found  after a successful login on the URL as the first set of numbers.
 - insights InsertId is generated in the insight user interface under manage API
 - appname is the text name of the app. best practive is to name it the same at in the Mobile Monitoring UI
 - startTrace(URL) is the URL to be used in the request.  PII is important so all URL parameters are moved.
 - addCustom(attribute_name, value) enables the user to add any extra data as it will display in the Distibuted trace UI
 - StopTrace() will stop the trace and send the data to New Relic.




        OkHttpClient client = new OkHttpClient();
        NrDistributedTrace nrdt = new NrDistributedTrace("mobileApplicationID", "accountId","insightsInsertId",NewRelic.currentSessionId(),"appname");  // New Relic Distibuted tracing Code
	    String nrHeader = nrdt.startTrace(URL);  // New Relic Distibuted tracing Code
	    /* End Start DT */
	    Request.Builder builder = new Request.Builder();
	    builder.addHeader("newrelic", nrHeader);
	    Log.i("dt", nrHeader);
	    builder.url(URL);
	    Request request = builder.build();
	        try {
		        Response response = client.newCall(request).execute();
		        nrdt.addCustom("responseStatus" , response.code());
		        nrdt.addCustom("weather", "rainy");  // New Relic Distibuted tracing Code
		        nrdt.stopTrace();  // New Relic Distibuted tracing Code
		        return response.body().string();
		     } catch (Exception e) {
		        e.printStackTrace();   }




