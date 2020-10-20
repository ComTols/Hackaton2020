package com.example.hackaton2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class CheckPerson extends AppCompatActivity {

	CheckBox[] otherUsers = new CheckBox[70];
	ConstraintLayout otherUsersList;

	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_person);

		otherUsersList = findViewById(R.id.addPerson_whoIsGuest_otherUsersList);
		checkOtherUsersArray();

		Button addPerson = findViewById(R.id.addPerson_addPersonButton);
		addPerson.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickAddPerson();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkOtherUsersArray();
	}

	private void checkOtherUsersArray() {
		System.out.println("Checke auf neue Personen");
		SharedPreferences accountDetails = getSharedPreferences("accountDetails", MODE_PRIVATE);
		otherUsers = new CheckBox[70];
		int i = 0;
		while(accountDetails.contains("otherUser["+i+"]")) {
			otherUsers[i] = new CheckBox(getApplicationContext());
			otherUsers[i].setText(accountDetails.getString("otherUser["+i+"]_forename", "")+ " " + accountDetails.getString("otherUser["+i+"]_forename", ""));
			i++;
		}
		otherUsersList.removeAllViews();
		for (i = 0; i < otherUsers.length; i++) {
			if(otherUsers[i]!=null) {
				otherUsersList.addView(otherUsers[i]);
			}
		}
	}

	protected void onClickAddPerson() {
		Intent intentAddPerson = new Intent(this, AddPerson.class);
		startActivity(intentAddPerson);
	}
}