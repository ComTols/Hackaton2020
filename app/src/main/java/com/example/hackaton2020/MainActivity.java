package com.example.hackaton2020;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

	private ZXingScannerView scannerView;
	private TextView textResult;
	private ChargedData chargedData;
	public String questEventID;

	//Beim erstellen der Activity wird diese Methode aufgerufen
	//Ruft onStart() auf
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
		Intent startServiceIntent = new Intent(this, CheckServerService.class);
		PendingIntent startServicePendingIntent = PendingIntent.getService(this, 0, startServiceIntent,0);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000*10, 1000*60*60, startServicePendingIntent);

		scannerView = findViewById(R.id.zxscan);
		textResult = findViewById(R.id.txtBarcodeValue);

		ImageButton menuButton = findViewById(R.id.buttonToMenu);
		menuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickMenuButton();
			}
		});
	}

	private void onClickMenuButton() {
		Intent intentHomeScreen = new Intent(this, HomeScreen.class);
		startActivity(intentHomeScreen);
		finish();
	}

	private void checkAccountInitialized() {
		SharedPreferences accountDetails = getSharedPreferences("accountDetails", MODE_PRIVATE);
		if (!accountDetails.contains("initialized")) {
			Intent intentCreateAccount = new Intent(this, CreateAccount.class);
			startActivity(intentCreateAccount);
		} else {
			String json = accountDetails.getString("savedData", null);
			Gson gson = new Gson();
			Type type = new TypeToken<ChargedData>() {}.getType();
			System.out.println(json);
			chargedData = gson.fromJson(json, type);
		}
	}

	//Nach dem erstellen und bei jedem Start der Activity wird diese Methode aufgerufen
	@Override
	protected void onStart() {
		Dexter.withActivity(this)
			.withPermission(Manifest.permission.CAMERA)
			.withListener(new PermissionListener() {
				@Override
				public void onPermissionGranted(PermissionGrantedResponse response) {
					scannerView.setResultHandler(MainActivity.this);
					scannerView.startCamera();
				}
				@Override
				public void onPermissionDenied(PermissionDeniedResponse response) {
				}
				@Override
				public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
				}
			})
			.check();
		super.onStart();
	}

	//Nach dem Starten und beim fortsetzten der Activity wird diese Methode aufgerufen
	@Override
	protected void onResume() {
		checkAccountInitialized();
		super.onResume();
	}

	//Beim endgültigen beenden wird diese Methode aufgerufen
	@Override
	protected void onDestroy() {
		scannerView.stopCamera();
		super.onDestroy();
	}

	@Override
	public void handleResult(Result rawResult) {
		//QR-Code Syntax: Veranstaltungstyp~ID~Name~Straße+Hausnr.~PLZ~Ort~Details
		//TODO: Bereits eingetragen überprüfen
		if(rawResult.getText().split("~").length != 7) {
			textResult.setText("QR-Code nicht korrekt!");
			return;
		}
		SharedPreferences accountDetails = getSharedPreferences("accountDetails", MODE_PRIVATE);
		String json = accountDetails.getString("savedData", null);
		Gson gson = new Gson();
		Type type = new TypeToken<ChargedData>() {}.getType();
		System.out.println(json);
		ChargedData chargedData = gson.fromJson(json, type);

		for(int i = 0; i < chargedData.getEvents().size(); i++) {
			ChargedData.Events event = chargedData.getEvents().get(i);
			System.out.println(rawResult.getText().split("~")[1]);
			if(event.id.equals(rawResult.getText().split("~")[1])) {
				System.out.println("Event gefunden!");
				questEventID = event.id;
				//TODO: Fix Dialog
				Dialog dialog = new Dialog(this);
				dialog.show();
			}
		}

		textResult.setText(rawResult.getText());
		Intent intentCheckPerson = new Intent(this, CheckPerson.class);
		intentCheckPerson.putExtra("qrResult", rawResult.getText());
		startActivity(intentCheckPerson);
	}

	@SuppressLint("SimpleDateFormat")
	public void logout(String eventID) {
		chargedData.getEventById(eventID).endTime = new SimpleDateFormat("dd/MM/yyyy-HH/mm/ss").format(Calendar.getInstance().getTime());
	}
}