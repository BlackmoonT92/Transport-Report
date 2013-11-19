package com.blackmoon.egov;

import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class Config {
	public static Activity activity;
	public ActionBar actionBar;
	// item is clicked to view detail
	public static JSONObject itemClicked;
	public static int currentFragment = 0;
	public static boolean isPreview = false;
	
	// boolean is true if post is opened by btnCamera in actionbar
	public static boolean flagOpenCameraFirst = false;
	public static boolean isLogin = false;
	public static final String KEY_POSTED = "posted";
	public static String username = "Login";
	public static JSONObject objectPreview = new JSONObject();
	
	
		
		
		
	
}
