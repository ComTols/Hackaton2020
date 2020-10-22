package com.example.hackaton2020;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class CheckServerService extends Service {

	//TODO: API schreiben
	public static final String API_URL = "";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if(internetAvalable()) {
			//requestDangerTimes(); <-- API_URL angeben, danach Kommentar entfernen
			Toast.makeText(this, "Server nach neuen Gefahrenzeiten fragen.", Toast.LENGTH_SHORT).show();
		}

		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private boolean internetAvalable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnectedOrConnecting();
	}

	private void requestDangerTimes() {
		try {
			String textParam = "requestDangerTimes=" + URLEncoder.encode("true", "UTF-8");

			URL scriptURL = new URL(API_URL);
			HttpURLConnection connection = (HttpURLConnection) scriptURL.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setFixedLengthStreamingMode(textParam.getBytes().length);
			OutputStreamWriter contentWriter = new OutputStreamWriter(connection.getOutputStream());
			contentWriter.write(textParam);
			contentWriter.flush();
			contentWriter.close();

			InputStream inputStream = connection.getInputStream();
			String answer = getTextFromInputStream(inputStream);
			inputStream.close();
			connection.disconnect();

			//TODO: Server Antwort mit eigener Liste vergleichen und Änderungen eintragen; evtl. Nutzer warnen
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getTextFromInputStream (InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder stringBuilder = new StringBuilder();

		String aktZeile;
		try {
			while ((aktZeile = reader.readLine()) != null) {
				stringBuilder.append(aktZeile);
				stringBuilder.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString().trim();
	}
}
