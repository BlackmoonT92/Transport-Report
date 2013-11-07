package com.blackmoon.dto;

public class GPSCoordinates {
	private String lon;
	private String lat;

	public GPSCoordinates() {
		lon = "10.882723";
		lat = "106.759275";
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

}
