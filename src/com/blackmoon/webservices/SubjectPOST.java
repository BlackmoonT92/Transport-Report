package com.blackmoon.webservices;

public interface SubjectPOST {
	public void addOnPostJsonListener(OnPostJsonListener o);
	public void removeOnPostJsonObserver(OnPostJsonListener o);
	public void notifyChange(int type);
}
