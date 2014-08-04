package com.rt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class RestUntouched extends Activity {

	EditText etResponse;
	TextView tvIsConnected;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.rest);
//		etResponse = (EditText) findViewById(R.id.etResponse);
//		tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
//		NetworkInfo networkInfo = ((ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
//	    if (networkInfo != null && networkInfo.isConnected()) {
//			tvIsConnected.setBackgroundColor(0xFF00CC00);
//			tvIsConnected.setText("You are conncted");
//        }
//		else{
//			tvIsConnected.setText("You are NOT conncted");
//		}
//		new HttpAsyncTask().execute("http://192.168.150.1:8080/Rest/rest/service/findjob");
	}

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
    		String result = "";
    		try {
    			HttpClient client = new DefaultHttpClient();
    			HttpResponse response = client.execute(new HttpGet(urls[0]));
    			inputStream = response.getEntity().getContent();
    			//if(response.getStatusLine().getStatusCode()==200){
    			if(inputStream != null){
    				BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
    		        String line = "";
    		        while((line = bufferedReader.readLine()) != null)
    		            result += line;
    		        inputStream.close();
    		    }
    			else
    				result = "Did not work!";
    		} catch (Exception e) {
    			Log.d("InputStream", e.getLocalizedMessage());
    		}
    		return result;
        }
        @Override
        protected void onPostExecute(String result) {
        	Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
        	etResponse.setText(result);
       }
    }
}