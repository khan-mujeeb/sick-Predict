package com.example.sickpredict;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sickpredict.data.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompat {

    private EditText editTextRegisterFullname, editTextRegisterEmail, editTextRegisterDob, editTextRegisterMobile, editTextRegisterPwd, editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private TextView loginRedirectText;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonGenderSelected;
    private static final String TAG = "RegisterActivity";

    private CheckBox chk;
    private DatePickerDialog picker;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressBar = findViewById(R.id.progressBar);
        editTextRegisterFullname = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDob = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPwd = findViewById(R.id.editText_register_confirm_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        chk = (CheckBox) findViewById(R.id.chkBox1);

        //Radiobutton for gender
        radioGroupGender = findViewById(R.id.radio_group_register_gender);
        radioGroupGender.clearCheck();

        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editTextRegisterConfirmPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    editTextRegisterConfirmPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //Setting up DatePicker on EditText
        editTextRegisterDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date Picker Dialog
                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                        editTextRegisterDob.setText(dayofMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });


        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectGenderId = radioGroupGender.getCheckedRadioButtonId();
                radioButtonGenderSelected = findViewById(selectGenderId);

                String textFullName = editTextRegisterFullname.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDob = editTextRegisterDob.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textConfirmPwd = editTextRegisterConfirmPwd.getText().toString();
                String textGender;


                //Validate mobile number using matcher and regular expression
                String mobileRegex = "[6-9][0-9]{9}"; //first no. can be 9, rest no. can be any
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(textMobile);

                if (TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Full Name", Toast.LENGTH_LONG).show();
                    editTextRegisterFullname.setError("Full Name is Required");
                    editTextRegisterFullname.requestFocus();

                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Email Id", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Email Id is Required");
                    editTextRegisterEmail.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please Re-Enter Email", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Valid Email is Required");
                    editTextRegisterEmail.requestFocus();


                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Mobile Number", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile Number is Required");
                    editTextRegisterMobile.requestFocus();

                } else if (textMobile.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Please Re-Enter Your Mobile Number", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile Number Should be 10 Digits");
                    editTextRegisterMobile.requestFocus();

                } else if (!mobileMatcher.find()) {
                    Toast.makeText(RegisterActivity.this, "Please Re-Enter Your Mobile Number", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile Number is not valid.");
                    editTextRegisterMobile.requestFocus();

                } else if (radioGroupGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RegisterActivity.this, "Please Select Your Gender", Toast.LENGTH_LONG).show();
                    radioButtonGenderSelected.setError("Gender is Required");
                    radioButtonGenderSelected.requestFocus();

                } else if (TextUtils.isEmpty(textDob)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your DOB", Toast.LENGTH_LONG).show();
                    editTextRegisterDob.setError("DOB is Required");
                    editTextRegisterDob.requestFocus();

                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Password", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Password is Required");
                    editTextRegisterPwd.requestFocus();

                } else if (textPwd.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password Should be Atleast 6 digits", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Password is too weak");
                    editTextRegisterPwd.requestFocus();

                } else if (TextUtils.isEmpty(textConfirmPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please Confirm Your Password", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("Password Confirmation is Required");
                    editTextRegisterConfirmPwd.requestFocus();

                } else if (!textPwd.equals(textConfirmPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Same Password", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("Password Not Matched");
                    editTextRegisterConfirmPwd.requestFocus();
                    editTextRegisterPwd.clearComposingText();
                    editTextRegisterConfirmPwd.clearComposingText();

                } else {
                    textGender = radioButtonGenderSelected.getText().toString();
                    progressBar.setVisibility(view.VISIBLE);
                    registerUser(textFullName, textEmail, textDob, textGender, textMobile, textPwd);
                }

            }
        });

    }

    //Register user with given credentials
    private void registerUser(String textFullName, String textEmail, String textDob, String textGender, String textMobile, String textPwd) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Create user profile
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            //Enter user data in firebase database
                            HelperClass helperClass = new HelperClass(textFullName, textEmail, textDob, textGender, textMobile);
                            User user = new User(
                                    textFullName,
                                    textEmail,
                                    textDob,
                                    textGender,
                                    textMobile,
                                    "",
                                    firebaseUser.getUid().toString()
                            );
                            //result = editTextRegisterFullname.getText().toString();

                            //Extracting user reference from database for "Registered Users"
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");

                            databaseReference.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        //send verfication email
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(RegisterActivity.this, "User registered successfully! Please verify your email.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(RegisterActivity.this, "User registeration failed! Please try again.", Toast.LENGTH_LONG).show();
                                    }
                                    //Hide Progressbar whether user creation is successful or failed
                                    progressBar.setVisibility(View.GONE);

                                }
                            });


                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                editTextRegisterPwd.setError("Your Password is too weak. Kindly use a mix of alphabates, numbers and special characters");
                                editTextRegisterPwd.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                editTextRegisterPwd.setError("Your email is invalid or already in use. Kindly Re-enter");
                                editTextRegisterPwd.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                editTextRegisterPwd.setError("User is already registered with this email. Use Another email.");
                                editTextRegisterPwd.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            //Hide Progressbar whether user creation is successful or failed
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}