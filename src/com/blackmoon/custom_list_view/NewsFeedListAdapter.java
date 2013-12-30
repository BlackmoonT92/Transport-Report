package com.blackmoon.custom_list_view;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.blackmoon.dto.CName;
import com.blackmoon.dto.JSONNewsFeedList;
import com.blackmoon.egov.Config;
import com.blackmoon.egov.NewsFeedFragment;
import com.blackmoon.egov.NewsFeedFragmentDetail;
import com.blackmoon.egov.R;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
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
		String myURLImage[];
		private JSONArray data = JSONNewsFeedList.listData;

		public NewsFeedListAdapter(Activity a, JSONArray d) {
			activity = a;
			// data = d;
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
				itemNewsFeed = data.getJSONObject(position);

				// Setting all value in listview
				txtTitleNewsFeedItem.setText(itemNewsFeed
						.getString(CName.KEY_NEWSFEED_TILTE));
				String descNews = "Gửi bởi: " + "BlackmonT92" + " - "
						+ itemNewsFeed.getString(CName.KEY_NEWSFEED_TIME)
						+ " - Tại: "
						+ itemNewsFeed.getString(CName.KEY_ADDRESS_DETAIL);
				txtDescNewsFeedItem.setText(descNews);

				// check uri != null => download and set image
				String uri= itemNewsFeed.getString(CName.KEY_NEWSFEED_URL_IMAGES);
				String linkarray = uri.substring(1, uri.length()-1);
				Log.d("URI", linkarray +"");
				if(linkarray.length()>0){
					String imgUri[] = linkarray.split(",");
					for(int i = 0 ; i< imgUri.length ; i++){
						myURLImage[i] = imgUri[i].substring(1, imgUri[i].length()-1);
						Log.d("myURLImage", myURLImage[i] +"");
					}
				}
				
				if (false/*uri != null*/) {
					imageNewsFeedItem.setImageURI(Uri.parse(uri));
				} else {
					imageNewsFeedItem.setImageResource(R.drawable.thumb_url);
				}

			} catch (JSONException e) {
				imageNewsFeedItem.setImageResource(R.drawable.thumb_url);
				e.printStackTrace();
			}
			return vi;

		}

}
