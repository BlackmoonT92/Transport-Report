package com.blackmoon.webservices;


public class ServiceGetNewsFeed extends WebserviceBaseGET {

	public void getAllNewsfeed() {
		String request = "http://egov047.juddy.vn/api/place/getall";
		getJSONObject(request);

	}

}
