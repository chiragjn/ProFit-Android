package com.modprobe.profit;

import android.app.Application;
import android.content.SharedPreferences;

public class AppController extends Application {

	public SharedPreferences prefs;

	public static final String TAG = AppController.class.getSimpleName();

	private static AppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		TypeFaceUtil.overrideFont(getApplicationContext(), "SERIF",
				"fonts/dinroundweb.ttf"); // font from assets:
											// "assets/fonts/Roboto-Regular.ttf
		prefs = this.getSharedPreferences("profit", 2);
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

}