package com.blackmoon.egov;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.blackmoon.custom_list_view.NewsFeedListAdapter;
import com.blackmoon.dto.NewsFeedItem;

import android.annotation.TargetApi;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class NewsFeedFragment extends Fragment {
	// ==============================================
	// VARIABLES AND CONSTANTS
	// ==============================================

	ListView list;
	private NewsFeedListAdapter listAdapter;
	Fragment newContent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Config.activity.setTitle(R.string.news_feed);
		// Config.activity.getActionBar().setIcon(R.drawable.newsfeed);
		// actionBar.getSupportActionBar().setIcon(R.drawable.newsfeed);

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = null;
		view = inflater.inflate(R.layout.fragment_newsfeed, container, false);
		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// set current fragment for back button
		Config.currentFragment = 1;

		list = (ListView) getView().findViewById(R.id.listItem);
		// load data and show here

		final ArrayList<NewsFeedItem> data = new ArrayList<NewsFeedItem>();
		for (int i = 0; i < 10; i++) {
			NewsFeedItem newsFeed = new NewsFeedItem();
			newsFeed.setTitle(newsFeed.getTitle() + " " + i);
			data.add(newsFeed);
		}
		listAdapter = new NewsFeedListAdapter(Config.activity, data);
		list.setAdapter(listAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// set currtent Fragment for backButton

				NewsFeedItem itemClicked = data.get(position);
				Config.itemClickedToViewDetail = itemClicked;
				newContent = new NewsFeedFragmentDetail(itemClicked);
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.content_frame, newContent);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				// add to back track
				ft.addToBackStack("NewsFeed");
				ft.commit();
			}
		});

	}

	@Override
	public void onDestroyView() {
		Config.currentFragment = 0;
		super.onDestroyView();
	}

}
