package com.example.hackaton2020;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ShowLoggedOut extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_logged_out);

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(Notifications.ALREADY_LOGGED_IN_NOTIFICATION_ID);

		SharedPreferences accountDetails = getSharedPreferences("accountDetails", MODE_PRIVATE);

		//Lesen
		String json = accountDetails.getString("savedData", null);
		Gson gson = new Gson();
		Type type = new TypeToken<ChargedData>() {}.getType();
		System.out.println(json);
		ChargedData chargedData = gson.fromJson(json, type);

		//try {
			ChargedData.Events ev = chargedData.getNotFinishedEvent();
			System.out.println("Das Objekt:");
			System.out.println(ev);
			ev.endTime = new SimpleDateFormat("dd/MM/yyyy-HH/mm/ss").format(Calendar.getInstance().getTime());
			chargedData.changeEventByStartTime(ev);
		/*} catch (Exception e) {
			e.fillInStackTrace();
			System.out.println("WARNUNG!");
		} */

		SharedPreferences.Editor editor = accountDetails.edit();
		editor.putString("savedData", new Gson().toJson(chargedData));
		editor.apply();
	}
}