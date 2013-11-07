package com.blackmoon.egov;

import java.util.ArrayList;
import java.util.HashMap;

import com.blackmoon.custom_list_view.MenuListAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MenuLeftFragment extends ListFragment {
	public static final String KEY_TITLE = "title"; // parent node
	public static final String KEY_ICON = "icon";

	private MenuListAdapter adapter;
	Fragment newContent = new Fragment(); // store a fragment when menu clicked

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// load data from database and put it into ArrayLists
		String[] features = getResources().getStringArray(R.array.features);
		String[] chucNang = getResources().getStringArray(R.array.chuc_nang);
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		// set adapter
		for (int i = 0; i < features.length; i++) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put(KEY_TITLE, chucNang[i]);
			item.put(KEY_ICON, features[i].toLowerCase());
			data.add(item);
		}

		adapter = new MenuListAdapter(getActivity(), data);
		setListAdapter(adapter);

		

	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d("<<MY DEBUG>>", "MenuLeftFragment + clicked memu Item "
				+ position);
		// đổi fragment

		switch (position) {
		case 0:

			newContent = new NewsFeedFragment();
			break;
		case 1:
			newContent = new FollowsFragment();
			break;
		case 2:
			newContent = new HistoryFragment();
			break;
		case 3:
			newContent = new SearchFragment();
			break;
		case 4:
			newContent = new AboutFragment();
			break;
		case 5:
			newContent = new LoginFragment();
			break;
		}

		if (newContent != null)
			switchFragment(newContent);

	}

	// the meat of switching the above framgent
	public void switchFragment(Fragment fragment) {
		if (getActivity() == null) {
			return;
		}
		// éo hiểu chỗ này.
		if (getActivity() instanceof MainActivity) {
			MainActivity mainActivity = (MainActivity) getActivity();
			mainActivity.switchContent(fragment);
		}
	}

}
