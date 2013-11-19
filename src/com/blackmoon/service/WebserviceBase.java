package com.blackmoon.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * This class design for basic web-service to manipulate with server
 * 
 * @author anhle
 * 
 */
public class WebserviceBase implements WebserviceInterface, WebserviceSubject {

	/**
	 * For test database as cloud-bees host
	 */
	public static final String serviceURL = "http://mobilehackthon.royalknight.transportreport.greendream-ait.cloudbees.net";
	
	/**
	 * For test database as localhost
	 */
	//	public static final String serviceURL = "http://10.0.2.2:8080/mobilehackthon.royalknight.transportreport";
	public static final int NOTIFY_DOWNLOAD_JSON_COMPLETE = 0;
	public static final int NOTIFY_DOWNLOAD_BITMAP_COMPLETE = 2;

	protected String result;
	protected Bitmap bitmap;
	protected boolean canbeLoginFlag;

	@Override
	public void getJsonResult(String request) {
		result = "[]";
		DownloadWebPageTask task = new DownloadWebPageTask();
		task.execute(new String[] { request });
	}

	@Override
	public void getBitmapFromURL(String url) throws MalformedURLException,
			IOException {

		DownloadBitmapTask task = new DownloadBitmapTask();
		task.execute(new String[] { url });
	}

	@Override
	public void postJson(String url, JSONObject json) {
		PostJsonTask task = new PostJsonTask();
		task.execute(new String[] { url });
	}
	
	/**
	 * This is Asyntask Download web-page source via URL run in Background
	 * 
	 * @author anhle
	 * 
	 */
	protected class PostJsonTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();
				
				HttpPost httpPost = new HttpPost(url);
				
				try {
					HttpResponse execute = client.execute(httpPost);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(String r) {
			result = r;
			notifyChange(NOTIFY_DOWNLOAD_JSON_COMPLETE);
		}
	}
	
	/**
	 * This is Asyntask Download web-page source via URL run in Background
	 * 
	 * @author anhle
	 * 
	 */
	protected class DownloadWebPageTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();
				
				HttpGet httpGet = new HttpGet(url);
				
				try {
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(String r) {
			result = r;
			notifyChange(NOTIFY_DOWNLOAD_JSON_COMPLETE);
		}
	}

	/**
	 * This is Asyntask Download Bitmap via URL run in Background
	 * 
	 * @author anhle
	 * 
	 */
	protected class DownloadBitmapTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			for (String url : urls) {

				try {
					bitmap = BitmapFactory.decodeStream(((InputStream) new URL(
							url).getContent()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			return "ok";
		}

		@Override
		protected void onPostExecute(String r) {
			notifyChange(NOTIFY_DOWNLOAD_JSON_COMPLETE);
		}
	}

	protected Vector<WebserviceObserver> observers = new Vector<WebserviceObserver>();

	@Override
	public void addObserver(WebserviceObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(WebserviceObserver o) {
		observers.remove(o);
	}

	@Override
	public void notifyChange(int notifyType) {
		for (WebserviceObserver o : observers) {
			switch (notifyType) {
			case NOTIFY_DOWNLOAD_JSON_COMPLETE:
				

				
				try {
					o.DownloadWebPageTaskComplete(new JSONArray(result));
				} catch (Exception e) {

				}

			case NOTIFY_DOWNLOAD_BITMAP_COMPLETE:
				o.DownloadBitmapComplete(bitmap);
			}
		}

	}

}
