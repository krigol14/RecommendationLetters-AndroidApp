package com.example.recommendationletters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class StudentsAdapter extends FirebaseRecyclerAdapter<StudentData, StudentsAdapter.studentsViewHolder> {

    public StudentsAdapter(@NonNull FirebaseRecyclerOptions<StudentData> options) {
        super(options);
    }

    static class studentsViewHolder extends RecyclerView.ViewHolder {
        TextView name, number;

        // constructor
        public studentsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.full_name);
            number = itemView.findViewById(R.id.reg_number);
        }
    }

    // function to bind the view in layout/student.xml with the data in StudentData.java file
    @Override
    protected void onBindViewHolder(@NonNull studentsViewHolder holder, int position, @NonNull StudentData model) {
        holder.name.setText(model.getFull_name());
        holder.number.setText(model.getRegistration_nr());
    }

    // function to tell the class about the layout/student.xml file, in which the data will be shown
    @NonNull
    @Override
    public studentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student, parent, false);
        return new studentsViewHolder(view);
    }
}
