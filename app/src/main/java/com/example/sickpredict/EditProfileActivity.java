package com.example.sickpredict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompat {

    private EditText editTextUpdateName, editTextUpdateDob, editTextUpdateMobile,editTextUpdateEmail;
    private RadioGroup radioGroupUpdateGender;
    private RadioButton radioButtonUpdateGenderSelected;
    private String textFullName, textDob, textGender, textMobile;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        progressBar = findViewById(R.id.progressBar);
        editTextUpdateName = findViewById(R.id.editText_update_full_name);
        editTextUpdateMobile = findViewById(R.id.editText_update_mobile);
        editTextUpdateDob = findViewById(R.id.editText_update_dob);

        radioGroupUpdateGender = findViewById(R.id.radio_group_update_gender);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        //Show Profile Data
        showUserData(firebaseUser);

        //Upload profile pic
        Button buttonUploadProfilePic = findViewById(R.id.button_upload_pic);
        buttonUploadProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this,UploadProfilePicActivity.class);
                startActivity(intent);
                finish();
            }
        });
/*
        //Update Email
        Button buttonUpdateEmail= findViewById(R.id.button_update_email);
        buttonUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this,UpdateEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });
*/
        editTextUpdateDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] textSADob = textDob.split("/");

                int day = Integer.parseInt(textSADob[0]);
                int month = Integer.parseInt(textSADob[1]) - 1;
                int year = Integer.parseInt(textSADob[2]);

                //Date Picker Dialog
                picker = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                        editTextUpdateDob.setText(dayofMonth + "/" + (month + 1) + "/" + year );
                    }
                }, year, month, day);
                picker.show();
            }
        });

        //Update Profile
        Button buttonUpdateProfile = findViewById(R.id.button_update_profile);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(firebaseUser);
            }
        });
    }

    private void updateProfile(FirebaseUser firebaseUser) {
        int selectedGenderID = radioGroupUpdateGender.getCheckedRadioButtonId();
        radioButtonUpdateGenderSelected = findViewById(selectedGenderID);

        //Validate mobile number using matcher and regular expression
        String mobileRegex = "[6-9][0-9]{9}"; //first no. can be 9, rest no. can be any
        Matcher mobileMatcher;
        Pattern mobilePattern = Pattern.compile(mobileRegex);
        mobileMatcher = mobilePattern.matcher(textMobile);

        if(TextUtils.isEmpty(textFullName)){
            Toast.makeText(EditProfileActivity.this,"Please Enter Your Full Name",Toast.LENGTH_LONG).show();
            editTextUpdateName.setError("Full Name is Required");
            editTextUpdateName.requestFocus();

        } else if(TextUtils.isEmpty(textMobile)){
            Toast.makeText(EditProfileActivity.this,"Please Enter Your Mobile Number",Toast.LENGTH_LONG).show();
            editTextUpdateMobile.setError("Mobile Number is Required");
            editTextUpdateMobile.requestFocus();

        } else if(textMobile.length()!=10){
            Toast.makeText(EditProfileActivity.this,"Please Re-Enter Your Mobile Number",Toast.LENGTH_LONG).show();
            editTextUpdateMobile.setError("Mobile Number Should be 10 Digits");
            editTextUpdateMobile.requestFocus();

        } else if (!mobileMatcher.find()) {
            Toast.makeText(EditProfileActivity.this,"Please Re-Enter Your Mobile Number",Toast.LENGTH_LONG).show();
            editTextUpdateMobile.setError("Mobile Number is not valid.");
            editTextUpdateMobile.requestFocus();

        }else if(radioGroupUpdateGender.getCheckedRadioButtonId() == -1){
            Toast.makeText(EditProfileActivity.this,"Please Select Your Gender",Toast.LENGTH_LONG).show();
            radioButtonUpdateGenderSelected.setError("Gender is Required");
            radioButtonUpdateGenderSelected.requestFocus();

        }else if(TextUtils.isEmpty(textDob)){
            Toast.makeText(EditProfileActivity.this,"Please Enter Your DOB",Toast.LENGTH_LONG).show();
            editTextUpdateDob.setError("DOB is Required");
            editTextUpdateDob.requestFocus();
        }else {
            textGender = radioButtonUpdateGenderSelected.getText().toString();
            textFullName = editTextUpdateName.getText().toString();
            textDob = editTextUpdateDob.getText().toString();
            textMobile = editTextUpdateMobile.getText().toString();

            HelperClass helperClass = new HelperClass(textFullName,textMobile,textGender,textDob);

            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

            String userID = firebaseUser.getUid();

            progressBar.setVisibility(View.VISIBLE);

            referenceProfile.child(userID).setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                        firebaseUser.updateProfile(profileUpdates);

                        Toast.makeText(EditProfileActivity.this,"Details Updated Successfully!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfileActivity.this,UserProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }else{
                        try{
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(EditProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    //Fetch Data from firebase and display
    private void showUserData(FirebaseUser firebaseUser){

        String useID = firebaseUser.getUid();

        //Extracting user reference from database
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(useID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HelperClass helperClass = snapshot.getValue(HelperClass.class);
                if(helperClass != null){
                    textFullName = helperClass.fullname;
                    textMobile = helperClass.mobile;
                    textGender = helperClass.gender;
                    textDob = helperClass.dob;

                    editTextUpdateName.setText(textFullName);
                    editTextUpdateMobile.setText(textMobile);
                    editTextUpdateDob.setText(textDob);

                    if(textGender.equals("Male")){
                        radioButtonUpdateGenderSelected = findViewById(R.id.radio_male);
                    }else {
                        radioButtonUpdateGenderSelected = findViewById(R.id.radio_female);
                    }
                    radioButtonUpdateGenderSelected.setChecked(true);

                }else{
                    Toast.makeText(EditProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //When any item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_refresh){
            //Refresh activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        } else if (id == R.id.menu_update_profile) {
            Intent intent = new Intent(EditProfileActivity.this,EditProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_update_email) {
            Intent intent = new Intent(EditProfileActivity.this,UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        }/* else if (id == R.id.menu_settings) {
            Toast.makeText(UserProfileActivity.this, "menu_settings",Toast.LENGTH_SHORT).show();
        }*/ else if (id == R.id.menu_change_password) {
            Intent intent = new Intent(EditProfileActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_delete_profile) {
            Intent intent = new Intent(EditProfileActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(EditProfileActivity.this, "Logged out!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(EditProfileActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(EditProfileActivity.this, "Something went wrong!",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}