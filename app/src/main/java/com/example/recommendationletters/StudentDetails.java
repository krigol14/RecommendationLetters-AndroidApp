package com.example.recommendationletters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentDetails extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, reference2;
    TextView d_name, d_reg, d_avg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Cart");

        d_name = findViewById(R.id.full_name);
        d_reg = findViewById(R.id.reg_number);
        d_avg = findViewById(R.id.average);

        // get the data of the student the user pressed, passed from the StudentsAdapter class
        Intent details = getIntent();
        String name = details.getStringExtra("full_name");
        String registration_nr = details.getStringExtra("number");
        String average = details.getStringExtra("average");

        d_name.setText(name);
        d_reg.setText(registration_nr);
        d_avg.setText(average);
    }
}
