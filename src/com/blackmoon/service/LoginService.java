package com.blackmoon.service;

public class LoginService extends WebserviceBase {
	
	public void login(String userName, String password) {
		userName = userName.replace(" ", "%20");
		String request = serviceURL + "/service/user/login?username=" + userName + "&password=" + password;
		getJsonResult(request);
	}
}
