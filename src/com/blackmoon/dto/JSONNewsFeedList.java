package com.blackmoon.dto;

import org.json.JSONArray;
import android.app.ProgressDialog;
import com.blackmoon.egov.Config;
import com.blackmoon.local_database.NewsDataSource;

public class JSONNewsFeedList  {
	private NewsDataSource dataSouce = new NewsDataSource(Config.activity);
	public static JSONArray listData = new JSONArray();
	ProgressDialog progressDialog;

	public JSONNewsFeedList() {

		dataSouce.open();
		listData = dataSouce.getAllNewsFeed();

		dataSouce.close();
	}



}