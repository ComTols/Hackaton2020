package com.example.hackaton2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;



public class HomeScreen extends AppCompatActivity {

    //Instanzierung der UI-Elemente
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private LinearLayout linearLayout;
    private TextView textViewRestaurant;
    private TextView textViewDateTime;

    public int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Deklarierung der UI-Elemente
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        //Toolbar wird als Actionbar der Activity gesetzt !!!WICHTIG!!! --> Dark Mode: Burger muss noch white_medium_emphasis sein
        setSupportActionBar(toolbar);
        //Titel der Actionbar
        getSupportActionBar().setTitle("Hackathon");

        //Öffnen/Schließen der Sidebar wird Instanziert und Deklariert
        toggle=new ActionBarDrawerToggle(this,drawerLayout, R.string.home_screen_open,R.string.home_screen_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.infektionMelden:
                        intent = new Intent(HomeScreen.this, RegisterInfection.class);
                        startActivity(intent);
                        //Toast.makeText(HomeScreen.this, "item1", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.veranstaltungRegistrieren:
                        intent = new Intent(HomeScreen.this, GenerateQrCode.class);
                        startActivity(intent);
                        //Toast.makeText(HomeScreen.this, "item3", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return true;
            }
        });

        //functionality for qrCode-Buttton
        FloatingActionButton qrcodeButton = findViewById(R.id.qrCodeButton);
        qrcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQrCodeButtonClick();
            }
        });

        linearLayout = findViewById(R.id.linearLayoutMain);
        fillRestaurantCards();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onQrCodeButtonClick()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void fillRestaurantCards()
    {
        SharedPreferences accountDetails = getSharedPreferences("accountDetails", MODE_PRIVATE);
        String json = accountDetails.getString("savedData", null);
        Gson gson = new Gson();
        Type type = new TypeToken<ChargedData>() {}.getType();
        //System.out.println(json);
        ChargedData chargedData = gson.fromJson(json, type);
        ArrayList<ChargedData.Events> events = chargedData.getEvents();
        System.out.println(events);

        if(events.size() > 0)
        {
            for(int i = events.size()-1; i >= 0; i--) {
                drawCardsonHome(events.get(i).name, events.get(i).startTime, i);
            }

        }else{
            TextView textView = new TextView(this);
            textView.setText("Keine Veranstaltungen in den letzten 14 Tagen besucht.");
            linearLayout.addView(textView);
        }
    }

    public void drawCardsonHome(String restaurant, String datumUhrzeit, int id_local)
    {
        View cardView = getLayoutInflater().inflate(R.layout.add_home_screen_card, null, false);

        textViewRestaurant = cardView.findViewById(R.id.textViewRestaurant);
        textViewRestaurant.setText(restaurant);

        textViewDateTime = cardView.findViewById(R.id.textViewDateTime);
        textViewDateTime.setText(datumUhrzeit);

        linearLayout.addView(cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHomeScreenCardClick(id_local);
            }
        });
    }

    private void onHomeScreenCardClick(int id_local)
    {
        Intent intent = new Intent(this, restaurant_info.class);
        intent.putExtra("id_local",id_local);
        startActivity(intent);
    }
}