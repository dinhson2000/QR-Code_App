package com.example.qr_code_generatorscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ScanFromLibraActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    ImageView imageView;
    TextInputEditText editText;
    Button btnGoToLink;
    DataSharePreferences dataSharePreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_from_libra);
        dataSharePreferences = new DataSharePreferences(this);
        imageView = findViewById(R.id.SelectImg);
        editText = findViewById(R.id.outData);
        btnGoToLink = findViewById(R.id.btnGotoLink);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        btnGoToLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = editText.getText().toString();
                if(data.length() == 0) {
                    Toast.makeText(ScanFromLibraActivity.this, "No infomation in text box", Toast.LENGTH_SHORT).show();
                } else{
                    String check = checkLink(data);
                    if(check.length() > 0){
                        try {
                            Intent browser = new Intent( Intent.ACTION_VIEW , Uri.parse( data ) );
                            startActivity( browser );
                        }
                        catch (Exception e){
                            Toast.makeText(ScanFromLibraActivity.this, "Cancel open the link", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ScanFromLibraActivity.this, "This text is not link", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    public String checkLink(String data){
        String http = "http";
        int check = data.indexOf(http);
        if(check == -1){
            return "";
        }
        int end = data.length();
        for(int i = check; i< data.length(); i++){
            if(data.charAt(i) == ' ') end = i;
        }
        return data.substring(check, end);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            editText.setText("");
            imageView.setImageURI(data.getData());
            if(data == null || data.getData()==null) {
                Toast.makeText(this, "False!! This image has nothing", Toast.LENGTH_SHORT).show();
                return;
            }
            try
            {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap == null)
                {
                    Toast.makeText(this, "False to covert image to bitmap", Toast.LENGTH_SHORT).show();
                    return;
                }
                int width = bitmap.getWidth(), height = bitmap.getHeight();
                int[] pixels = new int[width * height];
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                bitmap.recycle();
                RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
                MultiFormatReader reader = new MultiFormatReader();
                try
                {
                    Result result = reader.decode(bBitmap);
                    editText.setText(result.getText());
                    dataSharePreferences.addData("LIB " + result.getText());
                }
                catch (NotFoundException e)
                {
                    Toast.makeText(this, "False to read bitmap of QR image", Toast.LENGTH_SHORT).show();
                }
            }
            catch (FileNotFoundException e)
            {
                Log.e("TAG", "Can not open file" + data.getData().toString(), e);
            }
            //TODO: action
        }
    }
}