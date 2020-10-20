package com.example.hackaton2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ShowReady extends AppCompatActivity {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_ready);

		Button back = findViewById(R.id.showReady_backBtn);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				navigateUpTo(new Intent(getBaseContext(), MainActivity.class));
			}
		});
	}
}