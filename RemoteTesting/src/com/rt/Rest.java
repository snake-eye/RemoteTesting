package com.rt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;

import com.rt.db.Product;

public class Rest {

	String base = null;
	public void findjob(Product product) {
		base = "http://" + product.phost + ":" + product.ptom + "/Rest/service/";
		new HttpAsyncTask().execute("findjob", product.pname);
	}
	public void build(Product product){
		new HttpAsyncTask().execute("build", product.pname);
	}

	private class HttpAsyncTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... urls) {
				String url=base;
				String param=null;		//My POST parameters
				if(urls[0].equalsIgnoreCase("findjob")){
					url=base+"findjob";
					param=urls[1];
					publishProgress("Finding Job...");
				}
				else if(urls[0].equalsIgnoreCase("build")){
					url=base+"build";
					param=urls[1];
					publishProgress("Building Job...");
				}
				
				
				InputStream inputStream = null;
	    		String result = "";
	    		int timeout=10000;
	    		try {
	    			HttpParams httpParams = new BasicHttpParams();
	    			HttpConnectionParams.setConnectionTimeout(httpParams,timeout);
	    			HttpConnectionParams.setSoTimeout(httpParams, timeout);
	    			HttpClient client = new DefaultHttpClient(httpParams);
	    			
	    			HttpPost request = new HttpPost(url);
	    			request.setEntity(new StringEntity(param));
	    			request.setHeader("Accept", "application/json");
	    			request.setHeader("Content-type", "application/json");
	    	        HttpResponse response = client.execute(request);
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
	    				result = "Connection problem";
	    		} catch (Exception e) {
	    			Log.d("InputStream", e.getLocalizedMessage());
	    		}
	    		return result;
		}
		@Override
		protected void onProgressUpdate(String... text) {
			Main.setp(text[0]);
		}
		@Override
		protected void onPostExecute(String result) {
			Main.setp(result);
		}
	}
}