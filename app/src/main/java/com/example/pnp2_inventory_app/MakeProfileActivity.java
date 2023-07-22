package com.example.pnp2_inventory_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

        // Load user's profile image (if available)
        loadUserProfileImage();

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

                String name = currentUser.getDisplayName();
                String email = currentUser.getEmail();

                //Created Map object to input data(specific user)
                Map<String, Object> userData = new HashMap<>();
                userData.put("name", name);
                userData.put("email", email);

                // Get the current user's UID
                String uid = currentUser.getUid();
                // Access your Firebase Firestore instance
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Creates a reference to the user's document in Firestore
                DocumentReference userRef = db.collection("users").document(uid);

                userRef.set(userData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // User documentID is created
                                // Load the profile image again after creating the user documentID
                                loadUserProfileImage();
                            }
                        });

                // Update the user's profile with the new display name
                currentUser.updateProfile(profileName)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String uid = currentUser.getUid();
                                // Access your Firebase Firestore instance
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                // Creates a reference to the user's document in Firestore
                                DocumentReference userRef = db.collection("users").document(uid);

                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Get current user after making profile
                                    currentUser = mAuth.getCurrentUser();
                                    if (currentUser != null) {
                                        // Gets the display name
                                        String displayName = currentUser.getDisplayName();
                                        if (!TextUtils.isEmpty(displayName)) {
                                            userData.put("name", displayName);
                                            //Updates the display name in Firestore
                                            userRef.update(userData)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(MakeProfileActivity.this, "Profile Created", Toast.LENGTH_SHORT).show();
                                                            if (imageUri != null) {
                                                                uploadProfilePicture(imageUri);
                                                            } else {
                                                                navigateToMain();
                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(MakeProfileActivity.this, "Failed to create a profile", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                } else {
                                    Toast.makeText(MakeProfileActivity.this, "Failed to create a profile", Toast.LENGTH_SHORT).show();
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
            imageUri = data.getData();
            try {
                // Load the image into the "profilePic" ImageView using Glide and apply a circular transformation
                Glide.with(this)
                        .load(imageUri)
                        .apply(RequestOptions.circleCropTransform()) // Apply circular transformation
                        .into(profilePic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Method to upload the profile picture to Firebase Storage
    private void uploadProfilePicture(Uri imageUri) {
        if (imageUri != null) {
            // Create a Firebase Storage reference for the image
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final String randomKey = currentUser.getDisplayName() + UUID.randomUUID();
            // Create a reference to images
            StorageReference reference = storageRef.child("images/" + randomKey);

            // Upload the image to Firebase Storage
            reference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL of the uploaded image
                        reference.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            // Save the download URL in Firestore
                            String imageUrl = downloadUri.toString();
                            saveImageUrlToDatabase(imageUrl);

                            // Navigate to the main activity after updating the profile picture
                            navigateToMain();
                        });
                    })
                    .addOnFailureListener(exception -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MakeProfileActivity.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(snapshot -> {
                        // Show progress of image upload
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressBar.setProgress((int) progressPercent);
                    });
        }
    }

    // Method to save the profile picture URL in Firebase Firestore
    private void saveImageUrlToDatabase(String imageUrl) {
        // Get the current user's UID
        String uid = currentUser.getUid();

        // Access your Firebase Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a reference to the user's document in Firestore
        DocumentReference userRef = db.collection("users").document(uid);

        // Update the 'profileImageUrl' field with the image URL
        userRef.update("profileImageUrl", imageUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // The image URL is successfully saved in the database
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure to save the image URL
                    }
                });
    }

    // Method to load the user's profile image
    private void loadUserProfileImage() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Access your Firebase Firestore instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Creates a reference to the user's document in Firestore
            DocumentReference userRef = db.collection("users").document(uid);

            // Get the user's document from Firestore
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        // User document exists, retrieve the profile image URL from the document
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");

                        // Load the profile image into the ImageView using Glide
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            // Use Glide's CircleCrop transformation to crop photo
                            RequestOptions requestOptions = new RequestOptions()
                                    .transforms(new CircleCrop())
                                    .skipMemoryCache(true);

                            Glide.with(getApplicationContext())
                                    .load(profileImageUrl)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(profilePic);
                        }
                    } else {
                        // User document doesn't exist, create it with the UID and other user-specific data
                        String name = currentUser.getDisplayName();
                        String email = currentUser.getEmail();

                        //Created Map object to input data(specific user)
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("name", name);
                        userData.put("email", email);

                        userRef.set(userData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // User documentID is created
                                        // Load the profile image again after creating the user documentID
                                        loadUserProfileImage();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle the failure to create the user document
                                    }
                                });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle the failure to retrieve the user's document
                }
            });
        }
    }

// Method to navigate to the main activity
    private void navigateToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}