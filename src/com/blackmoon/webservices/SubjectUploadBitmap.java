package com.blackmoon.webservices;

public interface SubjectUploadBitmap {	
	public void addOnUploadBitmapListener(OnUploadBitmapListener o);
	public void removeOnUploadBitmapListener(OnUploadBitmapListener o);
	public void notifyChange(int status);
}
