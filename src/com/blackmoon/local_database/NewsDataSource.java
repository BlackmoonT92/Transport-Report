package com.blackmoon.local_database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.blackmoon.dto.CName;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NewsDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColums = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_JSON_NEWSFEED };

	public NewsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open(){
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}

	public JSONObject createNewsFeed(JSONObject object) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_JSON_NEWSFEED, object.toString());
		long insertId = database
				.insert(MySQLiteHelper.TABLE_NEWSFEED, null, values);

		Cursor cursor = database.query(MySQLiteHelper.TABLE_NEWSFEED, allColums,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		JSONObject newRoom = cursorToObject(cursor);

		cursor.close();
		return newRoom;
	}

	public JSONArray getAllNewsFeed() {
		JSONArray objects = new JSONArray();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NEWSFEED, allColums,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Object object = cursorToObject(cursor);
			objects.put(object);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return objects;
	}
	
	public void deleteNewsFeed(JSONObject object){
		int _id = 0;
		try {
			_id = object.getInt(CName.KEY_NEWSFEED_ID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("<<MY DEBUG>>", "delete object");
		database.delete(MySQLiteHelper.TABLE_NEWSFEED, MySQLiteHelper.COLUMN_ID + " = " + _id, null);
		
	}
	
	

	private JSONObject cursorToObject(Cursor cursor) {
		JSONObject object = null;
		try {
			object = new JSONObject(cursor.getString(1));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return object;
	}
}
