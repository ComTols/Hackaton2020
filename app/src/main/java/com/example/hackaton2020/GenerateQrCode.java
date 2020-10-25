package com.example.hackaton2020;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.WriterException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class GenerateQrCode extends AppCompatActivity {

    Bitmap bitmap;
    ImageView qrImage;
    Spinner spinner;
    long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        finish();
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    }
                })
                .check();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        //UI -> SpinBox Contents
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Gastronomie");
        spinnerArray.add("Freizeitveranstaltung");
        spinnerArray.add("Privatveranstaltung");

        spinner = findViewById(R.id.spinnerBar);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        spinner.setAdapter(spinnerArrayAdapter);

        Button generate = findViewById(R.id.buttonGenerate);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQrCode();
            }
        });


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

    private void generateQrCode(){
        String inputValue;
        //currentTime
        currentTime = Calendar.getInstance().getTimeInMillis();

        TextView errorText = findViewById(R.id.errorTextView);

        Spinner eventType = findViewById(R.id.spinnerBar);
        String type = eventType.getSelectedItem().toString();
        EditText eventName = findViewById(R.id.editTextEventName);
        EditText street = findViewById(R.id.editTextStrasse);
        EditText plz = findViewById(R.id.editTextPlz);
        EditText city = findViewById(R.id.editTextOrt);
        EditText details = findViewById(R.id.editTextDetails);


        if(!eventName.getText().toString().matches("") && !street.getText().toString().matches("") && !plz.getText().toString().matches("") && !city.getText().toString().matches("") && !details.getText().toString().matches("")) {
            errorText.setVisibility(View.INVISIBLE);

            inputValue = type.toString() + "~" + currentTime + "~" + eventName.getText().toString() + "~" + street.getText().toString() + "~" + plz.getText().toString() + "~" + city.getText().toString() + "~" + details.getText().toString();
            System.out.println(inputValue);

            //QR-Code
            QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, 1);
            //create QR-Code and encode into bitmap format
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
            } catch (WriterException e)
            {
                Log.v("QR-Code-Generierung", e.toString());
            }

            //save QR-Code to gallery
            String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
            try {
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap.createScaledBitmap(bitmap, 1000, 1000, false), eventName.getText().toString(), "Test");
                Toast.makeText(GenerateQrCode.this, "QR-Code in Gallerie gespeichert", Toast.LENGTH_SHORT).show();
                //QRGSaver.save(savePath, eventName.getText().toString(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Intent intent = new Intent(this, QrCodeShow.class);
            intent.putExtra("QR-Code-Bitmap", byteArray);
            startActivity(intent);
            finish();

        }else
        {
            errorText.setText("Bitte alle Felder ausfüllen!");
            errorText.setVisibility(View.VISIBLE);
        }

        //TODO: Eindeutige ID aus Datetime und Name erstellen








    }
}