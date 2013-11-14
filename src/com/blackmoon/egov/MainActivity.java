package com.blackmoon.egov;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blackmoon.features.MyInternet;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * This activity is an example of a responsive Android UI. On phones, the
 * SlidingMenu will be enabled only in portrait mode. In landscape mode, it will
 * present itself as a dual pane layout. On tablets, it will will do the same
 * general thing. In portrait mode, it will enable the SlidingMenu, and in
 * landscape mode, it will be a dual pane layout.
 * 
 * @author jeremy
 * 
 */
public class MainActivity extends SlidingFragmentActivity implements
		android.content.DialogInterface.OnClickListener {

	// ================================================
	// VARIABLES
	// ================================================

	private Fragment mContent;
	private SlidingMenu sm;
	private FragmentManager fm;
	private MyInternet mInternet;
	// ================================================
	// CLASS FUNCTIONS BASIC
	// ================================================
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
		Config.activity = this;
		setContentView(R.layout.content_frame);
		
		
	    
		
		// check if the content frame contains the menu frame
		if (findViewById(R.id.menu_frame) == null) {
			// menu_frame nằm trên view của class Main.

			setBehindContentView(R.layout.menu_frame);

			getSlidingMenu().setSlidingEnabled(true);
			getSlidingMenu()
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			// show home as up so we can toggle
			try {
				getActionBar().setDisplayHomeAsUpEnabled(true);
			} catch (RuntimeException e) {
				Log.d("<<MainActivity>>", "action bar error");
			}

		} else {
			// add a dummy view
			View v = new View(this);
			setBehindContentView(v);
			getSlidingMenu().setSlidingEnabled(false);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

		// set the Above View Fragment
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new NewsFeedFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		// set the Behind View Fragment
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MenuLeftFragment()).commit();

		setSlidingActionBarEnabled(false);
		

		// customize the SlidingMenu
		sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.25f);

		// get View of Layout_user

		LinearLayout layoutUser = (LinearLayout) findViewById(R.id.layoutUser);
		layoutUser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("<MY DEBUG", "clicked Name");
				Fragment newContent = new UserInfoFragment();
				switchContent(newContent);
			}
		});

		/**
		 * Check connection to internet.
		 * 
		 */
		if (mInternet.hasConnection(getApplicationContext())) {
			Toast.makeText(getApplicationContext(), "Connected to Internet", 1)
					.show();
		} else {
			// load data from local database

		}

	}// end onCreate

	// ===============================================
	// CLASS LOGICS
	// ===============================================

	

	public void switchContent(final Fragment fragment) {
		mContent = fragment;
		fm = getSupportFragmentManager();	
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.content_frame, mContent);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		// add to back track
		// ft.addToBackStack("NewsFeed");

		ft.commit();

		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}

	/**
	 * handle event back Key return NewsFeed if currentFragment is on one of
	 * mainScreen handle backstackFragment if current fragment is NewsFeed =>
	 * dialog confirm exit app
	 */

	@Override
	public void onBackPressed() {
		if (Config.currentFragment == 2) {
			switchContent(new NewsFeedFragment());
		} else if (Config.currentFragment == 1) {
			AlertDialog dialog = createAlertDialog();
			dialog.show();
		} else {
			super.onBackPressed();
		}

	}

	// set behavior for event menu will open Sliding left menu
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			toggle();
		}
		return super.onKeyDown(keyCode, event);
	}

	public AlertDialog createAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Thoát ứng dụng?");
		builder.setIcon(R.drawable.ic_laucher);

		builder.setPositiveButton("Éo", this);
		builder.setNeutralButton("Ờ", this);
		return builder.create();
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which == AlertDialog.BUTTON_POSITIVE) {
			Toast.makeText(this, "cancel", 0).show();
		} else if (which == AlertDialog.BUTTON_NEUTRAL) {
			Toast.makeText(this, "ok", 0).show();
			finish();

		}
	}

	// ===============================================
	// MENU
	// ===============================================
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		case R.id.action_post:
			//change status openCamraFirst to capture quickly
			Config.flagOpenCameraFirst = true;
			// đăng bài
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, new PostNewsFragment())
					.commit();
			return true;
		case R.id.action_refresh:
			toggle();
			// load lại tin mới.
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		android.view.MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	// ===============================================
	// ACTION BAR
	// ===============================================
	

}
