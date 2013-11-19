package com.blackmoon.uploadservice;

public interface UploadSubject {	
	public void addUploadObserver(UploadObserver o);
	public void removeUploadObserver(UploadObserver o);
	public void notifyChange();
}
