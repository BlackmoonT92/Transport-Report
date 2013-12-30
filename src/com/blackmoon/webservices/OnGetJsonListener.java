package com.blackmoon.webservices;

public interface OnGetJsonListener {
	public void onGetJsonCompleted(String response);
	public void onGetJsonFail(String response);
}
