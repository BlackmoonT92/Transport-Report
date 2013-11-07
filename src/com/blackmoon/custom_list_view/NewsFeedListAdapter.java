package com.blackmoon.custom_list_view;

import java.util.ArrayList;
import java.util.HashMap;

import com.blackmoon.dto.NewsFeedItem;
import com.blackmoon.egov.MenuLeftFragment;
import com.blackmoon.egov.R;

import android.app.Activity;
import android.content.Context;
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
	private ArrayList<NewsFeedItem> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public NewsFeedListAdapter(Activity a, ArrayList<NewsFeedItem> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
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
		TextView txtTitleNewsFeedItem = (TextView) vi.findViewById(R.id.txtNewsContent);
		TextView txtDescNewsFeedItem = (TextView) vi.findViewById(R.id.txtDescNews);
		ImageView imageNewsFeedItem = (ImageView) vi.findViewById(R.id.imageNewsFeedItem);
		
		NewsFeedItem itemNewsFeed = new NewsFeedItem();
		itemNewsFeed = data.get(position);

		// Setting all value in listview
		txtTitleNewsFeedItem.setText(itemNewsFeed.getTitle());
		txtDescNewsFeedItem.setText(itemNewsFeed.getDesctionItem());
		
		int resID = activity.getResources().getIdentifier(
				itemNewsFeed.getThumb_url(), "drawable",
				activity.getPackageName());

		imageNewsFeedItem.setImageResource(resID);
		return vi;

	}

}
