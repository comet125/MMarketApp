package com.ipia.mmarket.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ipia.mmarket.Models.User;
import com.ipia.mmarket.View.Activity.HomeLanding;
import com.ipia.mmarket.View.Fragment.DisplayUtils;

public class AuthUtils {


    public static void authenticateUser(FirebaseAuth mAuth, String username, String email, String password, final ProgressBar progressBar, final Activity activity, boolean isLogin) {
        if (TextUtils.isEmpty(email)) {
            com.ipia.mmarket.View.Activity.DisplayUtils.showFieldDialog(activity, "Email field cannot be empty.");
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            com.ipia.mmarket.View.Activity.DisplayUtils.showFieldDialog(activity, "Password field cannot be empty.");
            progressBar.setVisibility(View.GONE);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        if (isLogin) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                loginSuccess(activity);
                            } else {
                                loginFailure(activity);
                            }
                        }
                    });
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                insertToRealtime(username, email, activity);
                                com.ipia.mmarket.View.Activity.DisplayUtils.showFieldDialog(activity, "User created. You can log in now!");
                            } else {
                                registerFailure(activity);
                            }
                        }
                    });
        }
    }

    private static void insertToRealtime(String username, String email, Activity activity){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://mmarket-ipia-rma2-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        User u = new User();
        u.setName(username);
        u.setEmail(email);

        reference.child(firebaseUser.getUid()).setValue(u)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(activity, "Welcome", Toast.LENGTH_LONG).show();
                        }else Toast.makeText(activity, "Sign-up error. Some features may not work as expected. Contact support", Toast.LENGTH_LONG).show();
                    }
                });

    }
    private static void loginSuccess(Activity activity) {
        Toast.makeText(activity, "Login success!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(activity.getApplicationContext(), HomeLanding.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private static void loginFailure(Activity activity) {
        if (DisplayUtils.isNetworkConnected(activity)) {
            com.ipia.mmarket.View.Activity.DisplayUtils.showFieldDialog(activity, "Sign in failed; unknown email or password.");
        } else {
            com.ipia.mmarket.View.Activity.DisplayUtils.showFieldDialog(activity, "No internet connection. Please check your network settings.");
        }
    }

    private static void registerFailure(Activity activity) {
        if (com.ipia.mmarket.View.Activity.DisplayUtils.isNetworkConnected(activity)) {
            com.ipia.mmarket.View.Activity.DisplayUtils.showFieldDialog(activity, "The email was not of a valid format or the password length was less than 6 characters.");
        } else {
            com.ipia.mmarket.View.Activity.DisplayUtils.showFieldDialog(activity, "No internet connection. Please check your network settings.");
        }
    }
}
