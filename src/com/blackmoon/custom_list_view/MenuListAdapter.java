package com.blackmoon.custom_list_view;

import java.util.ArrayList;
import java.util.HashMap;

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

public class MenuListAdapter extends BaseAdapter {

	// ==========================================
	// VARIABLES
	// ==========================================

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public MenuListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
			vi = inflater.inflate(R.layout.row_menu, null);
		}
		TextView titleMenu = (TextView) vi.findViewById(R.id.row_title);
		ImageView iconMenu = (ImageView) vi.findViewById(R.id.row_icon);

		HashMap<String, String> itemMenu = new HashMap<String, String>();
		itemMenu = data.get(position);

		// Setting all value in listview
		titleMenu.setText(itemMenu.get(MenuLeftFragment.KEY_TITLE));
		int resID = activity.getResources().getIdentifier(
				itemMenu.get(MenuLeftFragment.KEY_ICON), "drawable",
				activity.getPackageName());

		iconMenu.setImageResource(resID);
		return vi;

	}

}
