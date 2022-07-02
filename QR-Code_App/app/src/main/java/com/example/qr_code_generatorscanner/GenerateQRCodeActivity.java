package com.example.qr_code_generatorscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.graphics.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.Bundle;
import java.io.File;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.Set;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Environment;
import android.content.ContentValues;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GenerateQRCodeActivity extends AppCompatActivity {

    private TextView qrCodeTextView;
    private ImageView qrCodeImageView;
    private TextInputEditText qrCodeTextInputEditText;
    private Button qrCodeGeneratorButton, saveImageQRcode;
    private boolean haveImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);
        qrCodeTextView = findViewById(R.id.frameText);
        qrCodeImageView = findViewById(R.id.QRCodeImg);
        qrCodeTextInputEditText = findViewById(R.id.inputData);
        qrCodeGeneratorButton = findViewById(R.id.QRCodeGeneratorBtn);
        saveImageQRcode = findViewById(R.id.SaveImagetoLibBtn);
        DataSharePreferences dataSharePreferences = new DataSharePreferences(this);
        ActivityCompat.requestPermissions(GenerateQRCodeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(GenerateQRCodeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        saveImageQRcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveImage == true){
                    Bitmap bitmap = ((BitmapDrawable)qrCodeImageView.getDrawable()).getBitmap();
                    FileOutputStream outputStream  = null;
                    File file = Environment.getExternalStorageDirectory();
                    File dir = new File(file.getAbsolutePath() + "/MyPics");
                    dir.mkdir();

                    String filname = String.format("%d.png", System.currentTimeMillis());
                    File outFile = new File(dir, filname);
                    try {
                        outputStream = new FileOutputStream(outFile);
                    } catch (Exception e){
                        Toast.makeText(GenerateQRCodeActivity.this, "Can't create file", Toast.LENGTH_SHORT).show();
                    }
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    try {
                        outputStream.flush();
                    }catch (Exception e){
                        Toast.makeText(GenerateQRCodeActivity.this, "Can't create file", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        outputStream.close();
                        Toast.makeText(GenerateQRCodeActivity.this, "Image is saved", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(GenerateQRCodeActivity.this, "Can't create file", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(GenerateQRCodeActivity.this, "Please generate QRcode image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        qrCodeGeneratorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = qrCodeTextInputEditText.getText().toString().trim();
                if(data.isEmpty()){
                    Toast.makeText(GenerateQRCodeActivity.this, "Please Enter Some Data to Generate QR Code", Toast.LENGTH_SHORT).show();
                }else{

                    MultiFormatWriter writer = new MultiFormatWriter();

                    try {
                        BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, 250, 250);
                        BarcodeEncoder encoder = new BarcodeEncoder();
                        Bitmap bitmap = encoder.createBitmap(matrix);
                        qrCodeImageView.setImageBitmap(bitmap);
                        InputMethodManager manager1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager1.hideSoftInputFromWindow(qrCodeTextInputEditText.getApplicationWindowToken(),0);
                        qrCodeTextView.setVisibility(View.GONE);
                        haveImage = true;
                        dataSharePreferences.addData("GEN " + data );
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }
}