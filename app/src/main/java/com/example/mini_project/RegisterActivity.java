package com.example.mini_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, firstNameEditText, lastNameEditText, confirmPasswordEditText;
    private TextView errorTextView;
    private FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        errorTextView = findViewById(R.id.errorTextView);
        Button registerButton = findViewById(R.id.registerButton);
        Button logoutButton = findViewById(R.id.loginTextButton);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        pd = new ProgressDialog(this);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
                    errorTextView.setText("Please enter email, password, first name, and last name.");
                } else if (!password.equals(confirmPassword)) {
                    errorTextView.setText("Passwords do not match.");
                } else {
                    pd.setMessage("Signing up...");
                    pd.show();
                    register(firstName, lastName, email, password);
                }
            }
        });
    }

    private void register(String firstName, String lastName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("id",mAuth.getCurrentUser().getUid());
                hashMap.put("firstName",firstName);
                hashMap.put("lastName",lastName);
                hashMap.put("email",email);
                hashMap.put("password",password);
                hashMap.put("bio","");
                hashMap.put("image_url","https://firebasestorage.googleapis.com/v0/b/learning-app-41786.appspot.com/o/profile_pic%2Fuser_img.png?alt=media&token=2c8e3048-1598-42d1-a202-8ef09a1cf291");
                reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("firstName").child(firstName);
                    HashMap<String,String> hashMap1 = new HashMap<>();
                    hashMap1.put("uid",mAuth.getCurrentUser().getUid());
                    reference1.setValue(hashMap1).addOnCompleteListener(task2 -> {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(main);
                        RegisterActivity.this.finish();
                    });
                });
            }
        });
    }
}
