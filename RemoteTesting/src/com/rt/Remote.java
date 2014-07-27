package com.rt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;

class Remote {
	Socket s = null;
	BufferedReader in = null;
	PrintWriter out = null;
	boolean connected=false;
	String resp=null;
	void connect(String...s){
		new Connect().execute(s[0],s[1]);		
	}
	void send(String s){
		if(connected)
			new Send().execute(s);
	}
	
	void disconnect(){
		if(connected)
			new Disconnect().execute();
	}
	
		private class Connect extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			 // Calls onProgressUpdate()
			try {
				publishProgress("Connecting...");
				s = new Socket(params[0], Integer.parseInt(params[1]));
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
				resp="Connected";
				connected=true;
			} catch (UnknownHostException uhe) {
				resp="UnknownHost: "+params[0];
				s = null;
			} catch (IOException ioe) {
				resp="Cant connect to "+params[0] +" at "+params[1];
				s = null;
			} catch (Exception e) {
				resp = e.getMessage();
			} 
			return resp;
		}

		@Override
		protected void onPostExecute(String result) {
			//if(
					Main.setp(result);
					//){this.cancel(true);Main.setp("Time out");}
		}
		@Override
		protected void onProgressUpdate(String... text) {
			Main.setp(text[0]);
		}
	}

		private class Send extends AsyncTask<String, String, String> {
			@Override
			protected String doInBackground(String... params) {
				publishProgress("Sending..."); // Calls onProgressUpdate()
				try {
					out.println(params[0]);
	                out.flush();
	                resp=in.readLine();
	            } catch (IOException ioe) {
	                resp="Connection interrupted";
	            } catch (Exception e) {
					resp = e.getMessage();
				} 
				return resp;
			}

			@Override
			protected void onPostExecute(String result) {
				Main.setp(result);
			}
			@Override
			protected void onProgressUpdate(String... text) {
				Main.setp(text[0]);
			}
		}

		private class Disconnect extends AsyncTask<String, String, String> {
			@Override
			protected String doInBackground(String... params) {
				publishProgress("Disconnecting..."); // Calls onProgressUpdate()
				try {
					out.println("quit");
	                out.flush();
	                in.close();
	                out.close();
	                s.close();
	                resp="Disconnected";
	            } catch (Exception e) {
					resp = e.getMessage();
				} 
				return resp;
			}

			@Override
			protected void onPostExecute(String result) {
				Main.setp(result);
			}
			@Override
			protected void onProgressUpdate(String... text) {
				Main.setp(text[0]);
			}
		}






}//Remote



