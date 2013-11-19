package com.blackmoon.dto;

import org.apache.commons.logging.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Paint.Join;

import com.blackmoon.egov.Config;
import com.blackmoon.local_database.NewsDataSource;
import com.blackmoon.service.NewsfeedService;
import com.blackmoon.service.WebserviceObserver;

public class JSONNewsFeedList implements WebserviceObserver{
	private NewsDataSource dataSouce = new NewsDataSource(Config.activity);
	public static JSONArray listData = new JSONArray();

	public JSONNewsFeedList(){
		
		dataSouce.open();
		listData = dataSouce.getAllNewsFeed();
		
		dataSouce.close();
	}
	
	
	public JSONNewsFeedList(Boolean f){
		NewsfeedService newsfeedService = new NewsfeedService();
		newsfeedService.addObserver(this);
		newsfeedService.getAllNewsfeed();
	}


	@Override
	public void DownloadWebPageTaskComplete(JSONArray jsonArray) {
		listData = jsonArray;
		
//		Log.w("DOWNLOAD - listData", listData);
	}


	@Override
	public void DownloadBitmapComplete(Bitmap bitmap) {
		// TODO Auto-generated method stub
		
	}

	
	
}