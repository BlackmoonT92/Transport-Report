package com.blackmoon.dto;

import java.util.List;

import android.location.Location;
import android.location.LocationManager;

public class NewsFeedItem {
	// =========================================
	// VARIBALES
	// =========================================
	private String item_Id; 	//guid
	private String title;
	private String userName;
	private String timePosted;
	private String address;
	private String thumb_url; // link hinh
	private String newsContent;
	private Location location;
	private String[] tagRegion;

	// CONTRUCTORS
	public NewsFeedItem() {
		title = "[Chơi Ngu] Hai thằng ngu té cống chết vì chăm chú ngắm gái xấu";
		userName = "blackmoonT92";
		timePosted = "30/12/1992, 69:96";
		address = "Thủ đức";
		setLocation(null);
		thumb_url = "";
		newsContent = " A group of Canadians was traveling by tour bus"
				+ " through Holland. As they stopped at a cheese farm, "
				+ "a young guide led them through a process of cheese "
				+ "making, explaining that goat's milk was used. "
				+ "She showed the group a lovely hillside where many ";
		tagRegion = new String[5];
		location = new Location(LocationManager.NETWORK_PROVIDER);

	}

	public String getDesctionItem() {
		return "Gửi bởi: " + getUserName() + " - " + getTimePosted()
				+ " - Tại: " + getAddress();

	}

	// =========================================
	// GETTERS AND SETTERS
	// =========================================
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTimePosted() {
		return timePosted;
	}

	public void setTimePosted(String timePosted) {
		this.timePosted = timePosted;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String located) {
		this.address = located;
	}

	public String getThumb_url() {
		return thumb_url;
	}

	public void setThumb_url(String thumb_url) {
		this.thumb_url = thumb_url;
	}

	public String getNewsContent() {
		return newsContent;
	}

	public void setNewsContent(String desciption) {
		this.newsContent = desciption;
	}

	public String getItem_Id() {
		return item_Id;
	}

	public void setItem_Id(String item_Id) {
		this.item_Id = item_Id;
	}

	public String[] getTagRegion() {
		return tagRegion;
	}

	public void setTagRegion(String[] tagRegion) {
		this.tagRegion = tagRegion;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
