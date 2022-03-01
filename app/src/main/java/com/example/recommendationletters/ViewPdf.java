package com.example.recommendationletters;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.barteksc.pdfviewer.PDFView;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewPdf extends AppCompatActivity {
    String urls;
    PDFView pdfView;
    ProgressDialog dialog;
    Button grant, save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);
        pdfView = findViewById(R.id.abc);

        RelativeLayout parent = (RelativeLayout) findViewById(R.id.signpdf);
        MyDrawView myDrawView = new MyDrawView(this);
        parent.addView(myDrawView);

        // change the action bar's color
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#284b63"));
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);

        grant = findViewById(R.id.grant);
        save = findViewById(R.id.save_sign);

        // show progress while loading the pdf file
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        // get the url of the pdf
        urls = getIntent().getStringExtra("url");
        new RetrievePdfStream().execute(urls);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.setDrawingCacheEnabled(true);
                Bitmap bmp = parent.getDrawingCache();

                // save the signature in .png format in Device File Explorer data/data/com.example.recommendationLetters/files/signature.png
                try {
                    FileOutputStream fos = openFileOutput("idea_signature.png", Context.MODE_PRIVATE);
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

    // retrieve the pdf using its url
    class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                // adding url
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // if url connection returns response code is 200, the execution was successful
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }
            catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        // load the pdf file and dismiss the dialog box
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
            dialog.dismiss();
        }
    }
}
