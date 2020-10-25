package com.example.hackaton2020;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckServerService extends Service {

	//TODO: API schreiben
	public static final String API_URL = "http://hackathon.myddns.me";

	public static class DangerTimes {
		public ArrayList<Times> dangerTimes = new ArrayList<>();
	}
	public static class Times {
		public String start;
		public String id;
		public String end;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if(internetAvalable()) {
			requestDangerTimes();
			System.out.println();
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
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				System.out.println("Antwort erhalten!");
				System.out.println(response);
				SharedPreferences accountDetails = getSharedPreferences("accountDetails", MODE_PRIVATE);

				//Lesen
				String json = accountDetails.getString("savedData", null);
				Gson gson = new Gson();
				Type type = new TypeToken<ChargedData>() {}.getType();
				System.out.println(json);
				ChargedData chargedData = gson.fromJson(json, type);

				DangerTimes dTimes = new Gson().fromJson(response, new TypeToken<DangerTimes>() {}.getType());
				for(int i = 0; i < dTimes.dangerTimes.size(); i++) {
					for(int j = 0; j < chargedData.getEvents().size(); j++) {
						if(dTimes.dangerTimes.get(i).id.equals(chargedData.getEvents().get(j).id)) {
							try {
								String testDate = chargedData.getEvents().get(j).endTime;
								System.out.println(testDate);
								@SuppressLint("SimpleDateFormat") Date dTimesStart = new SimpleDateFormat("dd/MM/yyyy-HH/mm/ss").parse(dTimes.dangerTimes.get(i).start);
								@SuppressLint("SimpleDateFormat") Date dTimesEnd = new SimpleDateFormat("dd/MM/yyyy-HH/mm/ss").parse(dTimes.dangerTimes.get(i).end);
								@SuppressLint("SimpleDateFormat") Date chargedDataStart = new SimpleDateFormat("dd/MM/yyyy-HH/mm/ss").parse(testDate);
								@SuppressLint("SimpleDateFormat") Date chargedDataEnd = new SimpleDateFormat("dd/MM/yyyy-HH/mm/ss").parse(testDate);
								if(dTimesStart.before(chargedDataStart) && dTimesEnd.before(chargedDataStart)) {
									//Keine Überschneidung
								} else if(dTimesStart.after(chargedDataEnd) && dTimesEnd.after(chargedDataEnd)) {
									//Keine Überschneidung
								} else {
									//TODO: Nutzer warnen!
									System.out.println("WARNUNG GEHT RAUS!!!");
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println(error);
			}
		}){
			@Override
			protected Map<String,String> getParams() {
				Map<String,String> params = new HashMap<String, String>();
				params.put("action", "requestDangerTimes");
				return params;
			}
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "application/x-www-form-urlencoded");
				return params;
			}
		};
		System.out.println("Request Gesendet.");
		queue.add(stringRequest);
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
