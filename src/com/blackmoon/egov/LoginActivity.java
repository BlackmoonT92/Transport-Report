package com.blackmoon.egov;

import org.json.JSONArray;
import org.json.JSONException;

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
import android.widget.Toast;

import com.blackmoon.features.MyInternet;
import com.blackmoon.service.LoginService;
import com.blackmoon.service.WebserviceObserver;
import com.blackmoon.util.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 
 * @author anhle
 * 
 */
public class LoginActivity extends Activity implements OnClickListener,
		WebserviceObserver {

	// ================================================
	// VARIABLES
	// ================================================

	private EditText etUsername;
	private EditText etPassword;
	private Button btnLogin;
	private ProgressDialog progressDialog;

	// ================================================
	// CLASS FUNCTIONS BASIC
	// ================================================
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.activity = this;
		setContentView(R.layout.activity_login);

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
			// Show waiting progress dialog
			progressDialog = ProgressDialog.show(this, "", "Đang kết nối máy chủ..",
					true, true);
			// Login
			LoginService loginService = new LoginService();
			loginService.addObserver(this);

			Log.w("PASS_WORD: " + etPassword.getText().toString(), Utils.string2MD5(etPassword.getText().toString()));
			loginService.login(etUsername.getText().toString(), Utils.string2MD5(etPassword.getText().toString().trim()));
			break;
		}

	}

	@Override
	public void DownloadWebPageTaskComplete(JSONArray jsonArray) {
		// Close waiting progress dialog and show result of login
		progressDialog.dismiss();

		// Check account for Login here !
		if (jsonArray.length() == 1) {
			Config.username = etUsername.getText().toString();
			Config.isLogin = true;
			// Start MainActivity if account valid
			Intent intentMainActivity = new Intent(this, MainActivity.class);
			this.startActivity(intentMainActivity);
		} else {
			Toast.makeText(this, "Wrong username or password !",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void DownloadBitmapComplete(Bitmap bitmap) {
		// TODO Auto-generated method stub

	}

}
