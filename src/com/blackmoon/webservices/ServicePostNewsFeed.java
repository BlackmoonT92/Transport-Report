package com.blackmoon.webservices;

import org.json.JSONObject;


public class ServicePostNewsFeed extends WebserviceBasePOST{
	public void postNewReport(String reportInfo) {
		String Url = "http://egov047.juddy.vn/api/Place/Insert?";
		postObject(reportInfo);
	}
}
