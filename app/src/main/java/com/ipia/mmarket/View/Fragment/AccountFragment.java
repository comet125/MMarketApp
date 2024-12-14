package com.ipia.mmarket.View.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ipia.mmarket.R;
import com.ipia.mmarket.View.Activity.Login;
import com.ipia.mmarket.MainActivity;

import java.util.Locale;

public class AccountFragment extends Fragment {

    private FirebaseAuth auth;
    private Button logoutButton;
    private Button deleteButton;
    private Button changePasswordButton;
    private Spinner languageSpinner;
    private FirebaseUser user;

    public AccountFragment() {

    }

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        logoutButton = view.findViewById(R.id.logout);
        deleteButton = view.findViewById(R.id.delete_account);
        changePasswordButton = view.findViewById(R.id.change_pw);
        languageSpinner = view.findViewById(R.id.language_spinner);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePw();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteAccount();
            }
        });

        setupLanguageSpinner();

        return view;
    }

    private void setupLanguageSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{"Choose language", "English", "Bosnian", "Croatian"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        languageSpinner.setSelection(0);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLang = "";
                switch (position) {
                    case 0:
                        return;
                    case 1:
                        selectedLang = "en";
                        break;
                    case 2:
                        selectedLang = "bs";
                        break;
                    case 3:
                        selectedLang = "hr";
                        break;
                }

                if (!selectedLang.equals(Locale.getDefault().getLanguage())) {
                    setLocale(selectedLang);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        Intent refresh = new Intent(getActivity(), MainActivity.class);
        startActivity(refresh);
        getActivity().finish();
    }

    private void confirmDeleteAccount() {
        new AlertDialog.Builder(requireContext())
                .setTitle("MMarket Critical Alert")
                .setMessage("Are you sure you want to delete your account? This action is permanent.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteAccount() {
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Account deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), Login.class);
                                startActivity(intent);
                                requireActivity().finish();
                            } else {
                                DisplayUtils.showFieldDialog(getContext(), "Failed to delete account. Check your connection or try again after logging out and logging back in.");
                            }
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No user is currently logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void changePw(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("MMarket Alert");
        builder.setMessage("Enter your new password");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPassword = input.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DisplayUtils.showFieldDialog(getContext(), "Password changed successfully!");
                                } else {
                                    DisplayUtils.showFieldDialog(getContext(), "Your password couldn't be changed. This might be because:\n\n- The password is shorter than 6 characters.\n\n- There is no internet connection\n\n- Bad session, try logging out and trying again ");
                                }
                            });
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}

