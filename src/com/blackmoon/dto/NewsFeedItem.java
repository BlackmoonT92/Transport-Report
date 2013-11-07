package com.blackmoon.dto;

public class NewsFeedItem {
	// =========================================
	// VARIBALES
	// =========================================
	private String item_Id;
	private String title;
	private String userName;
	private String timePosted;
	private String located;
	private String thumb_url;
	private String newsContent;
	private GPSCoordinates coordinates;

	// CONTRUCTORS
	public NewsFeedItem() {
		title = "[Chơi Ngu] Hai thằng ngu té cống chết vì chăm chú ngắm gái xấu";
		userName = "blackmoonT92";
		timePosted = "30/12/1992, 69:96";
		located = "Thủ đức";
		setCoordinates(new GPSCoordinates());
		thumb_url = "thum_url";
		newsContent = " A group of Canadians was traveling by tour bus" +
				" through Holland. As they stopped at a cheese farm, " +
				"a young guide led them through a process of cheese " +
				"making, explaining that goat's milk was used. " +
				"She showed the group a lovely hillside where many ";

	}

	public String getDesctionItem(){
		return "Gửi bởi: "+getUserName() + " - " + getTimePosted() + " - Tại: " + getLocated();
		
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

	public String getLocated() {
		return located;
	}

	public void setLocated(String located) {
		this.located = located;
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

	public GPSCoordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(GPSCoordinates coordinates) {
		this.coordinates = coordinates;
	}

}
