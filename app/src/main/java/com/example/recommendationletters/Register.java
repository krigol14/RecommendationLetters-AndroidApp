package com.example.recommendationletters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText register_email, register_password, confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        confirm_password = findViewById(R.id.confirm_password);
    }

    public void signUp(View view)
    {
        String passwd = register_password.getText().toString();
        String confpasswd = confirm_password.getText().toString();

        mAuth.createUserWithEmailAndPassword(register_email.getText().toString(), register_password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && (passwd.equals(confpasswd))){
                            Toast.makeText(getApplicationContext(), "REGISTERED SUCCESSFULLY", Toast.LENGTH_LONG).show();

                            // redirect user to the login page in order to log in with his newly created credentials
                            Intent intent = new Intent(Register.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "REGISTRATION ERROR", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
