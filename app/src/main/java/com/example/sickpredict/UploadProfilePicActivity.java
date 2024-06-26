package com.example.sickpredict;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;

public class UploadProfilePicActivity extends AppCompat {

    private ProgressBar progressBar;
    private ImageView imageViewUploadPic;
    private FirebaseAuth authProfile;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;
    private SwipeRefreshLayout swipeContainer;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
    DatabaseReference databasePatientListReference = FirebaseDatabase.getInstance().getReference("chats");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_pic);

        swipeToRefresh();

        Button buttonChoosePic = findViewById(R.id.upload_pic_choose_button);
        Button buttonUploadPic = findViewById(R.id.upload_pic_button);
        progressBar = findViewById(R.id.progressBar);
        imageViewUploadPic = findViewById(R.id.imageView_profile_dp);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");
        Uri uri = firebaseUser.getPhotoUrl();

        //Set User's Current DP in ImageView (If Uploaded already)

        Picasso.with(UploadProfilePicActivity.this).load(uri).into(imageViewUploadPic);

        //Choosing Pic to upload
        buttonChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        //Upload Image
        buttonUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                UploadPic();
            }
        });
    }

    private void swipeToRefresh() {
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(getIntent());
                finish();
                overridePendingTransition(0,0);
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,  android.R.color.holo_orange_light,  android.R.color.holo_red_light);
    }

    private void UploadPic() {
        if(uriImage!= null){
            //Save the image with uid of the currently logged user
            StorageReference fileReference = storageReference.child(authProfile.getCurrentUser().getUid() + "/displaypic."
                    + getFileExtension(uriImage));

            //Upload image to storage
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            firebaseUser = authProfile.getCurrentUser();



                            //Set the DP of user after uploaded

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileUpdates);
                            databaseReference.child(firebaseUser.getUid()).child("profile").setValue(downloadUri.toString());
                        }
                    });

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadProfilePicActivity.this, "Upload Successfull!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadProfilePicActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadProfilePicActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            progressBar.setVisibility(View.GONE);
            Toast.makeText(UploadProfilePicActivity.this, "No File Selected!",Toast.LENGTH_SHORT).show();
        }
    }

    //Obtain file extension of the image
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData()!= null){
            uriImage = data.getData();
            imageViewUploadPic.setImageURI(uriImage);
        }
    }

    @Override
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
        }

        else if (id == R.id.menu_update_profile) {
            Intent intent = new Intent(UploadProfilePicActivity.this,EditProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_update_email) {
            Intent intent = new Intent(UploadProfilePicActivity.this,UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        } /*else if (id == R.id.menu_settings) {
            Toast.makeText(UploadProfilePicActivity.this, "menu_settings",Toast.LENGTH_SHORT).show();
        } */else if (id == R.id.menu_change_password) {
            Intent intent = new Intent(UploadProfilePicActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_delete_profile) {
            Intent intent = new Intent(UploadProfilePicActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(UploadProfilePicActivity.this, "Logged out!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UploadProfilePicActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(UploadProfilePicActivity.this, "Something went wrong!",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

}