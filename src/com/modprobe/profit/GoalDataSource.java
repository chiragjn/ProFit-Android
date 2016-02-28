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

public class GoalDataSource {

  // Database fields
  private SQLiteDatabase database;
  private DataBaseHelper dbHelper;
  private String[] allColumns = { "_id","name","fitons","sid" ,"now"};

  public GoalDataSource(Context context) {
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

  public Goal createGoal(String name, String fitons, int sid, int now) {
	  open();
    ContentValues values = new ContentValues();
    //values.put("_id", 1);
    values.put("name", name);
    values.put("fitons", fitons);
    values.put("sid", sid);
    values.put("now", now);
    long insertId = database.insert("Goal", null,
        values);
    Cursor cursor = database.query("Goal",
        allColumns, "_id" + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Goal goal = cursorToGoal(cursor);
    cursor.close();
    return goal;
  }
  
  
  public Goal getGoal(int id){
	  open();
	  Cursor cursor = database.query("Goal",
		        allColumns, "_id" + " = " + id, null,
		        null, null, null);
		    cursor.moveToFirst();
		    Goal goal = cursorToGoal(cursor);
		    cursor.close();
		    return goal;
  }
  

  public void deleteGoal(Goal goal) {
    long id = goal._id;
    System.out.println("Comment deleted with id: " + id);
    database.delete("Goal", "_id"
        + " = " + id, null);
  }

  public List<Goal> getAllGoals() {
	  open();
    List<Goal> goals = new ArrayList<Goal>();

    Cursor cursor = database.query("Goal",
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
    	Goal goal = cursorToGoal(cursor);
      goals.add(goal);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return goals;
  }
  
  public int updateGoal(Goal goal){
			open();
			ContentValues values = new ContentValues();
		    values.put("now", 1);
			return database.update("Goal", values, "_id = ?", new String[] { ""+goal._id });
  }

  private Goal cursorToGoal(Cursor cursor) {
	//open();
	  Goal goal = new Goal();
	  goal._id=(int)cursor.getLong(0);
	  goal._name = cursor.getString(1);
	  goal._fitons = cursor.getString(2);
	  goal._sid = cursor.getInt(3);
	  goal._now = cursor.getInt(4);
    return goal;
  }
}