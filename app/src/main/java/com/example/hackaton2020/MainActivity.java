package com.example.hackaton2020;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

	private ZXingScannerView scannerView;
	private TextView textResult;
	private ImageButton menuButton;

	//Beim erstellen der Activity wird diese Methode aufgerufen
	//Ruft onStart() auf
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		checkAccountInitialized();

		setContentView(R.layout.activity_main);
		scannerView = findViewById(R.id.zxscan);
		textResult = findViewById(R.id.txtBarcodeValue);

		menuButton = findViewById(R.id.buttonToMenu);
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
		//TODO: QR-Code Syntax validieren
		//TODO: Bereits eingetragen überprüfen
		textResult.setText(rawResult.getText());
		Intent intentCheckPerson = new Intent(this, CheckPerson.class);
		intentCheckPerson.putExtra("qrResult", rawResult.getText());
		startActivity(intentCheckPerson);
	}
}