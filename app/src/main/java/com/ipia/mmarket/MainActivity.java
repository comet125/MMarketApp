package com.ipia.mmarket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ipia.mmarket.View.Activity.HomeLanding;
import com.ipia.mmarket.View.Activity.Login;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        checkForSession();
    }

    private void checkForSession() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        new Handler().postDelayed(() -> {
            Intent intent;
            if (currentUser != null) {
                Toast.makeText(MainActivity.this, "Welcome back! Signing you in.", Toast.LENGTH_SHORT).show();
                intent = new Intent(getApplicationContext(), HomeLanding.class);
            } else {
                intent = new Intent(getApplicationContext(), Login.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}
