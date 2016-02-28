package com.modprobe.profit;

import java.io.IOException;
import java.util.List;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;

public class MainActivity extends AppCompatActivity {
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String TAG = "MainActivity";
	private BroadcastReceiver mRegistrationBroadcastReceiver;
	public static final String SAMPLE_SESSION_NAME = "Afternoon run";
	private static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
	private GoogleApiClient mClient = null;
	private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (!checkPermissions()) {
			requestPermissions();
		}
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setElevation(0);
		if (AppController.getInstance().prefs.getBoolean("first", true)) {
			// Take to edit screen

			sendRegistration();// Account followed by gcm
			setupReciever();// Callback
			setupSharedPref();// Basic Account Setup

			seedDb();
		}
		if (!AppController.getInstance().prefs.getBoolean("profile_completed",
				false)) {
			EditProfileFragment edf = new EditProfileFragment();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.add(R.id.container, edf).commit();
		} else {
			// Use chirags activity main_xml,
			chirags_onCreate();
		}
	}

	@Override
	protected void onResume() {
		Log.e("MAIN", "RESUME");
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mRegistrationBroadcastReceiver,
				new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
		super.onResume();

		// This ensures that if the user denies the permissions then uses
		// Settings to re-enable
		// them, the app will start working.
		buildFitnessClient();
	}

	private void buildFitnessClient() {
		// Create the Google API Client
		if (mClient == null && checkPermissions()) {
			mClient = new GoogleApiClient.Builder(this)
					.addApi(Fitness.HISTORY_API)
					.addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
					.addConnectionCallbacks(
							new GoogleApiClient.ConnectionCallbacks() {
								@Override
								public void onConnected(Bundle bundle) {
									Log.i(TAG, "Connected!!!");

								}

								@Override
								public void onConnectionSuspended(int i) {
									// If your connection to the sensor gets
									// lost at some point,
									// you'll be able to determine the reason
									// and react to it here.
									if (i == ConnectionCallbacks.CAUSE_NETWORK_LOST) {
										Log.i(TAG,
												"Connection lost.  Cause: Network Lost.");
									} else if (i == ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
										Log.i(TAG,
												"Connection lost.  Reason: Service Disconnected");
									}
								}
							})
					.enableAutoManage(this, 0,
							new GoogleApiClient.OnConnectionFailedListener() {
								@Override
								public void onConnectionFailed(
										ConnectionResult result) {
									Log.i(TAG,
											"Google Play services connection failed. Cause: "
													+ result.toString());
									// TODO

								}
							}).build();
		}
	}

	private boolean checkPermissions() {
		int permissionState = ActivityCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION);
		return permissionState == PackageManager.PERMISSION_GRANTED;
	}

	private void requestPermissions() {
		boolean shouldProvideRationale = ActivityCompat
				.shouldShowRequestPermissionRationale(this,
						Manifest.permission.ACCESS_FINE_LOCATION);

		// Provide an additional rationale to the user. This would happen if the
		// user denied the
		// request previously, but didn't check the "Don't ask again" checkbox.
		if (shouldProvideRationale) {
			Log.i(TAG,
					"Displaying permission rationale to provide additional context.");
			Snackbar.make(findViewById(R.id.main_activity_view),
					R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
					.setAction("OK", new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// Request permission
							ActivityCompat
									.requestPermissions(
											MainActivity.this,
											new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
											REQUEST_PERMISSIONS_REQUEST_CODE);
						}
					}).show();
		} else {
			Log.i(TAG, "Requesting permission");
			// Request permission. It's possible this can be auto answered if
			// device policy
			// sets the permission in a given state or the user denied the
			// permission
			// previously and checked "Never ask again".
			ActivityCompat.requestPermissions(MainActivity.this,
					new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
					REQUEST_PERMISSIONS_REQUEST_CODE);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
			@NonNull String[] permissions, @NonNull int[] grantResults) {
		Log.i(TAG, "onRequestPermissionResult");
		if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
			if (grantResults.length <= 0) {
				// If user interaction was interrupted, the permission request
				// is cancelled and you
				// receive empty arrays.
				Log.i(TAG, "User interaction was cancelled.");
			} else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Permission was granted.
				buildFitnessClient();
			} else {
				Log.e(TAG, "Google Fit connection permissions denied!");
			}
		}
	}

	protected void setupSharedPref() {
		String bodyTypeArr[] = { "Slim", "Fit", "Plum", "Obese" };
		int fitons = 1000;
		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String uuid = tManager.getDeviceId();
		Editor e = AppController.getInstance().prefs.edit();
		e.putString("uuid", uuid);
		e.putBoolean("first", false);
		e.putInt("fitons", fitons);

		// String name = "Shyam Mehta";
		// int age = 20;
		// int height = 170;
		// int weightKg = 61;
		// int bodyType = 0;
		// String diseases = "None";
		// String email = "jain.chirag925@gmail.com";
		// String username = "paren_desai";
		// e.putString("name", name);
		// e.putInt("age", age);
		// e.putInt("height", height);
		// e.putInt("weightKg", weightKg);
		// e.putString("diseases", diseases);
		// e.putString("email", email);
		// e.putString("username", username);
		// e.putInt("bodyType", bodyType);
		// e.putString("image", "");
		e.commit();
		e.apply();
	}

	private boolean checkPlayServices() {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability
				.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(this, resultCode,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				mRegistrationBroadcastReceiver);
		super.onPause();
	}

	protected void sendRegistration() {
		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		final String uuid = tManager.getDeviceId();
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String charset = "UTF-8";
				String requestURL = Constants.BAE_URL + "register/";

				MultipartUtility multipart;
				try {
					multipart = new MultipartUtility(requestURL, charset);
					multipart.addFormField("uuid", uuid.trim());
					List<String> response = multipart.finish(); // response
					Log.e("resp", "" + response.toString());

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				// Assume response is correct
				if (checkPlayServices()) {
					// Start IntentService to register this application with
					// GCM.
					Intent intent = new Intent(MainActivity.this,
							RegistrationIntentService.class);
					startService(intent);
				}
				super.onPostExecute(result);
			}

		}.execute();
	}

	protected void setupReciever() {
		mRegistrationBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(context);
				boolean sentToken = sharedPreferences.getBoolean(
						QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
				if (sentToken) {
					Log.e("GCM", "Done");
				} else {
					Log.e("GCM", "Not Done");
				}
			}
		};
	}

	protected void chirags_onCreate() {
		// Ask Chirag to put shiz here

		MainActivityFragment mainActivityFragment = new MainActivityFragment();
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.container, mainActivityFragment).commit();
	}
	
	
	private void seedDb() {
		// TODO Auto-generated method stub
		
		ActivityDataSource ads = new ActivityDataSource(this);
		CategoryDataSource cds = new CategoryDataSource(this);
		cds.open();
		Category cc = cds.createCategory("Cardio", 6, 0);
		Category cc1 = cds.createCategory("Sports", 7, 1);
		Category cc2 = cds.createCategory("Gym", 8, 2);
		cds.close();

		SubCatDataSource sds = new SubCatDataSource(this);
		sds.open();
		SubCat ss = sds.createSubCat(cc1, "Tennis", 210, 0);
		SubCat ss1 = sds.createSubCat(cc1, "Badminton", 135, 1);
		SubCat ss2 = sds.createSubCat(cc, "Walking", 135, 3);
		SubCat ss3 = sds.createSubCat(cc, "Running", 240, 4);
		SubCat ss4 = sds.createSubCat(cc2, "Weight Lifting", 90, 5);
		SubCat ss5 = sds.createSubCat(cc2, "Yoga", 120, 6);
		sds.close();

		ads.open();
		ads.createActivity(ss, 60, Helper.getFitons(60, 6, ss._exertion), 6, 0);
		ads.createActivity(ss1, 30, Helper.getFitons(30, 5, ss1._exertion), 5, 1);
		ads.createActivity(ss2, 120, Helper.getFitons(120, 8, ss2._exertion), 8, 2);
		ads.createActivity(ss5, 45, Helper.getFitons(45, 10, ss5._exertion), 10, 100);
		ads.createActivity(ss4, 90, Helper.getFitons(90, 10, ss4._exertion), 10, 3);
		ads.createActivity(ss3, 100, Helper.getFitons(100, 4, ss3._exertion), 4, 4);
		ads.close();

		GoalDataSource gds = new GoalDataSource(this);
		gds.open();
		gds.createGoal("5k Marathon", "300,200,100", 0, 1);
		gds.createGoal("Mr Olympiad", "250,150,600", 1, 1);
		gds.createGoal("10k Marathon", "400,300,200", 3, 0);
		gds.createGoal("Weight Loss", "350,150,150", 4, 0);
		gds.close();
		
		SessionDataSource ssds = new SessionDataSource(this);
		ssds.open();
		Session sess = ssds.createSession("monday workout", 0);
		ssds.close();
		
		SessionActivityDataSource sads = new SessionActivityDataSource(this);
		sads.open();
		sads.createSessionActivity(sess, ss4, 20, 0);
		sads.createSessionActivity(sess, ss3, 30, 1);
		sads.createSessionActivity(sess, ss5, 10, 2);
		sads.close();
		
		WeightLogDataSource wlds = new WeightLogDataSource(this);
		wlds.open();
		wlds.createWeightLog("26/2/16", 55, 0);
		wlds.createWeightLog("27/2/16", 59, 1);
		wlds.createWeightLog("28/2/16", 56, 2);
		wlds.close();
		
	}
}
