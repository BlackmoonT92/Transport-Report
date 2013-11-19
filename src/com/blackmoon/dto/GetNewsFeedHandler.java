package com.blackmoon.dto;

import android.os.Handler;

public class GetNewsFeedHandler {
	private String url;
	private Handler mHandler;
	private Runnable pRunnable;
	private String data;
	private int stausCode;

	public GetNewsFeedHandler(String newUrl, String newData) {
		url = newUrl;
		data = newData;
		mHandler = new Handler();
	}

	public void start(Runnable newRun) {
		pRunnable = newRun;
		processRequest.start();
	}

	private Thread processRequest = new Thread() {
		public void run() {
			// Do you request here...
			if (pRunnable == null || mHandler == null)
				return;
			mHandler.post(pRunnable);
		}
	};
}