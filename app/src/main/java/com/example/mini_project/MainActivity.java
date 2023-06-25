package com.example.mini_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        Button logoutButton = findViewById(R.id.loginButton);

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null) {
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
            MainActivity.this.finish();
        }
    }
}