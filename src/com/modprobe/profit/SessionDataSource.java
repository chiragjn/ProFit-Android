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

public class SessionDataSource {

  // Database fields
  private SQLiteDatabase database;
  private DataBaseHelper dbHelper;
  private String[] allColumns = { "_id","name","sid" };

  public SessionDataSource(Context context) {
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

  public Session createSession(String name, int sid) {
	  open();
    ContentValues values = new ContentValues();
    //values.put("_id", 1);
    values.put("name", name);
    values.put("sid", sid);
    long insertId = database.insert("Session", null,
        values);
    Cursor cursor = database.query("Session",
        allColumns, "_id" + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Session session = cursorToSession(cursor);
    cursor.close();
    return session;
  }
  
  
  public Session getSession(int id){
	  open();
	  Cursor cursor = database.query("Session",
		        allColumns, "_id" + " = " + id, null,
		        null, null, null);
		    cursor.moveToFirst();
		    Session session = cursorToSession(cursor);
		    cursor.close();
		    return session;
  }
  

  public void deleteSession(Session session) {
    long id = session._id;
    System.out.println("Comment deleted with id: " + id);
    database.delete("Session", "_id"
        + " = " + id, null);
  }

  public List<Session> getAllSessions() {
	  open();
    List<Session> sessions = new ArrayList<Session>();

    Cursor cursor = database.query("Session",
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Session session = cursorToSession(cursor);
      sessions.add(session);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return sessions;
  }

  private Session cursorToSession(Cursor cursor) {
	//open();
    Session session = new Session();
    session._id=(int)cursor.getLong(0);
    session._name = cursor.getString(1);
    session._sid = cursor.getInt(2);
    return session;
  }
}