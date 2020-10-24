package com.example.hackaton2020;

import android.Manifest;
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

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

	private ZXingScannerView scannerView;
	private TextView textResult;
	private ChargedData chargedData;
	private String questEventID;

	private class Dialog extends AppCompatDialogFragment {
		private MainActivity mainActivity;

		Dialog(MainActivity main) {
			super();
			this.mainActivity = main;
		}

		@NonNull
		@Override
		public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Bereits registriert.")
					.setMessage("Möchten Sie sich aus dem Restaurant abmelden oder eine weitere Person hinzufügen?")
					.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intentCheckPerson = new Intent(mainActivity, CheckPerson.class);
					//TODO: Personen dem Event zuordnen
					intentCheckPerson.putExtra("joinUserToEvent", true);
					startActivity(intentCheckPerson);
				}
			}).setNegativeButton("Abmelden", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mainActivity.logout(mainActivity.questEventID);
				}
			});
			return super.onCreateDialog(savedInstanceState);
		}
	}

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
			if(event.id == rawResult.getText().split("~")[1]) {
				questEventID = event.id;
				Dialog dialog = new Dialog(this);
				dialog.show(getSupportFragmentManager(), "example dialog");
			}
		}

		textResult.setText(rawResult.getText());
		Intent intentCheckPerson = new Intent(this, CheckPerson.class);
		intentCheckPerson.putExtra("qrResult", rawResult.getText());
		startActivity(intentCheckPerson);
	}

	private void logout(String eventID) {
		//TODO: Aus Event ausloggen
	}
}