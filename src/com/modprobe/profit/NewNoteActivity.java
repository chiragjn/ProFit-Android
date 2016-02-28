package com.modprobe.profit;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.actions.NoteIntents;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class NewNoteActivity extends ActionBarActivity {

	private static final Uri NOTE_URI = Uri.parse("android-app://com.modprobe.profit/set_note_page");
	GoogleApiClient mClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_note);
		// Get the intent
		Intent intent = getIntent();
        if (NoteIntents.ACTION_CREATE_NOTE.equals(intent.getAction())) {
        	Log.e("Note","here");
        	mClient = new GoogleApiClient.Builder(this)
            .addApi(AppIndex.API).build();
        	dumpIntent(intent);
            if (intent.hasExtra(NoteIntents.EXTRA_NAME)) {
                Log.e("NOTEEE1",""+intent.getStringExtra(NoteIntents.EXTRA_NAME));
            }
            if (intent.hasExtra(NoteIntents.EXTRA_NOTE_QUERY)) {
                Log.e("NOTEEE2",""+intent.getStringExtra(NoteIntents.EXTRA_NOTE_QUERY));
            }
            if (intent.hasExtra(NoteIntents.EXTRA_TEXT)) {
                Log.e("NOTEEE3",""+intent.getStringExtra(NoteIntents.EXTRA_TEXT));
                //Works with adb am
                processString(intent.getStringExtra(NoteIntents.EXTRA_TEXT));
            }
            
            if (intent.hasExtra("android.intent.extra.TEXT")) {
                Log.e("NOTEEE4",""+intent.getStringExtra("android.intent.extra.TEXT"));
                //Works with googlenow
                processString(intent.getStringExtra("android.intent.extra.TEXT"));
            }

            // report the action through the App Indexing API
            Thing note = new Thing.Builder()
                    .setName("Update ProFit")
                    .setDescription("Update ProFit")
                    .setUrl(NOTE_URI)
                    .build();

            Action setAlarmAction = new Action.Builder(Action.TYPE_ADD)
                    .setObject(note)
                    .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                    .build();

            AppIndex.AppIndexApi.end(mClient, setAlarmAction);
            
            
        }
	}
	
	private void processString(String stringExtra) {
		// TODO Auto-generated method stub
		
		/*
		 * Syntax for VR :
		 * 	Session - Take a Note Session abc done with intensity X
		 * 	Activty - Take a Note Activity abc done with intensity X for Y minutes
		 *  WeightLog - Take a Note Today I weigh X kilograms
		 */
		int flagSession = 0 , flagActivity = 0 , flagWeight = 0, flagDone = 0, flagIntensity = 0, flagMinutes = 0;
		String arr[] = stringExtra.split(" ");
		for(int i = 0 ; i<arr.length; i++){
			if(arr[i].trim().equalsIgnoreCase("Session")){
				flagSession=1;
			}
			if(arr[i].trim().equalsIgnoreCase("Activity")){
				flagActivity=1;
			}
			if(arr[i].trim().equalsIgnoreCase("Weigh")){
				flagWeight=1;
			}
			if(arr[i].trim().equalsIgnoreCase("Done")){
				flagDone=i;
			}
			if(arr[i].trim().equalsIgnoreCase("Intensity")){
				flagIntensity=i;
			}
			if(arr[i].trim().equalsIgnoreCase("Minutes")){
				flagMinutes=i;
			}
			
		}
		
		try{
			if(flagActivity==1){
				//Activity abc done with intensity X for Y minutes
				String activityname = arr[1].trim();
				for(int i = 2; i < flagDone; i++)	activityname+=" "+arr[i].trim();
				activityname = activityname.trim();
				int intensity = Integer.parseInt(arr[flagIntensity+1].trim());
				int duration = Integer.parseInt(arr[flagMinutes-1].trim());
				SubCat sc = null;
				SubCatDataSource scds = new SubCatDataSource(this);
				scds.open();
				List<SubCat> lis = scds.getAllSubCats();
				scds.close();
				for(SubCat s : lis)	if(s._name.trim().equalsIgnoreCase(activityname))	sc = s;
				ActivityDataSource ads = new ActivityDataSource(this);
				ads.open();
				ads.createActivity(sc, duration, Helper.getFitons(duration, flagIntensity, sc._exertion), intensity, (int)(Math.random() * 50002));
				ads.close();
				
				getSupportActionBar().setTitle(R.string.app_name);
				getSupportFragmentManager().beginTransaction()
						.add(R.id.container, new FeedbackFragment(intensity, Helper.getFitons(duration, intensity, sc._exertion), sc._parent._name, sc._name)).commit();
				//openChiragsFeedbcakFragment
			}
			else if(flagSession==1){
				//Session abc done with intensity X
				String sessionname = arr[1].trim();
				for(int i = 2; i< flagDone; i++)	sessionname+=" "+arr[i].trim();
				int intensity = Integer.parseInt(arr[flagIntensity+1].trim());
				SessionDataSource sds = new SessionDataSource(this);
				Session ss = null;
				sds.open();
				List<Session> lis = sds.getAllSessions();
				sds.close();
				for(Session s: lis) if(s._name.trim().equalsIgnoreCase(sessionname.trim()))	ss = s;
				SessionActivityDataSource sads = new SessionActivityDataSource(this);
				sads.open();
				List<SessionActivity> sas = sads.getSessionActivities(ss);
				sads.close();
				ActivityDataSource ads = new ActivityDataSource(this);
				ads.open();
				int fitonsum=0;
				for(SessionActivity sa : sas){
					ads.createActivity(sa._subcat, sa._duration, Helper.getFitons(sa._duration, intensity, sa._subcat._exertion), intensity, (int)(Math.random() * 50002));
					fitonsum+=Helper.getFitons(sa._duration, intensity, sa._subcat._exertion);
				}
				ads.close();
				//openChiragsFeedbcakFragment
				getSupportActionBar().setTitle(R.string.app_name);
				getSupportFragmentManager().beginTransaction()
						.add(R.id.container, new FeedbackFragment(intensity, fitonsum, sessionname)).commit();
				
			}
			else if(flagWeight==1){
				//Today I weigh X kilograms
				int weight = Integer.parseInt(arr[3]);
				WeightLogDataSource wlds = new WeightLogDataSource(this);
				wlds.open();
				wlds.createWeightLog("28/2/16", weight, (int)(Math.random() * 50002));
				wlds.close();
				//openChiragsFeedbcakFragment
				getSupportFragmentManager().beginTransaction()
				.add(R.id.container, new FeedbackFragment(weight)).commit();
			}

		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "Didn't get that, please try again",Toast.LENGTH_SHORT).show();
			finish();
			
		}
		Log.e("flags",flagActivity+""+flagSession+""+flagWeight);
		
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mClient.connect();
	    Action viewAction = Action.newAction(Action.TYPE_VIEW, "Update Profit", NOTE_URI);
	    AppIndex.AppIndexApi.start(mClient, viewAction);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Action viewAction = Action.newAction(Action.TYPE_VIEW, "Update Profit", NOTE_URI);
	     AppIndex.AppIndexApi.end(mClient, viewAction);
	     mClient.disconnect();
		super.onStop();
	}
	
	
	private static final String TAG = "IntentDump";

    public static void dumpIntent(Intent i){
        Bundle bundle = i.getExtras();
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            Log.d(TAG, String.format("%s %s (%s)", key,  
                value.toString(), value.getClass().getName()));
        }
    }
    
    

	
}
