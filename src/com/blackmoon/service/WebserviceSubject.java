package com.blackmoon.service;

public interface WebserviceSubject {	
	public void addObserver(WebserviceObserver o);
	public void removeObserver(WebserviceObserver o);
	public void notifyChange(int notifyType);
}
