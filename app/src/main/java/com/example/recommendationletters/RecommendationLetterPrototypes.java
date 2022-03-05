package com.example.recommendationletters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecommendationLetterPrototypes extends AppCompatActivity {
    DatabaseReference database;
    Button choose;
    Spinner spinner;
    PDFView pdfView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_letter_prototypes);
        choose = findViewById(R.id.choose);
        spinner = findViewById(R.id.spinner);
        pdfView = findViewById(R.id.preview);

        // change the action bar's color
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#284b63"));
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);

        Intent prototypes = getIntent();
        String registration_nr = prototypes.getStringExtra("number");
        String name = prototypes.getStringExtra("name");
        new RetrievePdfStream().execute("https://firebasestorage.googleapis.com/v0/b/recommendation-letters-app.appspot.com/o/recLetter6_8.pdf?alt=media&token=6d546c2c-9d17-43b8-aef0-1f506eb4c5a0");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i) {
                    case 0:
                        new RetrievePdfStream().execute("https://firebasestorage.googleapis.com/v0/b/recommendation-letters-app.appspot.com/o/recLetter6_8.pdf?alt=media&token=6d546c2c-9d17-43b8-aef0-1f506eb4c5a0");
                        break;
                    case 1:
                        new RetrievePdfStream().execute("https://firebasestorage.googleapis.com/v0/b/recommendation-letters-app.appspot.com/o/recLetter8_9.pdf?alt=media&token=8ec98b37-68b4-46e0-ac11-2cafaa5d35d3");
                        break;
                    case 2:
                        new RetrievePdfStream().execute("https://firebasestorage.googleapis.com/v0/b/recommendation-letters-app.appspot.com/o/recLetter9_10.pdf?alt=media&token=58ddd52a-c07e-466c-ab83-7b16c9c7d853");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selected_option = spinner.getSelectedItem().toString();

                switch(selected_option){
                    case "grades 6-8":
                        getUrl("rec6_8", registration_nr, name);
                        break;
                    case "grades 8-9":
                        getUrl("rec8_9", registration_nr, name);
                        break;
                    case "grades 9-10":
                        getUrl("rec9_10", registration_nr, name);
                        break;
                }
            }
        });
    }

    public void getUrl(String childName, String registration_nr, String name) {
        // instantiate firebase reference for the specific child
        database = FirebaseDatabase.getInstance().getReference().child("RecommendationLetters").child(childName);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // snapshot.getValue(String.class) --> the url of the pdf saved in realtime database
                String pdf_url = snapshot.getValue(String.class);
                alertDialog(pdf_url, registration_nr, name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecommendationLetterPrototypes.this, "Error loading PDF!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // function which builds an alert dialog with the options the professor has after choosing the specific pdf
    public void alertDialog(String pdf_url, String registration_nr, String name) {
        // build alert dialog
        CharSequence options[] = new CharSequence[]{
                "Download",
                "View",
                "Cancel"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(RecommendationLetterPrototypes.this);
        builder.setTitle("Choose what you want to do: ");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // download the pdf
                if (which == 0) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
                    startActivity(intent);
                }
                // view the pdf
                if (which == 1) {
                    Intent intent = new Intent(RecommendationLetterPrototypes.this, ViewPdf.class);
                    intent.putExtra("url", pdf_url);
                    intent.putExtra("number", registration_nr);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
            }
        });
        builder.show();
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
            // dialog.dismiss();
        }
    }
}
