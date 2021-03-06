package com.example.recommendationletters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.io.ByteArrayOutputStream;

public class StudentsAdapter extends FirebaseRecyclerAdapter<StudentData, StudentsAdapter.studentsViewHolder> {

    public StudentsAdapter(@NonNull FirebaseRecyclerOptions<StudentData> options) {
        super(options);
    }

    static class studentsViewHolder extends RecyclerView.ViewHolder {
        TextView name, number, average;

        // constructor
        public studentsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.full_name);
            number = itemView.findViewById(R.id.reg_number);
            average = itemView.findViewById(R.id.average);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent details = new Intent(view.getContext(), StudentDetails.class);

                    // pass the data of the specific student clicked to the new StudentDetails activity
                    details.putExtra("full_name", name.getText());
                    details.putExtra("number", number.getText());
                    details.putExtra("average", average.getText());

                    view.getContext().startActivity(details);
                }
            });
        }
    }

    // function to bind the view in layout/student.xml with the data in StudentData.java file
    @Override
    protected void onBindViewHolder(@NonNull studentsViewHolder holder, int position, @NonNull StudentData model) {
        holder.name.setText(model.getFull_name());
        holder.number.setText(model.getRegistration_nr());
        // invisible field - needed in the student details activity
        holder.average.setText(String.valueOf(model.getTotal_average()));
    }

    // function to tell the class about the layout/student.xml file, in which the data will be shown
    @NonNull
    @Override
    public studentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student, parent, false);
        return new studentsViewHolder(view);
    }
}
