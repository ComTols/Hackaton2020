package com.example.hackaton2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class QrCodeShow extends AppCompatActivity {

    Bitmap bitmap;
    ImageView qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_show);

        //byteArray empfangen und in bitmap dekodieren
        Intent intent = getIntent();
        byte[] byteArray = intent.getByteArrayExtra("QR-Code-Bitmap");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        qrImage = findViewById(R.id.qrCodeShow);
        qrImage.setImageBitmap(bitmap.createScaledBitmap(bitmap, 1000, 1000, false));
    }
}