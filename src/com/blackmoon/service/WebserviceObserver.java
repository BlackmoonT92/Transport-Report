package com.blackmoon.service;

import org.json.JSONArray;

import android.graphics.Bitmap;

public interface WebserviceObserver {
	public void DownloadWebPageTaskComplete(JSONArray jsonArray);
	public void DownloadBitmapComplete(Bitmap bitmap);
}
