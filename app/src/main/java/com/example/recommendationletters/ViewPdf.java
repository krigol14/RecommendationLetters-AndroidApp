package com.example.recommendationletters;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewPdf extends AppCompatActivity {
    String urls;
    PDFView pdfView;
    ProgressDialog dialog;
    Button sign, grant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);
        pdfView = findViewById(R.id.abc);

        sign = findViewById(R.id.sign);
        grant = findViewById(R.id.grant);

        // show progress while loading the pdf file
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        // get the url of the pdf
        urls = getIntent().getStringExtra("url");
        new RetrivePdfStream().execute(urls);

        // when professor presses the sign button , redirect him to another activity
        // where he can sign and his signature will be saved as a png to be later added on the pdf
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewPdf.this, Sign.class));
            }
        });
    }

    // retrieve the pdf using its url
    class RetrivePdfStream extends AsyncTask<String, Void, InputStream> {
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
