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

public class WeightLogDataSource {

  // Database fields
  private SQLiteDatabase database;
  private DataBaseHelper dbHelper;
  private String[] allColumns = { "_id","date","weight", "sid" };

  public WeightLogDataSource(Context context) {
	  //DataBaseHelper myDbHelper = new DataBaseHelper();
      dbHelper = new DataBaseHelper(context);

      try {

      	dbHelper.createDataBase();

	} catch (IOException ioe) {
		Log.e("error sql","1");
		throw new Error("Unable to create database");

	}

	try {

		dbHelper.openDataBase();

	}catch(SQLException sqle){
		Log.e("error sql","2");
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

  public WeightLog createWeightLog(String date, int weight,  int sid) {
	  open();
    ContentValues values = new ContentValues();
    //values.put("_id", 1);
    values.put("date", date);
    values.put("weight", weight);
    values.put("sid", sid);
    long insertId = database.insert("WeightLog", null,
        values);
    Cursor cursor = database.query("WeightLog",
        allColumns, "_id" + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    WeightLog wl = cursorToWeightLog(cursor);
    cursor.close();
    return wl;
  }
  
  
  public WeightLog getWeightLog(int id){
	  open();
	  Cursor cursor = database.query("WeightLog",
		        allColumns, "_id" + " = " + id, null,
		        null, null, null);
		    cursor.moveToFirst();
		    WeightLog wl = cursorToWeightLog(cursor);
		    cursor.close();
		    return wl;
  }
  

  public void deleteWeightLog(WeightLog wl) {
    long id = wl._id;
    System.out.println("Comment deleted with id: " + id);
    database.delete("WeightLog", "_id"
        + " = " + id, null);
  }

  public List<WeightLog> getAllWeightLogs() {
	  open();
    List<WeightLog> wls = new ArrayList<WeightLog>();

    Cursor cursor = database.query("WeightLog",
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
    	WeightLog wl = cursorToWeightLog(cursor);
      wls.add(wl);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return wls;
  }

  private WeightLog cursorToWeightLog(Cursor cursor) {
	//open();
	  WeightLog wl = new WeightLog();
    wl._id=(int)cursor.getLong(0);
    wl._date = cursor.getString(1);
    wl._weight = cursor.getInt(2);
    wl._sid = cursor.getInt(3);
    return wl;
  }
}