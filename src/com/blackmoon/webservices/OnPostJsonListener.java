package com.blackmoon.webservices;

public interface OnPostJsonListener {
	public void onPostJsonCompleted(String response);
	public void onPostJsonFail(String response);
}
