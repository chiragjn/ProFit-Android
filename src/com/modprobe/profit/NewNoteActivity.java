package com.modprobe.profit;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.actions.NoteIntents;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class NewNoteActivity extends Activity {

	private static final Uri NOTE_URI = Uri.parse("android-app://com.modprobe.profit/set_note_page");
	GoogleApiClient mClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_note);
		// Get the intent
		Intent intent = getIntent();
		TextView tv = (TextView)findViewById(R.id.test);
		tv.setText("");
        if (NoteIntents.ACTION_CREATE_NOTE.equals(intent.getAction())) {
        	Log.e("Note","here");
        	mClient = new GoogleApiClient.Builder(this)
            .addApi(AppIndex.API).build();
        	dumpIntent(intent);
            if (intent.hasExtra(NoteIntents.EXTRA_NAME)) {
                // Step 2: get the rest of the intent extras and set an alarm
                Log.e("NOTEEE1",""+intent.getStringExtra(NoteIntents.EXTRA_NAME));
                tv.setText(tv.getText().toString()+" "+intent.getStringExtra(NoteIntents.EXTRA_NAME));
            }
            if (intent.hasExtra(NoteIntents.EXTRA_NOTE_QUERY)) {
                // Step 2: get the rest of the intent extras and set an alarm
                Log.e("NOTEEE2",""+intent.getStringExtra(NoteIntents.EXTRA_NOTE_QUERY));
                tv.setText(tv.getText().toString()+" "+intent.getStringExtra(NoteIntents.EXTRA_NOTE_QUERY));
            }
            if (intent.hasExtra(NoteIntents.EXTRA_TEXT)) {
                // Step 2: get the rest of the intent extras and set an alarm
                Log.e("NOTEEE3",""+intent.getStringExtra(NoteIntents.EXTRA_TEXT));
                
                tv.setText(tv.getText().toString()+" "+intent.getStringExtra(NoteIntents.EXTRA_TEXT));
            }
            
            if (intent.hasExtra("android.intent.extra.TEXT")) {
                // Step 2: get the rest of the intent extras and set an alarm
                Log.e("NOTEEE3",""+intent.getStringExtra("android.intent.extra.TEXT"));
                
                tv.setText(tv.getText().toString()+" "+intent.getStringExtra("android.intent.extra.TEXT"));
            }

            // Step 3: report the action through the App Indexing API
            Thing note = new Thing.Builder()
                    .setName("Update Profit")
                    .setDescription("Update Profit")
                    .setUrl(NOTE_URI)
                    .build();

            Action setAlarmAction = new Action.Builder(Action.TYPE_ADD)
                    .setObject(note)
                    .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                    .build();

            AppIndex.AppIndexApi.end(mClient, setAlarmAction);
        }
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
