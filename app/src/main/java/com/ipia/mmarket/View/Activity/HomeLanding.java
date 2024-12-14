package com.ipia.mmarket.View.Activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ipia.mmarket.R;
import com.ipia.mmarket.View.Fragment.AccountFragment;
import com.ipia.mmarket.View.Fragment.ChatFragment;
import com.ipia.mmarket.View.Fragment.HomeFragment;
import com.ipia.mmarket.databinding.ActivityHomeLandingBinding;

public class HomeLanding extends AppCompatActivity {

    ActivityHomeLandingBinding binding;
    FirebaseAuth auth;
    Button logoutButton;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawNavBar(savedInstanceState);
    }

    private void drawNavBar(Bundle savedInstanceState){
        binding = ActivityHomeLandingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavBar = binding.bottomNavBar;

        bottomNavBar.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.chat) {
                selectedFragment = new ChatFragment();
            } else if (itemId == R.id.account) {
                selectedFragment = new AccountFragment();
            }
            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }
            return true;
        });

        if (savedInstanceState == null) {
            bottomNavBar.setSelectedItemId(R.id.home);
            replaceFragment(new HomeFragment());
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
