package com.example.sickpredict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sickpredict.utils.LoadingDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class HomeActivity extends AppCompat {

    SharedPreferences sharedPref ;
    SharedPreferences.Editor editor;

    FirebaseAuth authProfile;
    private LoadingDialog loadingDialog;
    private String useID = "no-uid";
    private CardView crd1,crd2;
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

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        getUserData(firebaseUser);

        crd1 = (CardView) findViewById(R.id.card1);
        crd2 = (CardView) findViewById(R.id.card2);
        changeLang = (Button) findViewById(R.id.change_lang);

        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show AlertDialog to display list of languages
                final String[] listItems = {"English","हिंदी","मराठी"};
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);
                mBuilder.setTitle("Choose Language...");
                mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){
                            lang.updateResource ("en");
                            recreate();
                        } else if (i==1) {
                            lang.updateResource ("hi");
                            recreate();
                        }else if (i==2) {
                            lang.updateResource("mr");
                            recreate();
                        }

                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mdialog = mBuilder.create();
                mdialog.show();

            }
        });


        crd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent secondActivity = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(secondActivity);
            }
        });

        crd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent secondActivity1 = new Intent(HomeActivity.this, DoctorLoginActivity.class);
                startActivity(secondActivity1);
            }
        });
    }

    private void getUserData(FirebaseUser firebaseUser){

        loadingDialog.show();
        if(firebaseUser != null) {
            useID = firebaseUser.getUid();
        }

        else {
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
                if(helperClass != null){

                    editor.putString("dob", helperClass.dob);
                    editor.putString("gender", helperClass.gender);
                    editor.apply();
                    loadingDialog.hide();

                }else{

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




    /*
    private void showChangeLanguageDialog() {
        final String[] listItems = {"English","हिंदी","मराठी"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);
        mBuilder.setTitle("Choose Language...");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    setLocale ("en");
                    recreate();
                } else if (i==1) {
                    setLocale ("hi");
                    recreate();
                }else if (i==2) {
                    setLocale("mr");
                    recreate();
                }

                dialogInterface.dismiss();
            }
        });

        AlertDialog mdialog = mBuilder.create();
        mdialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale= locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        setLocale(language);
    }
*/
}