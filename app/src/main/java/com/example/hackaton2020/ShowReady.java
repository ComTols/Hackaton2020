package com.example.hackaton2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowReady extends AppCompatActivity {

	int countCheckedUsers;
	TextView counter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_ready);

		countCheckedUsers = getIntent().getIntExtra("countCheckedUsers", 1);
		System.out.println("Variable übergeben: " + countCheckedUsers);

		Button back = findViewById(R.id.showReady_backBtn);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				navigateUpTo(new Intent(getBaseContext(), HomeScreen.class));
			}
		});

		counter = findViewById(R.id.showReady_countUsers);
		Intent i = new Intent(this, Notifications.class);
		startService(i);
	}

	@Override
	protected void onStart() {
		super.onStart();
		counter.setText("" + countCheckedUsers);
	}
}