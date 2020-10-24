package com.example.hackaton2020;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog extends AppCompatDialogFragment {
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
