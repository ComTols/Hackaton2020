package com.example.hackaton2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class AddPerson extends AppCompatActivity {

	private CheckBox letterCheck;
	private CheckBox mailCheck;

	private ConstraintLayout letterContainer;
	private ConstraintLayout mailContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_person);

		Button cancel = findViewById(R.id.addPerson_cancelBtn);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickCancelButton();
			}
		});

		Button apply = findViewById(R.id.addPerson_applyBtn);
		apply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickApplyButton();
			}
		});

		letterCheck = findViewById(R.id.addPerson_messageOption_letter);
		mailCheck = findViewById(R.id.addPerson_messageOption_mail);
		letterContainer = findViewById(R.id.addPerson_messageOption_letter_details);
		mailContainer = findViewById(R.id.addPerson_messageOption_mail_details);

		letterCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (letterContainer.getVisibility() == View.GONE) {
					letterContainer.setVisibility(View.VISIBLE);
				} else {
					letterContainer.setVisibility(View.GONE);
				}
			}
		});

		mailCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (mailContainer.getVisibility() == View.GONE) {
					mailContainer.setVisibility(View.VISIBLE);
				} else {
					mailContainer.setVisibility(View.GONE);
				}
			}
		});

	}

	private void onClickApplyButton() {

		if (!isFormValid()) {
			return;
		}

		EditText forename = findViewById(R.id.addPerson_forename);
		EditText lastname = findViewById(R.id.addPerson_lastname);

		SharedPreferences accountDetails = getSharedPreferences("accountDetails", MODE_PRIVATE);
		SharedPreferences.Editor edit = accountDetails.edit();
		int i = 0;
		while (accountDetails.contains("otherUser[" + i + "]")) {
			i++;
		}
		edit.putBoolean("otherUser[" + i + "]", true);
		edit.putString("otherUser[" + i + "]_forename", forename.getText().toString());
		edit.putString("otherUser[" + i + "]_lastname", lastname.getText().toString());
		if (letterCheck.isChecked()) {
			edit.putBoolean("otherUser[" + i + "]_informByLetter", true);
			edit.putString("otherUser[" + i + "]_street", ((EditText) findViewById(R.id.addPerson_messageOption_letter_details_street)).getText().toString());
			edit.putString("otherUser[" + i + "]_postcode", ((EditText) findViewById(R.id.addPerson_messageOption_letter_details_postcode)).getText().toString());
			edit.putString("otherUser[" + i + "]_city", ((EditText) findViewById(R.id.addPerson_messageOption_letter_details_city)).getText().toString());

		}
		if (mailCheck.isChecked()) {
			edit.putBoolean("otherUser[" + i + "]_informByMail", true);
			edit.putString("otherUser[" + i + "]_mail", ((EditText) findViewById(R.id.addPerson_messageOption_mail_details_mailAdress)).getText().toString());
		}

		edit.apply();
		finish();
	}

	private boolean isFormValid() {
		CheckBox letterCheck = findViewById(R.id.addPerson_messageOption_letter);
		CheckBox mailCheck = findViewById(R.id.addPerson_messageOption_mail);

		EditText forename = findViewById(R.id.addPerson_forename);
		EditText lastname = findViewById(R.id.addPerson_lastname);
		EditText street = findViewById(R.id.addPerson_messageOption_letter_details_street);
		EditText postcode = findViewById(R.id.addPerson_messageOption_letter_details_postcode);
		EditText city = findViewById(R.id.addPerson_messageOption_letter_details_city);
		EditText mail = findViewById(R.id.addPerson_messageOption_mail_details_mailAdress);

		TextView forenameError = findViewById(R.id.addPerson_txtforename_error);
		TextView lastnameError = findViewById(R.id.addPerson_txtlastname_error);
		TextView letterError = findViewById(R.id.addPerson_messageOption_letter_details_error);
		TextView mailError = findViewById(R.id.addPerson_messageOption_mail_details_error);
		TextView applyError = findViewById(R.id.addPerson_applyBtn_error);

		forenameError.setVisibility(View.GONE);
		lastnameError.setVisibility(View.GONE);
		letterError.setVisibility(View.GONE);
		mailError.setVisibility(View.GONE);
		applyError.setVisibility(View.GONE);

		boolean isValid = true;

		if (forename.getText().toString().matches("")) {
			isValid = false;
			forenameError.setVisibility(View.VISIBLE);
		}
		if (lastname.getText().toString().matches("")) {
			isValid = false;
			lastnameError.setVisibility(View.VISIBLE);
		}

		String errMas = "";
		if (letterCheck.isChecked()) {
			if (street.getText().toString().matches("")) {
				isValid = false;
				errMas += "Eine Straße ist erforderlich!\n";
			}
			if (postcode.getText().toString().matches("")) {
				isValid = false;
				errMas += "Eine Postleitzahl ist erforderlich!\n";
			}
			if (city.getText().toString().matches("")) {
				isValid = false;
				errMas += "Ein Ort ist erforderlich!";
			}
			if (!isValid) {
				letterError.setText(errMas.trim());
				letterError.setVisibility(View.VISIBLE);
			}
		}
		if (mailCheck.isChecked()) {
			if (mail.getText().toString().matches("") && !mail.getText().toString().contains("@")) {
				isValid = false;
				mailError.setVisibility(View.VISIBLE);
			}
		}

		if(!(mailCheck.isChecked() || letterCheck.isChecked())) {
			isValid = false;
			applyError.setVisibility(View.VISIBLE);
		}
		return isValid;
	}

	protected void onClickCancelButton() {
		finish();
	}
}