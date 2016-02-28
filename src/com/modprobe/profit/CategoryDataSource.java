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

public class CategoryDataSource {

  // Database fields
  private SQLiteDatabase database;
  private DataBaseHelper dbHelper;
  private String[] allColumns = { "_id","name","exertion","sid" };

  public CategoryDataSource(Context context) {
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

  public Category createCategory(String name, int exertion, int sid) {
	  open();
    ContentValues values = new ContentValues();
    //values.put("_id", 1);
    values.put("name", name);
    values.put("exertion", exertion);
    values.put("sid", sid);
    long insertId = database.insert("Category", null,
        values);
    Cursor cursor = database.query("Category",
        allColumns, "_id" + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Category category = cursorToCategory(cursor);
    cursor.close();
    return category;
  }
  
  
  public Category getCategory(int id){
	  open();
	  Cursor cursor = database.query("Category",
		        allColumns, "_id" + " = " + id, null,
		        null, null, null);
		    cursor.moveToFirst();
		    Category newcategory = cursorToCategory(cursor);
		    cursor.close();
		    return newcategory;
  }
  

  public void deleteCategory(Category category) {
    long id = category._id;
    System.out.println("Comment deleted with id: " + id);
    database.delete("Category", "_id"
        + " = " + id, null);
  }

  public List<Category> getAllCategories() {
	  open();
    List<Category> categories = new ArrayList<Category>();

    Cursor cursor = database.query("Category",
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Category category = cursorToCategory(cursor);
      categories.add(category);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return categories;
  }

  private Category cursorToCategory(Cursor cursor) {
	//open();
    Category category = new Category();
    category._id=(int)cursor.getLong(0);
    category._name = cursor.getString(1);
    category._exertion = cursor.getInt(2);
    category._sid = cursor.getInt(3);
    return category;
  }
}