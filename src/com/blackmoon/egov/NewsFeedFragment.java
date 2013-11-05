package com.blackmoon.egov;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class NewsFeedFragment extends Fragment {
	// ==============================================
	// VARIABLES AND CONSTANTS
	// ==============================================
	ListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Config.activity.setTitle(R.string.news_feed);
		Config.activity.getActionBar().setIcon(R.drawable.newsfeed);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_newsfeed, container, false);

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		list = (ListView) getView().findViewById(R.id.listItem);
		//load data and show here
	}

}
