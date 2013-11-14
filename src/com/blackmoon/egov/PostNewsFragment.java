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
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
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

import com.blackmoon.dto.NewsFeedItem;
import com.blackmoon.features.GPSTracker;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class PostNewsFragment extends Fragment implements
		android.content.DialogInterface.OnClickListener {

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
	private CheckBox checkBox;
	private Spinner spinnerCity;

	private LinearLayout linearCheckBox2;
	private LinearLayout linearCheckBox1;
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
	Uri URI = Uri.parse(""); // path of image
	Bitmap bitmap_image;
	int columnIndex;
	String attachmentFile;

	// postNewsFeed return a NewsFeedItem
	private NewsFeedItem newsPosted = new NewsFeedItem();
	String timeStamp;

	// GPS
	// GPSTracker class
	private GPSTracker gps;
	private Location location = new Location(LocationManager.NETWORK_PROVIDER);
	Map<Integer, String> regions = new HashMap<Integer, String>();

	// ===========================================
	// CLASS DEFAULT
	// ===========================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Config.activity.setTitle(R.string.post);
		// Config.activity.getActionBar().setIcon(R.drawable.post);
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

		btnPost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// check district checkbox is selected
				if (regions.size() == 0) {
					Toast.makeText(Config.activity, "Bạn chưa chọn khu vực",
							Toast.LENGTH_SHORT).show();
				} else {
					// send to server and store to local database
					exportNewsFeed();
					showPreviewPosted();
				}

			}

		});

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
				getGPSLocation();

			}
		});

	}

	// ===========================================
	// CLASS LOGICS
	// ===========================================
	private void showPreviewPosted() {
		Fragment newContent = new NewsFeedFragmentDetail(newsPosted);
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.content_frame, newContent);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		// add to back track
		ft.addToBackStack("Post preview");
		ft.commit();

	}

	public NewsFeedItem exportNewsFeed() {
		// get current day

		String timeFormat = new SimpleDateFormat("dd/MM/yyyy_HH:mm",
				Locale.getDefault()).format(new Date());
		// Toast.makeText(Config.activity, timeFormat, 0).show();
		newsPosted.setTitle(edtPostTitle.getText().toString());
		newsPosted.setNewsContent(edtPostContent.getText().toString());
		newsPosted.setAddress(edtAddress.getText().toString());
		newsPosted.setThumb_url(URI.toString());
		newsPosted.setTimePosted(timeFormat);
		newsPosted.setLocation(location);
		// save all haskmap data from checkbox district
		String[] value = new String[5];
		int k = 0;
		for (Entry<Integer, String> entry : regions.entrySet()) {
			value[k++] = entry.getValue();
		}
		newsPosted.setTagRegion(value);
		// Log.d("Regions", newsPosted.getTagRegion()[1]);
		return newsPosted;
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
		getGPSLocation();
	}

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
				if (pos == 0) {
					if (linearCheckBox1 != null) {
						linearCheckBox1.removeAllViews();
						linearCheckBox2.removeAllViews();
					}

				} else {
					if (linearCheckBox1 != null) {
						linearCheckBox1.removeAllViews();
						linearCheckBox2.removeAllViews();
					}

					fillDataToSpinnerDistrict(cityList[pos]);
					// Toast.makeText(Config.activity, cityList[pos], 1).show();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	private void fillDataToSpinnerDistrict(String city) {

		int resID = Config.activity.getResources().getIdentifier(city, "array",
				Config.activity.getPackageName());

		// Spinner to choose district
		districList = getResources().getStringArray(resID);

		linearCheckBox1 = (LinearLayout) getView().findViewById(
				R.id.linearDistric1);
		linearCheckBox2 = (LinearLayout) getView().findViewById(
				R.id.linearDistric2);

		/**
		 * create linked hash map for store item you can get value from database
		 * or server also
		 */
		int k = (districList.length + 1) / 2;
		for (int i = 0; i < districList.length; i++) {
			checkBox = new CheckBox(Config.activity);
			checkBox.setId(i);
			checkBox.setText(districList[i]);
			checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
			if (i < k) {
				linearCheckBox1.addView(checkBox);
			} else {
				linearCheckBox2.addView(checkBox);
			}

		}

	}

	View.OnClickListener getOnClickDoSomething(final Button button) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				// Toast.makeText(Config.activity,
				// "Khu Vực" + button.getText().toString(), 0).show();

				String temp = regions.get(button.getId());
				if (temp != null) {
					regions.remove(button.getId());
				} else if (regions.size() < 5) {
					regions.put(button.getId(), button.getText().toString());
				} else {
					Toast.makeText(Config.activity,
							"Bạn đang chọn quá nhiều khu vực",
							Toast.LENGTH_SHORT).show();
					spinnerCity.setSelection(0);
					regions.clear();
				}

			}
		};
	}

	// ==========================================================
	// GPS
	// ==========================================================
	public Location getGPSLocation() {

		// create class object
		gps = new GPSTracker(Config.activity);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			location.setLongitude(longitude);
			location.setLatitude(latitude);
			// \n is for new line
			/*Toast.makeText(
					Config.activity,
					"Your Location is - \nLat: " + latitude + "\nLong: "
							+ longitude, Toast.LENGTH_LONG).show();*/
			String address = getAddressFromLocation(location);
			edtAddress.setText(address);
			return location;
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
			return null;
		}
	}

	public String getAddressFromLocation(Location location) {
		String value = null;
		try {
			Geocoder geocoder;
			List<Address> addresses;
			geocoder = new Geocoder(Config.activity, Locale.getDefault());
			Toast.makeText(Config.activity, geocoder.isPresent() + "", 0).show();
			addresses = geocoder.getFromLocation(location.getLatitude(),
					location.getLatitude(), 1);

			String address = addresses.get(0).getAddressLine(0);
			
			String city = addresses.get(0).getAddressLine(1);
			String country = addresses.get(0).getAddressLine(2);
			 value = address + ", " + city + ", " + country;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return value;
	}
}
