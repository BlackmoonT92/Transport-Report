package com.blackmoon.egov;

import org.json.JSONArray;
import org.json.JSONException;
import com.blackmoon.custom_list_view.NewsFeedListAdapter;
import com.blackmoon.dto.JSONNewsFeedList;
import com.blackmoon.webservices.OnGetJsonListener;
import com.blackmoon.webservices.ServiceGetNewsFeed;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class NewsFeedFragment extends Fragment  {
	// ==============================================
	// VARIABLES AND CONSTANTS
	// ==============================================

	private PullAndLoadListView list;
	public NewsFeedListAdapter listAdapter;
	Fragment newContent;
	ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Config.activity.setTitle(R.string.news_feed);

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

		// Show waiting progress dialog

		progressDialog = ProgressDialog.show(Config.activity, "",
				"Đang tải dữ liệu", true, true);

		list = (PullAndLoadListView) getView().findViewById(
				R.id.pullToRefreshListView);
		// load data and show here
		getDataFromServer();

	}

	private void getDataFromServer() {
		ServiceGetNewsFeed serviceGetNewsFeed = new ServiceGetNewsFeed();
		serviceGetNewsFeed.getAllNewsfeed();
		serviceGetNewsFeed.addOnGetJsonListener(new OnGetJsonListener() {

			@Override
			public void onGetJsonFail(String response) {

			}

			@Override
			public void onGetJsonCompleted(String response) {
				progressDialog.dismiss();
				try {
					JSONNewsFeedList.listData = new JSONArray(response);

					Log.d("GETALL NEWSFEED",
							JSONNewsFeedList.listData.toString());
					listAdapter = new NewsFeedListAdapter(Config.activity,
							JSONNewsFeedList.listData);
					list.setAdapter(listAdapter);
					
					list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int pos, long id) {
							try {
								Config.itemClicked = JSONNewsFeedList.listData.getJSONObject(pos);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Fragment mContent = new NewsFeedFragmentDetail();
							FragmentManager fm = getActivity().getSupportFragmentManager();
							FragmentTransaction ft = fm.beginTransaction();
							ft.replace(R.id.content_frame, mContent);
							ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
							// add to back track
							ft.addToBackStack("NewsFeed");

							ft.commit();

						}
					});

				} catch (JSONException e) {

					e.printStackTrace();
				}

			}
		});

		list.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				new PullToRefreshDataTask().execute();

			}
		});

		list.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				new LoadMoreDataTask().execute();

			}
		});
	}

	private class LoadMoreDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

			// Simulates a background task
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

			// load more data and add to list at here

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// We need notify the adapter that the data have been changed
			/**
			 * The content of the adapter has changed but ListView did not
			 * receive a notification. Make sure the content of your adapter is
			 * not modified from a background thread, but only from the UI
			 * thread. [in ListView(2131165283, class com.costum.android.widget.
			 * PullAndLoadListView) with Adapter(class
			 * android.widget.HeaderViewListAdapter)]
			 */
			// listAdapter.notifyDataSetChanged();

			// Call onLoadMoreComplete when the LoadMore task, has finished
			list.onLoadMoreComplete();
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			list.onLoadMoreComplete();
		}
	}

	private class PullToRefreshDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// listResult.addFirst("Added after refresh...")
			// listAdapter.notifyDataSetChanged();
			list.onRefreshComplete();

			super.onPostExecute(result);

		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			list.onLoadMoreComplete();
		}

	}

	@Override
	public void onDestroyView() {
		Config.currentFragment = 0;
		super.onDestroyView();
	}


	
}
