package com.blackmoon.egov;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.blackmoon.custom_list_view.NewsFeedListAdapter;
import com.blackmoon.dto.JSONNewsFeedList;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class HistoryFragment extends Fragment {
	ListView list;
	private NewsFeedListAdapter listAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Config.activity.setTitle(R.string.history);
		// Config.activity.getActionBar().setIcon(R.drawable.history);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_history, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// set current fragment
		Config.currentFragment = 2;
		list = (ListView) getView().findViewById(R.id.listHistory);
		// list all data form user follow
		JSONNewsFeedList listJson = new JSONNewsFeedList();
		final JSONArray dataNewsFeed = listJson.listData;
		listAdapter = new NewsFeedListAdapter(Config.activity, dataNewsFeed);
		list.setAdapter(listAdapter);
		list.setAdapter(listAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// set currtent Fragment for backButton

				// NewsFeedItem itemClicked =
				// data.get(position);
				// Config.itemClickedToViewDetail =
				// itemClicked;
				// covert to JSON
				JSONObject item = null;
				try {
					item = (JSONObject) dataNewsFeed.get(position);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Config.itemClicked = item;
				Fragment newContent = new NewsFeedFragmentDetail(item);
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
}
