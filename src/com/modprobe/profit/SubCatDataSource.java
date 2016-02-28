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

public class SubCatDataSource {

  // Database fields
  private SQLiteDatabase database;
  private DataBaseHelper dbHelper;
  private String[] allColumns = {"_id","fk_category","name","exertion","sid"};
  Context mContext;
  public SubCatDataSource(Context context) {
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

  public SubCat createSubCat(Category parent,String name, int exertion, int sid) {
	  open();
    ContentValues values = new ContentValues();
    values.put("fk_category", parent._id);
    values.put("name", name);
    values.put("exertion", exertion);
    values.put("sid", sid);
    long insertId = database.insert("SubCat", null,
        values);
    Cursor cursor = database.query("SubCat",
        allColumns, "_id" + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    SubCat subcat = cursorToSubCat(cursor);
    cursor.close();
    return subcat;
  }
  
  
  public SubCat getSubCat(int id){
	  open();
	  Cursor cursor = database.query("SubCat",
		        allColumns, "_id" + " = " + id, null,
		        null, null, null);
		    cursor.moveToFirst();
		    SubCat subcat = cursorToSubCat(cursor);
		    cursor.close();
		    return subcat;
  }

  public void deleteSubCat(SubCat subcat) {
    long id = subcat._id;
    System.out.println("Comment deleted with id: " + id);
    database.delete("SubCat", "_id"
        + " = " + id, null);
  }
  
  
  public List<SubCat> getSubCats(Category category) {
	  open();
	    List<SubCat> subcats = new ArrayList<SubCat>();

	    Cursor cursor = database.query("SubCat",
		        allColumns, "fk_category" + " = " + category._id, null,
		        null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	SubCat subcat = cursorToSubCat(cursor);
	      subcats.add(subcat);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return subcats;
	  }

  public List<SubCat> getAllSubCats() {
	  open();
    List<SubCat> subcats = new ArrayList<SubCat>();

    Cursor cursor = database.query("SubCat",
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
    	SubCat subcat = cursorToSubCat(cursor);
      subcats.add(subcat);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return subcats;
  }

  private SubCat cursorToSubCat(Cursor cursor) {
	//open();
	SubCat subcat = new SubCat();
    subcat._id=(int)cursor.getLong(0);
    CategoryDataSource sds = new CategoryDataSource(mContext);
    sds.open();
    subcat._parent=sds.getCategory(cursor.getInt(1));
    sds.close();
    subcat._name = cursor.getString(2);
    subcat._exertion = cursor.getInt(3);
    subcat._sid = cursor.getInt(4);
    return subcat;
  }


}