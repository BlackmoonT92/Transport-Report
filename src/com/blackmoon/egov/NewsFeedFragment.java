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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

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
		//Config.activity.getActionBar().setIcon(R.drawable.newsfeed);
		

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
		final JSONArray dataNewsFeed = JSONNewsFeedList.listData;
		Log.i("JSONARRAY", dataNewsFeed.toString());
		
		listAdapter = new NewsFeedListAdapter(Config.activity, dataNewsFeed);
		list.setAdapter(listAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// set currtent Fragment for backButton

				//NewsFeedItem itemClicked = data.get(position);
				//Config.itemClickedToViewDetail = itemClicked;
				// covert to JSON
				JSONObject item = null;
				try {
					item = (JSONObject) dataNewsFeed.get(position);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Config.itemClicked = item;
				newContent = new NewsFeedFragmentDetail(item);
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.content_frame, newContent);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				// add to back track
				ft.addToBackStack("NewsFeed");
				ft.commit();
			}
		});
		/**
		 * refresh newsfeed when scroll to top load more data when scroll to
		 * bottom
		 */
		list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				final int lastItem = firstVisibleItem + visibleItemCount;
				if (lastItem == totalItemCount) {
					//Toast.makeText(Config.activity, "Loading more data", 0).show();
				}
			}
		});

	}

	@Override
	public void onDestroyView() {
		Config.currentFragment = 0;
		super.onDestroyView();
	}

}
