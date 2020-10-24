package com.example.hackaton2020;

import androidx.annotation.Nullable;
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
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CheckPerson extends AppCompatActivity {

	ArrayList<String> otherUsers = new ArrayList<>();
	ArrayList<ChargedData.User> users = new ArrayList();
	ListView otherUsersList;
	Button finishButton;
	String[] qrResult;
	TextView headline;
	ChargedData chargedData;

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
		//QR-Code Syntax: Veranstaltungstyp~ID~Name~Straße+Hausnr.~PLZ~Ort~Details

		ChargedData.Events event = new ChargedData.Events();
		event.qrToEvent(qrResult);

		SparseBooleanArray checked = otherUsersList.getCheckedItemPositions();
		int countChecked = 0;
		for(int j = 0; j < otherUsersList.getCount(); j++) {
			if(checked.get(j)) {
				countChecked++;
				if(countChecked > 1) {
					if(getIntent().getBooleanExtra("joinUserToEvent", false)) {
						//TODO: User hinzufügen, wenn Event bereist existiert
					} else {
						String name = ((CheckedTextView)otherUsersList.getChildAt(j)).getText().toString();
						System.out.println(name);
						ChargedData.User user = chargedData.getUserByFullName(name);
						if(user == null) System.out.println("User ist Null");
						System.out.println(user);
						event.invitetUsers.add(user);
					}
				}
			}
		}
		chargedData.addEvent(event);
		SharedPreferences accountDetails = getSharedPreferences("accountDetails", MODE_PRIVATE);
		SharedPreferences.Editor editor = accountDetails.edit();
		Gson gson = new Gson();
		String json = gson.toJson(chargedData);
		editor.putString("savedData", json);
		editor.apply();

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

		String json = accountDetails.getString("savedData", null);
		Gson gson = new Gson();
		Type type = new TypeToken<ChargedData>() {}.getType();
		System.out.println(json);
		chargedData = gson.fromJson(json, type);

		otherUsers.clear();
		for(int i = 0; i < chargedData.getOtherUsers().size(); i++) {
			otherUsers.add(chargedData.getOtherUsers().get(i).forename + " " + chargedData.getOtherUsers().get(i).name);
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
		startActivityForResult(intentAddPerson, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			if(resultCode == RESULT_OK) {
				Gson gson = new Gson();
				users.add(new Gson().fromJson(data.getStringExtra("newPerson"), new TypeToken<ChargedData.User>() {}.getType()));
			}
		}
	}
}