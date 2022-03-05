package com.example.recommendationletters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewPdf extends AppCompatActivity {
    String urls, registration_nr, name;
    PDFView pdfView;
    ProgressDialog dialog;
    Button grant;
    DatabaseReference database;
    EditText date, profName, stdName;
    TextView dateText, profNameText, stdNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);
        pdfView = findViewById(R.id.abc);
        grant = findViewById(R.id.grant);
        date = findViewById(R.id.dateEdit);
        profName = findViewById(R.id.professorName);
        // stdName = findViewById(R.id.studentName);
        dateText = findViewById(R.id.date);
        stdNameText = findViewById(R.id.student_name);
        profNameText = findViewById(R.id.professor_name);

        RelativeLayout parent = (RelativeLayout) findViewById(R.id.signpdf);
        MyDrawView myDrawView = new MyDrawView(this);
        parent.addView(myDrawView);

        // change the action bar's color
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#284b63"));
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);

        // show progress while loading the pdf file
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        // get the url of the pdf
        urls = getIntent().getStringExtra("url");
        registration_nr = getIntent().getStringExtra("number");
        name = getIntent().getStringExtra("name");
        stdNameText.setText(name);
        new RetrievePdfStream().execute(urls);

        grant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set details in the pdf document
                String dateInput = date.getText().toString();
                String professor_name = profName.getText().toString();
                // String student_name = stdName.getText().toString();

                dateText.setText(dateInput);
                profNameText.setText(professor_name);
                // stdNameText.setText(student_name);

                parent.setDrawingCacheEnabled(true);
                Bitmap bmp = parent.getDrawingCache();

                // save the signature in .png format in Device File Explorer data/data/com.example.recommendationLetters/files/signature.png
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

                // push the saved pdf with its signature in firebase storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("granted_recLetters");
                Uri file = Uri.fromFile(new File("data/data/com.example.recommendationletters/files/signature.png"));
                UploadTask uploadTask = storageReference.child("recLetter" + System.currentTimeMillis() + ".png").putFile(file);

                // push the url of the pdf in realtime database
                database = FirebaseDatabase.getInstance().getReference().child("Students").child(registration_nr).child("RecLetters");
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();

                        String pdf_url  = uri.toString();
                        database.child(database.push().getKey()).setValue(pdf_url);
                    }
                });
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
