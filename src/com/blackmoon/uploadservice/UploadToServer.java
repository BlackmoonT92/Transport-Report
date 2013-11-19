package com.blackmoon.uploadservice;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


public class UploadToServer implements UploadSubject {

	public void uploadImage(Uri imageUri) {
		UploadImageTask task = new UploadImageTask();
		task.execute(new String[] { imageUri.toString() });
	}
	
	/**
	 * This is Asyntask Upload web-page source via URL run in Background
	 * 
	 * @author anhle
	 * 
	 */
	protected class UploadImageTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... uriArray) {
			String response = "OK";
			result = "";
			String[] uris = uriArray[0].split("~");
			for (String uri : uris) {
				try {
					if (result == "") {
						result = uploadFile(uri);
					} else {
						result = result + "~" + uploadFile(uri);
					}
				} catch (Exception e) {
					response = "FAIL";
				}
			}
			
			return response;
		}

		@Override
		protected void onPostExecute(String r) {
			notifyChange();
		}
	}
	
	/**
	 * URL of image after upload
	 * 
	 * @param sourceFileUri : path of image in device
	 * @return
	 */
	private String uploadFile(String sourceFileUri) {
		String upLoadServerUri = "http://blackmoont92.byethost13.com/test/upload_media_test.php";
		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		
		File sourceFile = new File(sourceFileUri.substring(7, sourceFileUri.length()));
		if (!sourceFile.isFile()) {
			Log.e("uploadFile", "Source File Does not exist");
			Log.e("UPLOADTOSERVER", sourceFileUri);
			return "";
		}
		
		try { // open a URL connection to the Servlet
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			URL url = new URL(upLoadServerUri);
			conn = (HttpURLConnection) url.openConnection(); // Open a HTTP
																// connection to
																// the URL
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("uploaded_file", fileName);
			
			dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
					+ fileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available(); // create a buffer of
															// maximum size

			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// Responses from the server (code and message)
			int serverResponseCode = conn.getResponseCode();
			String serverResponseMessage = conn.getResponseMessage();

			Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage
					+ ": " + serverResponseCode);
			
			// close the streams //
			fileInputStream.close();
			dos.flush();
			dos.close();

			String[] sd = sourceFileUri.split("/");			
			return "http://blackmoont92.byethost13.com/test/uploads/" + sd[sd.length - 1]; 
		} catch (Exception e) {
			return "";
		}
		
	}


	// Observers which need to notify change
	protected Vector<UploadObserver> observers = new Vector<UploadObserver>();
	protected String result = "";

	@Override
	public void addUploadObserver(UploadObserver o) {
		observers.add(o);

	}

	@Override
	public void removeUploadObserver(UploadObserver o) {
		observers.remove(o);

	}

	@Override
	public void notifyChange() {
		for (UploadObserver o : observers) {
			o.UploadImageBitmapComplete(result);
		}
	}
	
	
}