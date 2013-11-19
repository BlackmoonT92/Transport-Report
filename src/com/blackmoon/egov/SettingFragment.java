package com.blackmoon.egov;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Config.activity.setTitle(R.string.setting);
	//	Config.activity.getActionBar().setIcon(R.drawable.setting);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_setting, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// set current fragment
				Config.currentFragment = 2;
	}
}
