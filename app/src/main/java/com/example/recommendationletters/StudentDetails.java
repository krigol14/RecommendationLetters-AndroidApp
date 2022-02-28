package com.example.recommendationletters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StudentDetails extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    TextView d_name, d_reg, d_avg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        // change the action bar's color
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#284b63"));
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Students");

        d_name = findViewById(R.id.full_name);
        d_reg = findViewById(R.id.reg_number);
        d_avg = findViewById(R.id.average);

        // get the data of the student the user pressed, passed from the StudentsAdapter class
        Intent details = getIntent();
        String name = details.getStringExtra("full_name");
        String registration_nr = details.getStringExtra("number");

        // display the student's details
        d_name.setText(name);
        d_reg.setText(registration_nr);

        int[] lessons_textViews = {R.id.lesson1, R.id.lesson2, R.id.lesson3, R.id.lesson4, R.id.lesson5, R.id.lesson6, R.id.lesson7, R.id.lesson8, R.id.lesson9, R.id.lesson10};
        int[] grades_textViews = {R.id.grade1, R.id.grade2, R.id.grade3, R.id.grade4, R.id.grade5, R.id.grade6, R.id.grade7, R.id.grade8, R.id.grade9, R.id.grade10};
        List<String> lesson_names = new ArrayList<>();
        List<Integer> lesson_grades = new ArrayList<>();

        // retrieve the student's lessons along with his grades for each one
        reference.child(registration_nr).child("lessons").addValueEventListener(new ValueEventListener() {
            int grades_total;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()){
                    // retrieve lesson names
                    String lesson_name = String.valueOf(dsp.getKey());
                    lesson_names.add(lesson_name);
                }

                // for each lesson find its grade
                for (int i = 0; i < 10; i++){
                    // set each lesson to its corresponding textView
                    ((TextView) findViewById(lessons_textViews[i])).setText(lesson_names.get(i));

                    int j = i;
                    reference.child(registration_nr).child("lessons").child(lesson_names.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int lesson_grade = snapshot.getValue(Integer.class);

                            // calculate student's average and set it to its textView
                            lesson_grades.add(lesson_grade);
                            grades_total += lesson_grades.get(j);
                            d_avg.setText(String.valueOf((float)grades_total / 10));

                            // set lesson grade to its corresponding textview
                            ((TextView) findViewById(grades_textViews[j])).setText(String.valueOf(lesson_grade));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void showPrototypes(View view) {
        // redirect professor to the activity which displays some recommendation letter prototypes
        startActivity(new Intent(StudentDetails.this, RecommendationLetterPrototypes.class));
    }
}
