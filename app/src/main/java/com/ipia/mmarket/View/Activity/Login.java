package com.ipia.mmarket.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.ipia.mmarket.R;

public class Login extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button loginButton;
    TextView textView;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        fixStatusBar();
        loginCoreFunctions();
    }

    private void fixStatusBar(){
        //koristi boje za light temu iako koristim dark globalno, ne znam razlog
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.materialPlavaFix));
        window.setNavigationBarColor(getResources().getColor(R.color.black));
    }

    private void findViews() {
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginRegister);
    }


    private void loginCoreFunctions() {
        findViews();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(editTextEmail.getText());
                String password = String.valueOf(editTextPassword.getText());
                AuthUtils.authenticateUser(mAuth, "User", email, password, progressBar, Login.this, true);
            }
        });
    }
}
