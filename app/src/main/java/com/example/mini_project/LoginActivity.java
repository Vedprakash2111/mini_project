package com.example.mini_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private TextView errorTextView;
    private Button loginButton,registerButton;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        errorTextView = findViewById(R.id.errorTextView);
        loginButton = findViewById(R.id.loginButton);
        registerButton=findViewById(R.id.registerButton);
        pd = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    errorTextView.setText("Please enter email and password.");
                } else {
                    pd.setMessage("Login your account...");
                    pd.show();
                    signIn(email,password);
                }
            }
        });
    }
    private void signIn(String email ,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login successful
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),"login Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // Login failed
                            pd.dismiss();
                            errorTextView.setText("Login failed. Please check your credentials.");
                        }
                    }
                });
    }
}
