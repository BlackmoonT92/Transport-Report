package com.blackmoon.local_database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	// =============================================
	// VARIABLES
	// =============================================
	public static final String TABLE_NEWSFEED = "newsfeed";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_JSON_NEWSFEED = "jsonNewsFeed";

	private static final String DATABASE_NAME = "VNReport.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE_ROOMS = "CREATE TABLE ["
			+ TABLE_NEWSFEED + "] (" + "[" + COLUMN_ID
			+ "] INTEGER PRIMARY KEY AUTOINCREMENT," + "["
			+ COLUMN_JSON_NEWSFEED + "] TEXT NOT NULL" +")";

	

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_ROOMS);
		// db.execSQL(DATABASE_CREATE_THAMSO);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWSFEED);
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_THAMSO);
		onCreate(db);

	}

}
