package com.blackmoon.egov;

import com.blackmoon.dto.NewsFeedItem;
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

	NewsFeedItem item = null;
	private View view = null;

	// contructor
	public NewsFeedFragmentDetail(){
		item = Config.itemClickedToViewDetail;
	}
	public NewsFeedFragmentDetail(NewsFeedItem itemClicked) {
		item = itemClicked;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Config.activity.setTitle(R.string.news_feed_detail);
		Config.activity.getActionBar().setIcon(R.drawable.ic_laucher);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_newsfeed_detail,
					container, false);
			Log.d("FUCK", "view parent error");
			
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
				R.id.imageNewsFeedItem);
		Button btnOpenMaps = (Button) getView().findViewById(R.id.btnLocated);
		
		txtTitleNews.setText(item.getTitle());
		txtDescNews.setText(item.getDesctionItem());
		txtNewsContent.setText(item.getNewsContent());
		txtLocated.setText(item.getLocated());
		

		// temp link url
		int resID = Config.activity.getResources().getIdentifier(
				item.getThumb_url(), "drawable",
				Config.activity.getPackageName());
		imageNews.setImageResource(resID);

		// setLick event
		btnOpenMaps.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				String geoCode = "geo:" + item.getCoordinates().getLon() + ","
						+ item.getCoordinates().getLat();
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoCode));
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
