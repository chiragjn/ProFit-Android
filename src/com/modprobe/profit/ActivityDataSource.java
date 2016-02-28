package com.modprobe.profit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ActivityDataSource {

	// Database fields
	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;
	private String[] allColumns = { "_id", "fk_subcat", "duration", "intensity", "fitons",
			"sid" };
	Context mContext;

	public ActivityDataSource(Context context) {
		// DataBaseHelper myDbHelper = new DataBaseHelper();
		mContext = context;
		dbHelper = new DataBaseHelper(context);

		try {

			dbHelper.createDataBase();

		} catch (IOException ioe) {
			Log.e("error sql", "1");
			throw new Error("Unable to create database");

		}

		try {

			dbHelper.openDataBase();

		} catch (SQLException sqle) {
			Log.e("error sql", "2");
			throw sqle;

		}

	}

	public void open() throws SQLException {
		dbHelper.close();
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Activity createActivity(SubCat parent, int duration,
			int fitons, int intensity, int sid) {
		open();
		ContentValues values = new ContentValues();
		values.put("fk_subcat", parent._id);
		values.put("duration", duration);
		values.put("fitons", fitons);
		values.put("intensity", intensity);
		values.put("sid", sid);
		long insertId = database.insert("Activity", null, values);
		Cursor cursor = database.query("Activity", allColumns, "_id"
				+ " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Activity activity = cursorToActivity(cursor);
		cursor.close();
		return activity;
	}

	public void deleteActivity(Activity activity) {
		long id = activity._id;
		System.out.println("Comment deleted with id: " + id);
		database.delete("Activity", "_id" + " = " + id, null);
	}

	public List<Activity> getActivities(SubCat subcat) {
		List<Activity> activities = new ArrayList<Activity>();
		open();
		Cursor cursor = database.query("Activity", allColumns, "fk_subcat"
				+ " = " + subcat._id, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Activity activity = cursorToActivity(cursor);
			activities.add(activity);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return activities;
	}

	public List<Activity> getAllActivites() {
		List<Activity> activities = new ArrayList<Activity>();
		open();
		Cursor cursor = database.query("Activity", allColumns, null, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Activity activity = cursorToActivity(cursor);
			activities.add(activity);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return activities;
	}

	private Activity cursorToActivity(Cursor cursor) {
		//open();
		Activity activity = new Activity();
		activity._id = (int) cursor.getLong(0);
		SubCatDataSource qds = new SubCatDataSource(mContext);
		qds.open();
		activity._parent = qds.getSubCat(cursor.getInt(1));
		qds.close();
		activity._duration = cursor.getInt(2);
		activity._intensity = cursor.getInt(3);
		activity._fitons = cursor.getInt(4);
		activity._sid = cursor.getInt(5);
		return activity;
	}
}