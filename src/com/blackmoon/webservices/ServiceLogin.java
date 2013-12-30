package com.blackmoon.webservices;

import org.json.JSONObject;

public class ServiceLogin extends WebserviceBasePOST {
	
	/**
	 * Login to 4cars system
	 * 
	 * You must be use addOnPostJsonListener(OnPostJsonListener) for this Object
	 * to handle when POST completed or fail
	 * 
	 * @param loginInfo
	 */
	public void logon(String userName, String password) {
		userName = userName.replace(" ", "%20");
		String request = "http://http://egov047.juddy.vn/api/User/Login?userName=" + userName + "&pass=" + password;
		postObject(request);
	}
}
