package com.example.hackaton2020;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //Beim erstellen der Activity wird diese Methode aufgerufen
    //Ruft onStart() auf
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	//Nach dem erstellen und bei jedem Start der Activity wird diese Methode aufgerufen
    @Override
    protected void onStart() {
        super.onStart();
    }

    //Nach dem Starten und beim fortsetzten der Activity wird diese Methode aufgerufen
    @Override
    protected void onResume() {
        super.onResume();
    }

    //Beim pausieren der Methode wird diese Methode aufgerufen (zB. Activity gerät in den Hintergrund)
    @Override
    protected void onPause() {
	    super.onPause();
    }

    //Beim beenden der Activity wird diese Mathode aufgerufen (zB. beim minimieren)
    @Override
	protected void onStop() {
		super.onStop();
	}

	//Nach dem beenden und beim zurückkehren in die Activity wird diese Methode aufgerufen
    //Ruft onStart() auf
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    //Beim endgültigen beenden wird diese MEthode aufgerufen
    @Override
	protected void onDestroy() {
		super.onDestroy();
	}
}