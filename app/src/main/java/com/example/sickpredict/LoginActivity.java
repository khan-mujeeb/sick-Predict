package com.example.sickpredict;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompat {

    private EditText loginUsername, loginPassword;
    private Button loginButton;
    private TextView signupRedirectText, forgotPassword;
    private CheckBox chk;

    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private static final String TAG = "LoginActivity";
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    String UserRecordIDFromServer = myRef.push().getKey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));


        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.progressBar);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        loginButton = findViewById(R.id.login_button);
        forgotPassword = findViewById(R.id.ForgotPassword);
        chk = findViewById(R.id.chkBox1);


        checkUserIsLoginedOrNot();

        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    loginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        //Reset Password
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "You can reset your password now!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(view -> {
            String textEmail = loginUsername.getText().toString();
            String textPwd = loginPassword.getText().toString();

            if (TextUtils.isEmpty(textEmail)) {
                Toast.makeText(LoginActivity.this, "Please enter your email.", Toast.LENGTH_LONG).show();
                loginUsername.setError("Email is required");
                loginUsername.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                Toast.makeText(LoginActivity.this, "Please Re-enter your email.", Toast.LENGTH_LONG).show();
                loginUsername.setError("Valid email is required");
                loginUsername.requestFocus();
            } else if (TextUtils.isEmpty(textPwd)) {
                Toast.makeText(LoginActivity.this, "Please enter your password.", Toast.LENGTH_LONG).show();
                loginPassword.setError("Password is required");
                loginPassword.requestFocus();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                loginUser(textEmail, textPwd);
            }
        });

    }

    private void checkUserIsLoginedOrNot() {
        //Get current user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {

            startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class));
            finish();
        }
    }

    private void loginUser(String email, String pwd) {
        firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    //Get instance of the current user
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                    //Check if email is verified before user can access app
                    if (firebaseUser.isEmailVerified()) {
                        Toast.makeText(LoginActivity.this, "You are logged in now!", Toast.LENGTH_SHORT).show();

                        //Start ProfileActiviy
                        startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class));
                        finish();
                    } else {
                        firebaseUser.sendEmailVerification();
                        firebaseAuth.signOut();
                        showAlertDialog();
                    }
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        loginUsername.setError("User does not exists or no longer valid. Please register again.");
                        loginUsername.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        loginUsername.setError("Invalid credentials. Kindly, check and re-enter.");
                        loginUsername.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email Not Verfied!");
        builder.setMessage("Please verify your email now! You can not login without email verfication.");

        //open email app if user clicks continue
        builder.setPositiveButton("Conitnue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // TO open in new tab
                startActivity(intent);
            }
        });
        //Create AlerDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}