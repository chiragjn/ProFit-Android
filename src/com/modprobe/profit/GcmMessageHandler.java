package com.modprobe.profit;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class GcmMessageHandler extends GcmListenerService {
	public static final int MESSAGE_NOTIFICATION_ID = 435345;

	@Override
	public void onMessageReceived(String from, Bundle dataB) {
		try{
		String type = dataB.getString("type");
		String data = dataB.getString("data");
		String topic = "", message = "";
		
		Log.e("GCM", "Recieved");
		createNotification(topic, message);
		}catch(Exception ignored)
		{
			
		}
		
	}

	

	// Creates notification based on title and body received
	private void createNotification(String title, String body) {

		Context context = getBaseContext();
		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_ONE_SHOT);
		Notification note = new Notification(R.drawable.ic_launcher, "ProFit Alert",
				System.currentTimeMillis());

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);
		note = builder.setContentIntent(pi).setSound(alarmSound)
				.setSmallIcon(R.drawable.ic_launcher).setTicker(title)
				.setAutoCancel(true).setContentTitle(title)
				.setContentText(body).build();
		int id = 123456789;
		manager.notify(id, note);

	}

}