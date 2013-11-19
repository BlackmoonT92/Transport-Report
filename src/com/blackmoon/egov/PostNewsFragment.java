package com.blackmoon.egov;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.blackmoon.dto.CName;
import com.blackmoon.dto.JSONNewsFeedList;
import com.blackmoon.features.GPSTracker;
import com.blackmoon.features.MyInternet;
import com.blackmoon.local_database.NewsDataSource;
import com.blackmoon.service.NewsfeedService;
import com.blackmoon.service.WebserviceObserver;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class PostNewsFragment extends Fragment implements WebserviceObserver,
		android.content.DialogInterface.OnClickListener, OnClickListener {

	// ===========================================
	// VARIABLES
	// ===========================================
	private Button btnPost;
	private Button btnAttach;
	private Button btnGPS;
	private ImageView imageAttach;
	private EditText edtPostTitle;
	private EditText edtPostContent;
	private EditText edtAddress;
	private Spinner spinnerCity;
	private Spinner spinnerDistrict;

	// load city list from array.xml
	String[] cityList;
	String[] districList;

	// camera
	private static final int PICK_FROM_GALLERY = 101; // repuest code
	// request code for capture
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "BlackmoonT92";
	public static final int MEDIA_TYPE_IMAGE = 1;
	Uri URI = null; // path of image
	Bitmap bitmap_image;
	int columnIndex;
	String attachmentFile;

	// postNewsFeed return a NewsFeedItem
	private JSONObject JSONNews = new JSONObject();
	String timeStamp;

	// GPS
	// GPSTracker class
	private GPSTracker gps;
	private Location location = new Location(LocationManager.NETWORK_PROVIDER);
	private String city = null;
	private String district = null;
	private String address = null;

	// database
	NewsDataSource dataSource = new NewsDataSource(Config.activity);

	// ===========================================
	// CLASS DEFAULT
	// ===========================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Config.activity.setTitle(R.string.post);
	//	Config.activity.getActionBar().setIcon(R.drawable.post);
		if (Config.flagOpenCameraFirst) {
			openCamera();
		}
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_post, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// get location to get address and fill address to UI
		getGPSLocation();

		// set current fragment
		Config.currentFragment = 2;
		// Spinner to choose district
		spinnerCity();
		// Button event
		imageAttach = (ImageView) getView().findViewById(R.id.imageAttach);
		btnPost = (Button) getView().findViewById(R.id.btnPost);
		btnAttach = (Button) getView().findViewById(R.id.btnAttach);
		btnGPS = (Button) getView().findViewById(R.id.btnGPS);
		edtPostTitle = (EditText) getView().findViewById(R.id.edtPostTitle);
		edtPostContent = (EditText) getView().findViewById(R.id.edtPostContent);
		edtAddress = (EditText) getView().findViewById(R.id.edtAddress);
		spinnerDistrict = (Spinner) getView()
				.findViewById(R.id.spinnerDistrict);
		btnPost.setOnClickListener(this);

		btnAttach.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// open gallery
				AlertDialog dialog = createAlertDialog();
				dialog.show();
				// resize photo and show

			}
		});
		/**
		 * Long click picture will delete this picture
		 */
		imageAttach.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// delete this picture
				imageAttach.setVisibility(View.GONE);
				URI = Uri.parse("");
				return false;
			}
		});

		btnGPS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (address == null) {
					new GetAddressTask(Config.activity).execute(location);

				} else {
					if ("Binh Duong".equals(city)) {
						spinnerCity.setSelection(2);
					}
					edtAddress.setText(address);
				}
			}
		});

	}

	// ===========================================
	// CLASS LOGICS
	// ===========================================
	private void showPreviewPosted() {
		new reloadData().execute();
		Fragment newContent = new NewsFeedFragmentDetail(JSONNews);
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.content_frame, newContent);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		// add to back track
		ft.addToBackStack("Post preview");
		ft.commit();

	}

	public JSONObject exportNewsFeed() {
		// get current day
		JSONObject JSONNewsPost = new JSONObject();
		String timeFormat = new SimpleDateFormat("dd/MM/yyyy_HH:mm",
				Locale.getDefault()).format(new Date());
		// Toast.makeText(Config.activity, timeFormat, 0).show();

		try {
			JSONNewsPost.put(CName.KEY_NEWSFEED_TILTE, edtPostTitle.getText()
					.toString());
			JSONNewsPost.put(CName.KEY_NEWSFEED_CONTENT, edtPostContent
					.getText().toString());
			JSONNewsPost.put(CName.KEY_NEWSFEED_TIME, timeFormat);
			JSONNewsPost.put(CName.KEY_NEWSFEED_URL_IMAGES, URI);
			JSONNewsPost.put(CName.KEY_ADDRESS_DETAIL, edtAddress.getText()
					.toString());
			JSONNewsPost.put(CName.KEY_ADDRESS_COORDINATED_X,
					location.getLatitude() + "");
			JSONNewsPost.put(CName.KEY_ADDRESS_COORDINATED_Y,
					location.getLongitude() + "");
			JSONNewsPost.put(CName.KEY_USER_NAME, "BlackmoonT92");
			JSONNewsPost.put(CName.KEY_DISTRICT_NAME, spinnerDistrict
					.getSelectedItem().toString());
			JSONNewsPost.put(CName.KEY_PROVINCE_NAME, spinnerCity
					.getSelectedItem().toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONNewsPost;
	}

	public AlertDialog createAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Lấy ảnh từ...");
		builder.setIcon(R.drawable.ic_laucher);

		builder.setPositiveButton("Gallery", this);
		builder.setNeutralButton("Camera", this);
		return builder.create();
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which == AlertDialog.BUTTON_POSITIVE) {
			openGallery();
		} else if (which == AlertDialog.BUTTON_NEUTRAL) {
			// Checking camera availability
			if (!isDeviceSupportCamera()) {
				Toast.makeText(Config.activity,
						"Xin lỗi, máy của bạn không có Camera",
						Toast.LENGTH_LONG).show();
				// will close the app if the device does't have camera
			} else {
				openCamera();
			}
		}

		// set image for preview

	}

	// ==============================================
	// GET IMAGE FROM CAMERA OR GALLERY
	// ==============================================

	/**
	 * Checking device has camera hardware or not
	 * */
	private boolean isDeviceSupportCamera() {
		if (Config.activity.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	/*
	 * Capturing Camera Image will lauch camera app requrest image capture
	 */
	public void openCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		URI = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, URI);
		Toast.makeText(Config.activity,
				"Xoay ngang màn hình để chụp cho hiển thị tốt nhất",
				Toast.LENGTH_SHORT).show();
		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	/**
	 * ------------ Helper Methods ----------------------
	 * */

	/**
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/**
	 * returning image / video
	 */
	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	public void openGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.putExtra("return-data", true);
		startActivityForResult(
				Intent.createChooser(intent, "Complete action using"),
				PICK_FROM_GALLERY);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_FROM_GALLERY
				&& resultCode == Activity.RESULT_OK) {
			/**
			 * Get Path
			 */
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = Config.activity.getContentResolver().query(
					selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			attachmentFile = cursor.getString(columnIndex);
			Log.e("Attachment Path:", attachmentFile);
			URI = Uri.parse("file://" + attachmentFile);
			Log.e("URI Path:", URI.toString() + "");
			cursor.close();
		} else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				// successfully captured the image
				// display it in image view
				try {

					BitmapFactory.Options bounds = new BitmapFactory.Options();
					bounds.inJustDecodeBounds = true;
					Bitmap bm = BitmapFactory.decodeFile(URI.getPath(), bounds);

					ExifInterface exif = new ExifInterface(new File(
							URI.toString()).getAbsolutePath());
					String orientString = exif
							.getAttribute(ExifInterface.TAG_ORIENTATION);
					int orientation = orientString != null ? Integer
							.parseInt(orientString)
							: ExifInterface.ORIENTATION_NORMAL;
					int rotationAngle = 0;
					if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
						rotationAngle = 90;
					if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
						rotationAngle = 180;
					if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
						rotationAngle = 270;

					Matrix matrix = new Matrix();

					matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2,
							(float) bm.getHeight() / 2);
					Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0,
							bounds.outWidth, bounds.outHeight, matrix, true);

					Log.e("bitmap size:", rotatedBitmap.getHeight()
							+ rotatedBitmap.getWidth() + "");
					Log.e("URI Path:", URI.toString() + "");
					// imgPreview.setImageBitmap(bitmap);
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
		imageAttach.setImageURI(URI);
		imageAttach.setVisibility(View.VISIBLE);
		// get gps location but it's will slow to reposone
		// getGPSLocation();
	}

	// ==============================================
	// GET ADDRESS
	// ==============================================

	/**
	 * Spinner to choose city and districts
	 */
	private void spinnerCity() {
		// Spinner to choose district
		cityList = getResources().getStringArray(R.array.city);

		spinnerCity = (Spinner) getView().findViewById(R.id.spinnerCity);

		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
				Config.activity, android.R.layout.simple_spinner_item);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		for (int i = 0; i < cityList.length; i++) {
			spinnerAdapter.add(cityList[i]);
		}

		spinnerCity.setAdapter(spinnerAdapter);
		spinnerCity.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if (pos != 0) {
					city = cityList[pos];
					spinnerDistrict.setVisibility(View.VISIBLE);
					fillDataToSpinnerDistrict(city);
					// Toast.makeText(Config.activity, cityList[pos], 1).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	private void fillDataToSpinnerDistrict(String city) {

		try {

			int resID = Config.activity.getResources().getIdentifier(city,
					"array", Config.activity.getPackageName());

			// Spinner to choose district
			districList = getResources().getStringArray(resID);
			ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
					Config.activity, android.R.layout.simple_spinner_item);
			spinnerAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			for (int i = 0; i < districList.length; i++) {
				spinnerAdapter.add(districList[i]);
			}

			spinnerDistrict.setAdapter(spinnerAdapter);
			spinnerDistrict
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int pos, long id) {
							district = districList[pos];
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}
					});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ==========================================================
	// GPS
	// ==========================================================
	public void getGPSLocation() {

		// create class object
		gps = new GPSTracker(Config.activity);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			location.setLongitude(longitude);
			location.setLatitude(latitude);
			Toast.makeText(Config.activity,
					location.getLatitude() + " , " + location.getLongitude(), 0)
					.show();
			// get address from location
			new GetAddressTask(Config.activity).execute(location);

		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();

		}
	}

	/**
	 * A subclass of AsyncTask that calls getFromLocation() in the background.
	 * The class definition has these generic types: Location - A Location
	 * object containing the current location. Void - indicates that progress
	 * units are not used String - An address passed to onPostExecute()
	 */
	private class GetAddressTask extends AsyncTask<Location, Void, String> {
		Context mContext;

		public GetAddressTask(Context context) {
			super();
			mContext = context;
		}

		/**
		 * Get a Geocoder instance, get the latitude and longitude look up the
		 * address, and return it
		 * 
		 * @params params One or more Location objects
		 * @return A string containing the address of the current location, or
		 *         an empty string if no address can be found, or an error
		 *         message
		 */
		@Override
		protected String doInBackground(Location... params) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
			// Get the current location from the input parameter list
			Location loc = params[0];
			// Create a list to contain the result address
			List<Address> addresses = null;
			try {
				/*
				 * Return 1 address.
				 */
				addresses = geocoder.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);
			} catch (IOException e1) {
				Log.e("LocationSampleActivity",
						"IO Exception in getFromLocation()");
				e1.printStackTrace();
				return null;
			} catch (IllegalArgumentException e2) {
				// Error message to post in the log
				String errorString = "Illegal arguments "
						+ Double.toString(loc.getLatitude()) + " , "
						+ Double.toString(loc.getLongitude())
						+ " passed to address service";
				Log.e("LocationSampleActivity", errorString);
				e2.printStackTrace();
				return errorString;
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {
				// Get the first address
				Address address = addresses.get(0);
				/*
				 * Format the first line of address (if available), city, and
				 * country name.
				 */
				city = address.getAdminArea();
				district = address.getLocality();
				String addressText = String.format(
						"%s, %s, %s, %s, %s",
						// If there's a street address, add it
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "",
						// phường xã
						address.getSubLocality(),
						// Locality is usually a city
						district, city,
						// The country of the address
						address.getCountryName());

				// Return the text
				return addressText;
			} else {
				return null;
			}

		}

		@Override
		protected void onPostExecute(String result) {
			address = result;
			super.onPostExecute(result);
		}
	}

	private class reloadData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			new JSONNewsFeedList();
			return null;
		}

	}

	@Override
	public void onClick(View v) {
		// check district checkbox is selected
		if (spinnerCity.getSelectedItemPosition() == 0) {
			Toast.makeText(Config.activity, "Bạn chưa chọn khu vực",
					Toast.LENGTH_SHORT).show();
		} else {
			// send to server and store to local database
			JSONNews = exportNewsFeed();
			Log.i("JSON POST", JSONNews.toString());
			dataSource.open();
			dataSource.createNewsFeed(JSONNews);
			showPreviewPosted();
			if (!MyInternet.hasConnection(Config.activity)) {
				Toast.makeText(
						Config.activity,
						"Tin của bạn sẽ được đăng tự động sau khi kết nối mạng",
						1).show();
				// store to database and set flag not post
				
			} else {
				
				// --Ngoc Anh: Post new Newsfeed
				Log.i("JSON POST", JSONNews.toString());

				NewsfeedService newsfeedService = new NewsfeedService();
				newsfeedService.addObserver(this);
				newsfeedService.postNewService(JSONNews);
			}
			dataSource.close();

		}

	}

	@Override
	public void DownloadWebPageTaskComplete(JSONArray jsonArray) {
		// TODO Auto-generated method stub

	}

	@Override
	public void DownloadBitmapComplete(Bitmap bitmap) {
		// TODO Auto-generated method stub

	}

}
