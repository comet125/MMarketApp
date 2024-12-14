package com.ipia.mmarket.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ipia.mmarket.R;

public class Register extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, editTextUser;
    Button regButton;
    Button backButton;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerCoreFunctions();
    }

    private void findViews(){
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance("https://mmarket-ipia-rma2-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users");
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextUser = findViewById(R.id.user);
        regButton = findViewById(R.id.reg_btn);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.back_btn);
    }

    private void registerCoreFunctions(){

        findViews();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(editTextEmail.getText());
                String password = String.valueOf(editTextPassword.getText());
                String username = String.valueOf(editTextUser.getText());
                if (username.isEmpty()){
                    com.ipia.mmarket.View.Activity.DisplayUtils.showFieldDialog(Register.this, "You need to provide a username");
                }else if (!username.equals("admin")){
                    AuthUtils.authenticateUser(mAuth, username, email, password, progressBar, Register.this, false);

                }else DisplayUtils.showFieldDialog(Register.this, "This username is not allowed. Please choose any other username.");

            }
        });
    }
}
