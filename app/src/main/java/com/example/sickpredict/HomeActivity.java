package com.example.sickpredict;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;

import com.example.sickpredict.data.doctor.Doctor;
import com.example.sickpredict.utils.DoctorData;
import com.example.sickpredict.utils.LoadingDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompat {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    FirebaseAuth authProfile;
    private LoadingDialog loadingDialog;
    private String useID = "no-uid";
    private CardView crd1, crd2;
    private Button changeLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        sharedPref = getApplicationContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));

        LanguageManager lang = new LanguageManager(this);
        loadingDialog = new LoadingDialog(this);

        crd1 = findViewById(R.id.card1);
        crd2 = findViewById(R.id.card2);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        getUserData(firebaseUser);


        changeLang = findViewById(R.id.change_lang);


        changeLang.setOnClickListener(view -> {
            //Show AlertDialog to display list of languages
            final String[] listItems = {"English", "हिंदी", "मराठी"};
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);
            mBuilder.setTitle("Choose Language...");
            mBuilder.setSingleChoiceItems(listItems, -1, (dialogInterface, i) -> {
                if (i == 0) {
                    lang.updateResource("en");
                    recreate();
                } else if (i == 1) {
                    lang.updateResource("hi");
                    recreate();
                } else if (i == 2) {
                    lang.updateResource("mr");
                    recreate();
                }

                dialogInterface.dismiss();
            });

            AlertDialog mdialog = mBuilder.create();
            mdialog.show();

        });


        crd1.setOnClickListener(view -> {
            Intent secondActivity = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(secondActivity);
        });

        crd2.setOnClickListener(view -> {
            Intent secondActivity1 = new Intent(HomeActivity.this, DoctorLoginActivity.class);
            startActivity(secondActivity1);
        });
    }

    private void getUserData(FirebaseUser firebaseUser) {
        ArrayList<Doctor> doctors = DoctorData.INSTANCE.getDoctorList();
        loadingDialog.show();
        if (firebaseUser != null) {
            useID = firebaseUser.getUid();
            if (
                    useID.equals(doctors.get(0).getUid()) ||
                    Objects.equals(useID, doctors.get(1).getUid()) ||
                    Objects.equals(useID, doctors.get(2).getUid())

            ) {
                crd2.setVisibility(View.VISIBLE);
                crd1.setVisibility(View.VISIBLE);
            } else {
                crd2.setVisibility(View.GONE);
                crd1.setVisibility(View.GONE);

                startActivity(new Intent(HomeActivity.this, UserDashboardActivity.class));
                finish();
            }
        } else {
            loadingDialog.hide();
            Toast.makeText(HomeActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
            return;
        }

        //Extracting user reference from database
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(useID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HelperClass helperClass = snapshot.getValue(HelperClass.class);
                if (helperClass != null) {

                    editor.putString("dob", helperClass.dob);
                    editor.putString("gender", helperClass.gender);
                    editor.putString("name", helperClass.fullname);
                    editor.apply();
                    loadingDialog.hide();

                } else {

                    loadingDialog.hide();
                    Toast.makeText(HomeActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingDialog.hide();
                Toast.makeText(HomeActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
            }


        });
    }


}