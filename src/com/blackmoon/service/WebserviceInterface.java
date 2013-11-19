package com.blackmoon.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONObject;

public interface WebserviceInterface {	
	public void getJsonResult(String request);
	public void getBitmapFromURL(String url) throws MalformedURLException, IOException;
	public void postJson(String url, JSONObject json);
}
