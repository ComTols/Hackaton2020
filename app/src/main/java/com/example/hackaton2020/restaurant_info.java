package com.example.hackaton2020;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class restaurant_info extends AppCompatActivity {

    public int id;
    private Toolbar toolbar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

        toolbar2 = findViewById(R.id.toolbar_restaurant_info);
        setSupportActionBar(toolbar2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");


        Intent intent = getIntent();
        id = intent.getIntExtra("id_local", 999999);

        if (id != 999999) {
            fillRestaurantInfo();
        } else {
            Toast.makeText(this, "Fehler", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "App bitte neu starten", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void fillRestaurantInfo() {
        SharedPreferences accountDetails = getSharedPreferences("accountDetails", MODE_PRIVATE);
        String json = accountDetails.getString("savedData", null);
        Gson gson = new Gson();
        Type type = new TypeToken<ChargedData>() {}.getType();
        //System.out.println(json);
        ChargedData chargedData = gson.fromJson(json, type);
        ArrayList<ChargedData.Events> events = chargedData.getEvents();

        TextView restaurantName = findViewById(R.id.RestaurantName);
        TextView adresse1 = findViewById(R.id.StrasseHaunummer);
        TextView adresse2 = findViewById(R.id.PostleitzahlOrt);
        TextView anfang = findViewById(R.id.AnfangBesuchszeit);
        TextView ende = findViewById(R.id.EndeBesuchszeit);

        ArrayList<ChargedData.User> invitedUsers = events.get(id).invitetUsers;

        restaurantName.setText(events.get(id).name);
        adresse1.setText(events.get(id).street);
        adresse2.setText(events.get(id).plz + " " + events.get(id).city);
        anfang.setText(events.get(id).startTime);
        ende.setText(events.get(id).endTime);

        LinearLayout linearLayout = findViewById(R.id.linearLayoutRight);

        for(int i=0; i< invitedUsers.size(); i++)
        {
            TextView textView = new TextView(this);
            textView.setText(invitedUsers.get(i).forename + " " + invitedUsers.get(i).name);
            linearLayout.addView(textView);
        }
    }
}