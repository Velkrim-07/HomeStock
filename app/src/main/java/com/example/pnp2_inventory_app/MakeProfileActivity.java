package com.example.pnp2_inventory_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;

public class MakeProfileActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
    TextInputEditText editTextFirstName, editTextLastName;
    ImageView profilePic;
    Button continuebtn;
    //creating object to connect to firebase authorization
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private Uri imageUri; // Declare the imageUri variable here

    // Constants for image picker
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //assigning objects to specific id's
        profilePic = findViewById(R.id.profileImage);
        editTextFirstName = findViewById(R.id.firstname);
        editTextLastName = findViewById(R.id.lastname);
        continuebtn = findViewById(R.id.continuebtn);
        progressBar = findViewById(R.id.progressBar);

        //Add OnEditorActionListener to editTextFirstName
        //pressing enter key to register
        editTextFirstName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    continuebtn.performClick();
                    return true;
                }
                return false;
            }
        });

        //Add OnEditorActionListener to editTextLastName
        //pressing enter key to register
        editTextLastName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    continuebtn.performClick();
                    return true;
                }
                return false;
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });


        //event handler for register button
        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //makes progress bar visible after clicking register button
                progressBar.setVisibility(View.VISIBLE);
                String firstname, lastname;
                firstname = String.valueOf(editTextFirstName.getText());
                lastname = String.valueOf(editTextLastName.getText());
                //checks if the fields are empty
                if (TextUtils.isEmpty(firstname)) {
                    Toast.makeText(MakeProfileActivity.this, "Enter first name", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(lastname)) {
                    Toast.makeText(MakeProfileActivity.this, "Enter last name", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                //creates the profile and inputs the data to firebase

                UserProfileChangeRequest profileName = new UserProfileChangeRequest.Builder()
                        .setDisplayName(firstname + " " + lastname)
                        .build();

                currentUser.updateProfile(profileName)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(MakeProfileActivity.this, "Profile Created", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(MakeProfileActivity.this, "Make a profile before proceeding", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    // Method to open the image picker
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}