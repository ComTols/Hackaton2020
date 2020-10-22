package com.example.hackaton2020;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
			Intent startServiceIntent = new Intent(context, CheckServerService.class);
			PendingIntent startServicePendingIntent = PendingIntent.getService(context, 0, startServiceIntent,0);

			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000*10, 1000*60*60, startServicePendingIntent);
		}

	}
}
