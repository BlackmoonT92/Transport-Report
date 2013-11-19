package com.blackmoon.custom_list_view;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.blackmoon.dto.CName;
import com.blackmoon.dto.JSONNewsFeedList;
import com.blackmoon.egov.MenuLeftFragment;
import com.blackmoon.egov.R;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsFeedListAdapter extends BaseAdapter {
	// ==========================================
	// VARIABLES
	// ==========================================

	private Activity activity;
	// private ArrayList<NewsFeedItem> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	private JSONArray data = null;

	public NewsFeedListAdapter(Activity a, JSONArray d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		try {
			Log.w("INTO DATA", data.optJSONObject(0).getString(CName.KEY_NEWSFEED_CONTENT).toString().trim());
			Log.w("INTO DATA", data.optJSONObject(0).getString(CName.KEY_PROVINCE_NAME).toString().trim());
			Log.w("INTO DATA", data.optJSONObject(0).getString(CName.KEY_NEWSFEED_TILTE).toString().trim());
			Log.w("INTO DATA", data.optJSONObject(0).getString(CName.KEY_DISTRICT_NAME).toString().trim());
			Log.w("INTO DATA", data.optJSONObject(0).getString(CName.KEY_ADDRESS_COORDINATED_X).toString().trim());
			Log.w("INTO DATA", data.optJSONObject(0).getString(CName.KEY_ADDRESS_COORDINATED_Y).toString().trim());
			Log.w("INTO DATA", data.optJSONObject(0).getString(CName.KEY_NEWSFEED_TIME).toString().trim());
			Log.w("INTO DATA", data.optJSONObject(0).getString(CName.KEY_ADDRESS_DETAIL).toString().trim());
			Log.w("INTO DATA", data.optJSONObject(0).getString(CName.KEY_USER_NAME).toString().trim());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.row_newsfeed_item, null);
		}
		
		TextView txtTitleNewsFeedItem = (TextView) vi
				.findViewById(R.id.txtNewsContent);
		TextView txtDescNewsFeedItem = (TextView) vi
				.findViewById(R.id.txtDescNews);
		ImageView imageNewsFeedItem = (ImageView) vi
				.findViewById(R.id.imageAttach);

		JSONObject itemNewsFeed = new JSONObject();
		
		try {
			String strusername = data.optJSONObject(position).getString(CName.KEY_USER_NAME).toString().trim();
			String strnewscontent = data.optJSONObject(position).getString(CName.KEY_NEWSFEED_CONTENT).toString().trim();
			String strprovincename =  data.optJSONObject(position).getString(CName.KEY_PROVINCE_NAME).toString().trim();
			String strnewstitle = data.optJSONObject(position).getString(CName.KEY_NEWSFEED_TILTE).toString().trim();
			String strdistricname =  data.optJSONObject(position).getString(CName.KEY_DISTRICT_NAME).toString().trim();
			String strcoorx= data.optJSONObject(position).getString(CName.KEY_ADDRESS_COORDINATED_X).toString().trim();
			String strcoory = data.optJSONObject(position).getString(CName.KEY_ADDRESS_COORDINATED_Y).toString().trim();
			String strnewstime = data.optJSONObject(position).getString(CName.KEY_NEWSFEED_TIME).toString().trim();
			String stradddetail = data.optJSONObject(position).getString(CName.KEY_ADDRESS_DETAIL).toString().trim();
			
			// Setting all value in listview
			txtTitleNewsFeedItem.setText(strnewstitle);
			
			String descNews = "Gửi bởi: "
					+ strusername.toString().trim() + " - "
					+ strnewstime.toString().trim()
					+ " - Tại: "
					+ strdistricname.toString().trim();
			txtDescNewsFeedItem.setText(descNews.toString().trim() + "??????????");

			// check uri != null => download and set image
			String uri;

			// ==== Load and scale image to fit screen and reduce memory
			uri = itemNewsFeed.getString(CName.KEY_NEWSFEED_URL_IMAGES).toString().trim();
			if (uri != null) {
				imageNewsFeedItem.setImageURI(Uri.parse(uri));
			} else {
				imageNewsFeedItem.setImageResource(R.drawable.thumb_url);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vi;

	}

}
