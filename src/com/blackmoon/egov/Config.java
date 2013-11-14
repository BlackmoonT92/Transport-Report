package com.blackmoon.egov;

import com.actionbarsherlock.app.ActionBar;
import com.blackmoon.dto.NewsFeedItem;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class Config {
	public static Activity activity;
	public ActionBar actionBar;
	// item is clicked to view detail
	public static NewsFeedItem itemClickedToViewDetail;
	public static int currentFragment = 0;
	public static boolean isPreview = false;
	
	// boolean is true if post is opened by btnCamera in actionbar
	public static boolean flagOpenCameraFirst = false;
	
	//XML note keys are order to get data from server
	// XML node keys
		public static final String KEY_ROOM = "room"; // parent node
		public static final String KEY_ROOM_NAME = "room_name";
		public static final String KEY_DATE_COME = "date_come";
		public static final String KEY_DEBIT = "debit";
		public static final String KEY_ROOM_LEVEL = "room_lvl";
		public static final String KEY_THUMB_URL = "thumb_url";
		
		
		
	
}
