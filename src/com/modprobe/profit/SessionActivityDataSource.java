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

public class SessionActivityDataSource {

  // Database fields
  private SQLiteDatabase database;
  private DataBaseHelper dbHelper;
  private String[] allColumns = {"_id","fk_session","fk_subcat","duration","sid"};
  Context mContext;
  public SessionActivityDataSource(Context context) {
	  //DataBaseHelper myDbHelper = new DataBaseHelper();
	  mContext = context;
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

  public SessionActivity createSessionActivity(Session parent,SubCat subcat, int duration, int sid) {
	  open();
    ContentValues values = new ContentValues();
    values.put("fk_session", parent._id);
    values.put("fk_subcat", subcat._id);
    values.put("duration", duration);
    values.put("sid", sid);
    long insertId = database.insert("SessionActivity", null,
        values);
    Cursor cursor = database.query("SessionActivity",
        allColumns, "_id" + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    SessionActivity sa = cursorToSessionActivity(cursor);
    cursor.close();
    return sa;
  }
  
  
  public SessionActivity getSessionActivity(int id){
	  open();
	  Cursor cursor = database.query("SessionActivity",
		        allColumns, "_id" + " = " + id, null,
		        null, null, null);
		    cursor.moveToFirst();
		    SessionActivity sa = cursorToSessionActivity(cursor);
		    cursor.close();
		    return sa;
  }

  public void deleteSessionActivity(SessionActivity sa) {
    long id = sa._id;
    System.out.println("Comment deleted with id: " + id);
    database.delete("SessionActivity", "_id"
        + " = " + id, null);
  }
  
  
  public List<SessionActivity> getSessionActivities(Session session) {
	  open();
	    List<SessionActivity> sas = new ArrayList<SessionActivity>();

	    Cursor cursor = database.query("SessionActivity",
		        allColumns, "fk_session" + " = " + session._id, null,
		        null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	SessionActivity sa = cursorToSessionActivity(cursor);
	      sas.add(sa);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return sas;
	  }

  public List<SessionActivity> getAllSessionActivities() {
	  open();
    List<SessionActivity> sas = new ArrayList<SessionActivity>();

    Cursor cursor = database.query("SessionActivity",
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
    	SessionActivity session = cursorToSessionActivity(cursor);
      sas.add(session);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return sas;
  }

  private SessionActivity cursorToSessionActivity(Cursor cursor) {
	//open();
	SessionActivity sa = new SessionActivity();
    sa._id=(int)cursor.getLong(0);
    SessionDataSource sds = new SessionDataSource(mContext);
    sds.open();
    sa._parent=sds.getSession(cursor.getInt(1));
    sds.close();
    SubCatDataSource sdss = new SubCatDataSource(mContext);
    sdss.open();
    sa._subcat=sdss.getSubCat(cursor.getInt(2));
    sdss.close();
    sa._duration = cursor.getInt(3);
    sa._sid = cursor.getInt(4);
    return sa;
  }

public SessionActivity getSessionActivityServer(int sid) {
	// TODO Auto-generated method stub
	open();
	Cursor cursor = database.query("SessionActivity",
	        allColumns, "sid" + " = " + sid, null,
	        null, null, null);
	    cursor.moveToFirst();
	    SessionActivity sa = cursorToSessionActivity(cursor);
	    cursor.close();
	    return sa;
	
}
}