package com.example.sickpredict;

import com.example.sickpredict.data.doctor.Doctor;
import com.example.sickpredict.doctor.DoctorDashboardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sickpredict.utils.DoctorData;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<Doctor> doctors = DoctorData.INSTANCE.getDoctorList();

        Thread thread = new Thread() {

            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    if (currentUser != null) {
                        String useID = currentUser.getUid();
                        if (
                                useID.equals(doctors.get(0).getUid()) ||
                                        Objects.equals(useID, doctors.get(1).getUid()) ||
                                        Objects.equals(useID, doctors.get(2).getUid())

                        ) {
                            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                            finish();
                        } else {

                            startActivity(new Intent(SplashActivity.this, UserDashboardActivity.class));
                            finish();

                        }
                        // Your existing code that uses the useID
                    } else {
                        // If the user is not authenticated, redirect to the login activity
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        finish(); // Finish the current activity
                    }


                }
            }
        };thread.start();
    }
}