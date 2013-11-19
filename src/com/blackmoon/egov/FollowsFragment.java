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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class FollowsFragment extends Fragment {
	// ===================================================
	// VARIABLES
	// ===================================================
	private NewsFeedListAdapter listAdapter;
	private Spinner spinnerFillter;
	ListView list;
	String[] dataFillter = new String[] { "Khu Vực", "Người Đăng" };
	String[] dataKhuVuc = new String[] { "Binh Duong", "Ho Chi Minh", };
	String[] dataUser = new String[] { "Chọn UserName", "BlackmoonT92",
			"GreenDream", "HoanNguyen.Uit" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Config.activity.setTitle(R.string.follows);
		//Config.activity.getActionBar().setIcon(R.drawable.follows);
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_follows, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// set current fragment
		Config.currentFragment = 2;
		// set spinner filter
		spinnerFillter = (Spinner) getView().findViewById(R.id.spinnerFiller);

		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
				Config.activity, android.R.layout.simple_spinner_item);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		for (int i = 0; i < dataFillter.length; i++) {
			spinnerAdapter.add(dataFillter[i]);
		}

		spinnerFillter.setAdapter(spinnerAdapter);
		spinnerFillter.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				Spinner spinnerChoose = (Spinner) getView().findViewById(
						R.id.spinnerChoose);
				if (pos == 0) {
					ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
							Config.activity,
							android.R.layout.simple_spinner_item);
					spinnerAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					for (int i = 0; i < dataKhuVuc.length; i++) {
						spinnerAdapter.add(dataKhuVuc[i]);
					}
					spinnerChoose.setAdapter(spinnerAdapter);
				} else {
					ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
							Config.activity,
							android.R.layout.simple_spinner_item);
					spinnerAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					for (int i = 0; i < dataUser.length; i++) {
						spinnerAdapter.add(dataUser[i]);
					}
					spinnerChoose.setAdapter(spinnerAdapter);
				}
				spinnerChoose
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int pos, long arg3) {

								// list all data form user follow
								JSONNewsFeedList listJson = new JSONNewsFeedList();
								final JSONArray dataNewsFeed = listJson.listData;
								listAdapter = new NewsFeedListAdapter(
										Config.activity, dataNewsFeed);
								list = (ListView) getView().findViewById(
										R.id.listFollow);
								list.setAdapter(listAdapter);
								list.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										// set currtent Fragment for backButton

										// NewsFeedItem itemClicked =
										// data.get(position);
										// Config.itemClickedToViewDetail =
										// itemClicked;
										// covert to JSON
										JSONObject item = null;
										try {
											item = (JSONObject) dataNewsFeed
													.get(position);
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										Config.itemClicked = item;
										Fragment newContent = new NewsFeedFragmentDetail(
												item);
										FragmentManager fm = getActivity()
												.getSupportFragmentManager();
										FragmentTransaction ft = fm
												.beginTransaction();
										ft.replace(R.id.content_frame,
												newContent);
										ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
										// add to back track
										ft.addToBackStack("NewsFeed");
										ft.commit();
									}
								});

							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

}
