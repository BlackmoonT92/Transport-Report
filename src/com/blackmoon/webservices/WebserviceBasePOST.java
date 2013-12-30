package com.blackmoon.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class WebserviceBasePOST implements SubjectPOST {

	public static final int NOTIFY_POST_JSON_COMPLETED = 0;
	public static final int NOTIFY_POST_JSON_FAIL = 1;

	protected Vector<OnPostJsonListener> mOnPostJsonListeners = new Vector<OnPostJsonListener>();

	protected String mResponse;
	protected JSONObject mRequestJSONObject = null;

	protected void postObject(String request) {
		mResponse = "[]";
		PostJSONObjectTask task = new PostJSONObjectTask();
		task.execute(new String[] { request });

	}

	/**
	 * This is Asyntask Download web-page source via URL run in Background
	 * 
	 * @author anhle
	 * 
	 */
	protected class PostJSONObjectTask extends AsyncTask<String, Void, String> {
		Boolean isFail = true;
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
			mResponse = r;
			Log.i("END POST JSON TASK", mResponse);
			
			if (isFail) {
				notifyChange(NOTIFY_POST_JSON_FAIL);
			} else {
				notifyChange(NOTIFY_POST_JSON_COMPLETED);
			}
		}

	}
	
	@Override
	public void addOnPostJsonListener(OnPostJsonListener o) {
		// TODO Auto-generated method stub
		mOnPostJsonListeners.add(o);
	}

	@Override
	public void removeOnPostJsonObserver(OnPostJsonListener o) {
		// TODO Auto-generated method stub
		mOnPostJsonListeners.remove(o);
	}

	@Override
	public void notifyChange(int type) {
		// TODO Auto-generated method stub
		for (OnPostJsonListener o : mOnPostJsonListeners) {
			switch (type) {

			case NOTIFY_POST_JSON_COMPLETED:
				o.onPostJsonCompleted(mResponse);
				break;

			case NOTIFY_POST_JSON_FAIL:
				o.onPostJsonFail(mResponse);
				break;
			}
		}
	}
}
