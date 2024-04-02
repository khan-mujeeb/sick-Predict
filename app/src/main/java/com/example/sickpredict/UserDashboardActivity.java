package com.example.sickpredict;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.example.sickpredict.data.doctor.Doctor;
import com.example.sickpredict.databinding.ActivityUserDashboardBinding;
import com.example.sickpredict.doctor.DoctorProfileActivity;
import com.example.sickpredict.user.HomeRemediesActivity;
import com.example.sickpredict.utils.DoctorData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;

public class UserDashboardActivity extends AppCompat {

    ArrayList<Doctor> doctors = DoctorData.INSTANCE.getDoctorList();

    private FirebaseAuth authProfile;
    private ActivityUserDashboardBinding binding;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private LinearLayout layout_predict, layout_vc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));

        authProfile = FirebaseAuth.getInstance();

        layout_predict = binding.layoutPredict;

        subscribeOnClickEvents();

        if (currentUser != null) {
            String useID = currentUser.getUid();
            if (
                    useID.equals(doctors.get(0).getUid())

            ) {
                binding.doctorCard1.setVisibility(View.GONE);
            } else  if (
                    useID.equals(doctors.get(1).getUid())

            ) {
                binding.doctorCard2.setVisibility(View.GONE);
            } else  if (
                    useID.equals(doctors.get(2).getUid())

            ) {
                binding.doctorCard3.setVisibility(View.GONE);
            }
        }



        layout_predict.setOnClickListener(view -> {
            Intent intent = new Intent(UserDashboardActivity.this, PredictionActivity.class);
            startActivity(intent);
        });

        binding.layoutVc.setOnClickListener(view -> {
            Intent intent = new Intent(UserDashboardActivity.this, VIdeoConsultation.class);
            startActivity(intent);
        });
    }

    private void subscribeOnClickEvents() {
        Intent intent = new Intent(UserDashboardActivity.this, DoctorProfileActivity.class);

        binding.doctorCard1.setOnClickListener(view -> {
            intent.putExtra("doctorId", 0);
            startActivity(intent);
        });

        binding.doctorCard2.setOnClickListener(view -> {
            intent.putExtra("from", "user");
            intent.putExtra("doctorId", 1);
            startActivity(intent);
        });

        binding.doctorCard3.setOnClickListener(view -> {
            intent.putExtra("from", "user");
            intent.putExtra("doctorId", 2);
            startActivity(intent);
        });


        binding.homeRemedies.setOnClickListener(view -> {
            Intent intent1 = new Intent(UserDashboardActivity.this, HomeRemediesActivity.class);
            startActivity(intent1);
        });
    }

    //Creating Action Bar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //When any item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
            //Refresh activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);
        } else if (id == R.id.menu_update_profile) {
            Intent intent = new Intent(UserDashboardActivity.this, EditProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_update_email) {
            Intent intent = new Intent(UserDashboardActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_change_password) {
            Intent intent = new Intent(UserDashboardActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_delete_profile) {
            Intent intent = new Intent(UserDashboardActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(UserDashboardActivity.this, "Logged out!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UserDashboardActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(UserDashboardActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}