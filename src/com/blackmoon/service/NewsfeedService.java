package com.blackmoon.service;

import org.json.JSONArray;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.blackmoon.dto.CName;
import com.blackmoon.uploadservice.UploadObserver;
import com.blackmoon.uploadservice.UploadToServer;

public class NewsfeedService extends WebserviceBase implements UploadObserver {
	private JSONObject mNewsfeed = new JSONObject();

	public void postNewService(JSONObject newsfeed) {
		// Setting info for upload image
		mNewsfeed = newsfeed;

		// Upload images to server
		try {
			UploadToServer imageUploader = new UploadToServer();
			imageUploader.addUploadObserver(this);
			String imageFile = mNewsfeed.getString(CName.KEY_NEWSFEED_URL_IMAGES);
			Uri imageUri = Uri.parse(imageFile);
			imageUploader.uploadImage(imageUri);
			
		} catch (Exception e) {
			// Notify upload image fail
			notifyChange(NOTIFY_DOWNLOAD_JSON_COMPLETE);
			
		}

	}

	@Override
	public void UploadImageBitmapComplete(String url) {
		// Upload image completed
		try {
			// Get link image from server and replace to newsfeed
			mNewsfeed.put(CName.KEY_NEWSFEED_URL_IMAGES, url);
			

			Log.w("POST NEW FEED NOW", mNewsfeed.toString());
			
			// Create request string to post new newsfeed to server
			String request = serviceURL
					+ "/service/newsfeed/insertNewsfeed?"
					+ "newsfeed_content=" + mNewsfeed.getString("newsfeed_content")
					+ "&province_name=" + mNewsfeed.getString("province_name")
					+ "&district_name=" + mNewsfeed.getString("district_name")
					+ "&newsfeed_url_images=" + url
					+ "&user_name=" + mNewsfeed.getString(CName.KEY_USER_NAME)
					+ "&address_coordinated_x=" + mNewsfeed.getString("address_coordinated_x")
					+ "&address_coordinated_y=" + mNewsfeed.getString("address_coordinated_y")
					+ "&newsfeed_tilte=" + mNewsfeed.getString("newsfeed_tilte")
					+ "&newsfeed_time=" + mNewsfeed.getString("newsfeed_time")
					+ "&address_detail=" + mNewsfeed.getString("address_detail");
					

			request = request.replace(" ", "%20");
			Log.w("REQUEST-----", request);
			
			getJsonResult(request);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public JSONArray getAllNewsfeed() {
		String request = serviceURL + "/service/newsfeed/search_newsfeed_all";
		getJsonResult(request);		
		return null;
	}
}
