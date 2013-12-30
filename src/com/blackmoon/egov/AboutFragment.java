package com.blackmoon.egov;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class AboutFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Config.activity.setTitle(R.string.about);
		//Config.activity.getActionBar().setIcon(R.drawable.about);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_about, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// set current fragment
		Config.currentFragment = 2;

		// TextView txtFuck = (TextView) getView.findViewById(R.id.xxx);
		// fucking here
	}
}
