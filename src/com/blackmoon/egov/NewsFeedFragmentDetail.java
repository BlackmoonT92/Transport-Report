package com.blackmoon.egov;

import org.json.JSONException;
import org.json.JSONObject;

import com.blackmoon.dto.CName;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class NewsFeedFragmentDetail extends Fragment {
	// ==========================================
	// VARIABLES
	// ==========================================

	private View view = null;
	private JSONObject JSONNews = new JSONObject();

	private String image_url;

	// contructor
	public NewsFeedFragmentDetail() {
		JSONNews = Config.itemClicked;
	}

	public NewsFeedFragmentDetail(JSONObject itemClicked) {
		JSONNews = itemClicked;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Config.activity.setTitle(R.string.news_feed_detail);
		// Config.activity.getActionBar().setIcon(R.drawable.ic_laucher);

		setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_newsfeed_detail,
					container, false);
			Log.d("NewsFeed Detail", "view parent error");

		} else {
			// If we are returning from a configuration change:
			// "view" is still attached to the previous view hierarchy
			// so we need to remove it and re-attach it to the current one
			ViewGroup parent = (ViewGroup) view.getParent();
			parent.removeView(view);
		}
		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		// get component and findViewById
		TextView txtTitleNews = (TextView) getView().findViewById(
				R.id.txtTitleNews);
		TextView txtDescNews = (TextView) getView().findViewById(
				R.id.txtDescNews);
		TextView txtNewsContent = (TextView) getView().findViewById(
				R.id.txtNewsContent);
		TextView txtLocated = (TextView) getView()
				.findViewById(R.id.txtLocated);
		ImageView imageNews = (ImageView) getView().findViewById(
				R.id.imageAttach);
		final ImageView imageLike = (ImageView) getView().findViewById(
				R.id.imageLike);
		imageLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageLike.setImageResource(R.drawable.liked);

			}
		});
		Button btnOpenMaps = (Button) getView().findViewById(R.id.btnLocated);
		try {

			txtTitleNews.setText(JSONNews.getString(CName.KEY_NEWSFEED_TILTE));
			String descNews = "Gửi bởi: "
					+ JSONNews.getString(CName.KEY_USER_NAME) + " - "
					+ JSONNews.getString(CName.KEY_NEWSFEED_TIME) + " - Tại: "
					+ JSONNews.getString(CName.KEY_DISTRICT_NAME);
			txtDescNews.setText(descNews);
			txtNewsContent.setText(JSONNews
					.getString(CName.KEY_NEWSFEED_CONTENT));
			txtLocated.setText(JSONNews.getString(CName.KEY_ADDRESS_DETAIL));

			// temp link url
			String uri;
			uri = JSONNews.getString(CName.KEY_NEWSFEED_URL_IMAGES);
			if (uri != null) {
				imageNews.setImageURI(Uri.parse(uri));
			} else {
				imageNews.setImageResource(R.drawable.thumb_url);
			}

			imageNews.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// double click is like

				}
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// setLick event
		btnOpenMaps.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String geoCode = null;
				double lat;
				double lon;
				try {

					lat = Double.parseDouble(JSONNews
							.getString(CName.KEY_ADDRESS_COORDINATED_X));

					lon = Double.parseDouble(JSONNews
							.getString(CName.KEY_ADDRESS_COORDINATED_Y));
					if (lat != 0.0) {
						geoCode = "geo:0,0?q=" + lat + "," + lon;

					} else {
						geoCode = "http://maps.google.com/maps?q="
								+ JSONNews.getString(CName.KEY_ADDRESS_DETAIL);
					}

				} catch (JSONException e) {
					try {
						geoCode = "http://maps.google.com/maps?q="
								+ JSONNews.getString(CName.KEY_ADDRESS_DETAIL);

					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(geoCode));
				startActivity(intent);
			}
		});

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

}
