package com.example.hackaton2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CheckPerson extends AppCompatActivity {

	ArrayList<String> otherUsers = new ArrayList<>();
	ListView otherUsersList;
	Button finishButton;
	String[] qrResult;
	TextView headline;

	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_person);

		otherUsersList = findViewById(R.id.addPerson_whoIsGuest_otherUsersList);
		otherUsersList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		checkOtherUsersArray();

		Button addPerson = findViewById(R.id.addPerson_addPersonButton);
		addPerson.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickAddPerson();
			}
		});

		finishButton = findViewById(R.id.check_finishButton);
		finishButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickFinishButton();
			}
		});

		qrResult = getIntent().getStringExtra("qrResult").split("~");
		headline = findViewById(R.id.checkPerson_restaurantName);
		headline.setText(qrResult[2]);
	}

	private void onClickFinishButton() {
		//TODO: Schreibe Besuchszeit in SharedPreferences

		SparseBooleanArray checked = otherUsersList.getCheckedItemPositions();
		int countChecked = 0;
		for(int i = 0; i < otherUsersList.getCount(); i++) {
			if(checked.get(i)) {
				countChecked++;
			}
		}

		Intent intentShowReady = new Intent(this, ShowReady.class);
		intentShowReady.putExtra("countCheckedUsers", countChecked);
		startActivity(intentShowReady);
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkOtherUsersArray();
	}

	private void checkOtherUsersArray() {
		System.out.println("Checke auf neue Personen");
		SharedPreferences accountDetails = getSharedPreferences("accountDetails", MODE_PRIVATE);
		otherUsers.clear();
		int i = 0;
		while(accountDetails.contains("otherUser["+i+"]")) {
			otherUsers.add(accountDetails.getString("otherUser["+i+"]_forename", "")+ " " + accountDetails.getString("otherUser["+i+"]_lastname", ""));
			i++;
		}
		otherUsers.add(0, getResources().getString(R.string.checkPerson_whoIsGuest_me));
		ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, otherUsers) {
			@Override
			public boolean isEnabled(int position) {
				//Verbiete erstes Item (Ich) zu ändern
				if(position == 0) {
					return false;
				}
				return super.isEnabled(position);
			}
		};
		otherUsersList.setAdapter(arrayAdapter);
		otherUsersList.setItemChecked(0, true);
	}

	protected void onClickAddPerson() {
		Intent intentAddPerson = new Intent(this, AddPerson.class);
		startActivity(intentAddPerson);
	}
}