package com.example.hackaton2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {

	//Setzte Checkboxen als Felder
	private CheckBox letter;
	private CheckBox mail;
	private CheckBox push;

	//Setzte Formulare als Felder
	private ConstraintLayout letterDescription;
	private ConstraintLayout mailDescription;

	//Setzte Weiter-Button als Feld
	private Button cont;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_create_account);
		super.onCreate(savedInstanceState);

		//Fülle Felder
		letter = findViewById(R.id.login_messageOption_letter);
		mail = findViewById(R.id.login_messageOption_mail);
		push = findViewById(R.id.login_messageOption_push);
		letterDescription = findViewById(R.id.login_messageOption_letter_detailContainer);
		mailDescription = findViewById(R.id.login_messageOption_mail_detailContainer);
		cont = findViewById(R.id.login_continue);

		letter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				changeLetterDescriptionVisibility();
			}
		});

		mail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				changeMailDescriptionVisibility();
			}
		});

		cont.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onContinue();
			}
		});
	}

	protected void changeLetterDescriptionVisibility() {
		if (letterDescription.getVisibility() == View.GONE) {
			letterDescription.setVisibility(View.VISIBLE);
		} else {
			letterDescription.setVisibility(View.GONE);
		}
	}

	protected void changeMailDescriptionVisibility() {
		if (mailDescription.getVisibility() == View.GONE) {
			mailDescription.setVisibility(View.VISIBLE);
		} else {
			mailDescription.setVisibility(View.GONE);
		}
	}

	protected void onContinue() {
		boolean isValid = true;

		EditText forename = findViewById(R.id.login_messageOption_letter_detail_firstname);
		EditText name = findViewById(R.id.login_messageOption_letter_detail_name);
		EditText street = findViewById(R.id.login_messageOption_letter_detail_street);
		EditText plz = findViewById(R.id.login_messageOption_letter_detail_postcode);
		EditText city = findViewById(R.id.login_messageOption_letter_detail_city);

		if (letter.isChecked()) {
			String errorMes = "";

			System.out.println("Vorname " + forename.getText());

			if (forename.getText().toString().matches("")) {
				errorMes += "Der Vorname ist erforderlich\n";
			}
			if (name.getText().toString().matches("")) {
				errorMes += "Der Nachname ist erforderlich!\n";
			}
			if (street.getText().toString().matches("")) {
				errorMes += "Eine Straße und eine Hausnummer ist erforderlich!\n";
			}
			if (plz.getText().toString().matches("")) {
				errorMes += "Eine Postleitzahl ist erforderlich!\n";
			}
			if (city.getText().toString().matches("")) {
				errorMes += "Eine Ortsangabe ist erforderlich!";
			}

			TextView errorLetter = findViewById(R.id.login_messageOption_errorLetter);
			if (!(errorMes.equals(""))) {
				isValid = false;
				errorLetter.setText(errorMes.trim());
				errorLetter.setVisibility(View.VISIBLE);
			} else {
				errorLetter.setVisibility(View.GONE);
			}
		}
		EditText mailTxt = findViewById(R.id.login_messageOption_detail_mail);
		if (mail.isChecked()) {
			TextView errorMail = findViewById(R.id.login_messageOption_errorMail);

			if (mailTxt.getText().toString().matches("") || !(mailTxt.getText().toString().contains("@"))) {
				isValid = false;
				errorMail.setText("Eine E-Mail Adresse ist erforderlich!");
				errorMail.setVisibility(View.VISIBLE);
			} else {
				errorMail.setVisibility(View.GONE);
			}
		}

		if(isValid) {
			SharedPreferences accountDetails = getSharedPreferences("accountDetails", MODE_PRIVATE);
			SharedPreferences.Editor editor = accountDetails.edit();

			editor.putBoolean("initialized", true);
			editor.putBoolean("messageLetter", letter.isChecked());
			editor.putBoolean("messageMail", mail.isChecked());
			editor.putBoolean("messagePush", push.isChecked());
			editor.putBoolean("messageInApp", true);

			if(letter.isChecked()) {
				editor.putString("forename", forename.getText().toString());
				editor.putString("forename", name.getText().toString());
				editor.putString("forename", street.getText().toString());
				editor.putString("forename", plz.getText().toString());
				editor.putString("forename", city.getText().toString());
			}
			if(mail.isChecked()) {
				editor.putString("mail", mailTxt.getText().toString());
			}

			editor.apply();

			finish();
		}
	}
}
