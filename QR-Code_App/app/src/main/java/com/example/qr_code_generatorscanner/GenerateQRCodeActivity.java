package com.example.qr_code_generatorscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button qrCodeGeneratorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);
        qrCodeTextView = findViewById(R.id.frameText);
        qrCodeImageView = findViewById(R.id.QRCodeImg);
        qrCodeTextInputEditText = findViewById(R.id.inputData);
        qrCodeGeneratorButton = findViewById(R.id.QRCodeGeneratorBtn);

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

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

    }
}