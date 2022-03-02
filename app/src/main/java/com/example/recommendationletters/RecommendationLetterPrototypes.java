package com.example.recommendationletters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecommendationLetterPrototypes extends AppCompatActivity {
    DatabaseReference database;
    Button choose6_8, choose8_9, choose9_10;
    String pdf_url6_8, pdf_url8_9, pdf_url9_10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_letter_prototypes);
        choose6_8 = findViewById(R.id.choose6_8);
        choose8_9 = findViewById(R.id.choose8_9);
        choose9_10 = findViewById(R.id.choose9_10);

        // change the action bar's color
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#284b63"));
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);

        // grades 6-8
        choose6_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // instantiate firebase reference for the specific child
                database = FirebaseDatabase.getInstance().getReference().child("RecommendationLetters").child("rec6_8");
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // get url of the pdf the professor chose
                        pdf_url6_8 = dataSnapshot.getValue(String.class);

                        // show alert dialog regrading professor's options with the specific pdf
                        alertDialog(pdf_url6_8);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(RecommendationLetterPrototypes.this, "Error loading PDF!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // grades 8-9
        choose8_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // instantiate firebase reference for the specific child
                database = FirebaseDatabase.getInstance().getReference().child("RecommendationLetters").child("rec8_9");
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // get url of the pdf the professor chose
                        pdf_url8_9 = dataSnapshot.getValue(String.class);

                        // show alert dialog regrading professor's options with the specific pdf
                        alertDialog(pdf_url8_9);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(RecommendationLetterPrototypes.this, "Error loading PDF!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // grades 9-10
        choose9_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // instantiate firebase reference for the specific child
                database = FirebaseDatabase.getInstance().getReference().child("RecommendationLetters").child("rec9_10");
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // get url of the pdf the professor chose
                        pdf_url9_10 = dataSnapshot.getValue(String.class);

                        // show alert dialog regrading professor's options with the specific pdf
                        alertDialog(pdf_url9_10);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(RecommendationLetterPrototypes.this, "Error loading PDF!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // function which builds an alert dialog with the options the professor has after choosing the specific pdf
    public void alertDialog(String pdf_url) {
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
                    startActivity(intent);
                }
            }
        });
        builder.show();
    }
}
