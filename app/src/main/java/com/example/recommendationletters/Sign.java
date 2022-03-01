package com.example.recommendationletters;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Sign extends AppCompatActivity {
    Button save_signature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        save_signature = findViewById(R.id.save_signature);

        RelativeLayout parent = (RelativeLayout) findViewById(R.id.signImageParent);
        MyDrawView myDrawView = new MyDrawView(this);
        parent.addView(myDrawView);

        save_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.setDrawingCacheEnabled(true);
                Bitmap bmp = parent.getDrawingCache();

                // save the signature in png format in Device File Explorer data/data/com.example.recommendationLetters/files/signature.png
                try {
                    FileOutputStream fos = openFileOutput("signature.png", Context.MODE_PRIVATE);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100 , bos);
                    byte[] bitmapdata = bos.toByteArray();
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}