package com.blackmoon.egov;

import com.blackmoon.dto.NewsFeedItem;

import android.app.Activity;

public class Config {
	public static Activity activity;
	// item is clicked to view detail
	public static NewsFeedItem itemClickedToViewDetail;
	
	//XML note keys are order to get data from server
	// XML node keys
		public static final String KEY_ROOM = "room"; // parent node
		public static final String KEY_ROOM_NAME = "room_name";
		public static final String KEY_DATE_COME = "date_come";
		public static final String KEY_DEBIT = "debit";
		public static final String KEY_ROOM_LEVEL = "room_lvl";
		public static final String KEY_THUMB_URL = "thumb_url";
		
		
		
	
}
