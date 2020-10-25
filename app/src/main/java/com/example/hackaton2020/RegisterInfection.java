package com.example.hackaton2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RegisterInfection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_infection);

        FloatingActionButton qrcodeButton = findViewById(R.id.qrCodeButton);
        qrcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActionButtonClick();
            }
        });
    }

    private void onActionButtonClick()
    {
        navigateUpTo(new Intent(getBaseContext(), HomeScreen.class));
        finish();
    }
}