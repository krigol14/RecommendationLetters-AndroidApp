package com.example.recommendationletters;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Students extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    StudentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        // change the action bar's color
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#284b63"));
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Students");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // class provided by FirebaseUI in order to make a query in the database and fetch wanted data
        FirebaseRecyclerOptions<StudentData> options = new FirebaseRecyclerOptions.Builder<StudentData>().setQuery(reference, StudentData.class).build();
        adapter = new StudentsAdapter(options);     // connect object of adapter class to the adapter class itself
        recyclerView.setAdapter(adapter);           // connect adapter class with the recycler view
    }

    // function to tell the app to start getting data from the database when the activity starts
    @Override protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    // function to tell the app to stop getting data from the database when the activity stops
    @Override protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
