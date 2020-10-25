package com.example.hackaton2020;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class Notifications extends Service {
	public static final int ALREADY_LOGGED_IN_NOTIFICATION_ID = 1;
	public Notifications() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		displayNotification();
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	private void displayNotification() {
		System.out.println("Du wirst benachrichtigt.");

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = "Im Restaurant";
			String description = "Benachrichtigung zum Beenden des Aufenthalts im Restaurant oder beim Event.";
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel("1", name, importance);
			channel.setDescription(description);
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}


		Intent intentMainActivity = new Intent(this, ShowLoggedOut.class);
		intentMainActivity.putExtra("logout", true);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentMainActivity,0);

		NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "1")
				.setContentTitle(getResources().getString(R.string.notification_title))
				.setContentText(getResources().getString(R.string.notification_text))
				.setSmallIcon(R.drawable.corona_tracker_logo_white)
				.setColor(getResources().getColor(R.color.accent))
				.setVibrate(new long[] {0,300,300,300})
				.setLights(getResources().getColor(R.color.accent), 1000, 5000)
				.setAutoCancel(false)
				.setPriority(NotificationCompat.PRIORITY_LOW)
				.addAction(R.drawable.app_logo_bar_icon, getResources().getString(R.string.notification_action), pendingIntent)
				.setOngoing(true);

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(ALREADY_LOGGED_IN_NOTIFICATION_ID, notification.build());
	}
}