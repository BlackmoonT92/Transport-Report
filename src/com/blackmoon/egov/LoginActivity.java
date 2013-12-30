package com.blackmoon.egov;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blackmoon.features.MyInternet;

import com.blackmoon.util.Utils;
import com.blackmoon.webservices.OnPostJsonListener;
import com.blackmoon.webservices.ServiceLogin;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 
 * @author anhle
 * 
 */
public class LoginActivity extends Activity implements OnClickListener, OnPostJsonListener
		 {

	// ================================================
	// VARIABLES
	// ================================================

	private EditText etUsername;
	private EditText etPassword;
	private Button btnLogin;
	private ProgressBar progressBar;

	// ================================================
	// CLASS FUNCTIONS BASIC
	// ================================================
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.activity = this;
		setContentView(R.layout.activity_login);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);

		etUsername = (EditText) findViewById(R.id.etUsername);

		etPassword = (EditText) findViewById(R.id.etPassword);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);

	}// end onCreate

	/**
	 * handle event back Key return NewsFeed if currentFragment is on one of
	 * mainScreen handle backstackFragment if current fragment is NewsFeed =>
	 * dialog confirm exit app
	 */
	@Override
	public void onBackPressed() {

		super.onBackPressed();

	}

	// set behavior for event menu will open Sliding left menu
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			// Login When user enter_key was pressed

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnLogin:
		
			// Login
			ServiceLogin serviceLogin = new ServiceLogin();
			serviceLogin.addOnPostJsonListener(this);
			try {
				// Show progress
				progressBar.setVisibility(View.VISIBLE);
				JSONObject loginInfo = new JSONObject();
				loginInfo.put("userName", etUsername.getText().toString());
				Config.userName = etUsername.getText().toString();
				loginInfo.put("password", etPassword.getText().toString());
				Config.password = etPassword.getText().toString();
				//serviceLogin.logon(loginInfo);
				serviceLogin.logon(etUsername.getText().toString(), etPassword.getText().toString());
			} catch (JSONException e) {
				if (BuildConfig.DEBUG) {
					e.printStackTrace();
				}
			}
			
			break;
		
		}

	}



	@Override
	public void onPostJsonCompleted(String response) {
		Log.w("LOGIN SUCCESS", response);

		// Get info about Login process
		try {
			JSONObject responseJSON = new JSONObject(response);
			if (responseJSON.getBoolean("status")) {
				Toast.makeText(this,
						"Đăng nhập thành công",
						Toast.LENGTH_LONG).show();
				Config.userAccount = responseJSON.getJSONObject("user");
				Config.isLogged = true;
				Intent intentMainActivity = new Intent(this, MainActivity.class);
				this.startActivity(intentMainActivity);

			} else {
				Toast.makeText(this,
						"Đăng nhập thất bại", Toast.LENGTH_LONG)
						.show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		progressBar.setVisibility(View.GONE);
		
	}

	@Override
	public void onPostJsonFail(String response) {
		Toast.makeText(this,
				"Đăng nhập thất bại", Toast.LENGTH_LONG).show();
		progressBar.setVisibility(View.GONE);
		
	}
	

}
