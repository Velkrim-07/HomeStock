package com.example.pnp2_inventory_app;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.annotation.SuppressLint;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class ProfileFragment extends Fragment {
    private static final int REQUEST_IMAGE_PICKER = 2;
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1;

    public Uri imageUri;
    public SwitchCompat nightModeSwitch;
    private boolean isNightModeEnabled = false; // Track the current night mode state
    private AlertDialog dialog;
    private ImageView arrowFamily;
    private SwitchCompat notifswitch;
    private Button editProfileButton;
    private TextView emailTextView, nameTextView;
    private ImageView profileImage;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage; //calls fb storage
    private StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Initialize views
        nightModeSwitch = view.findViewById(R.id.nightmodebtn);
        arrowFamily = view.findViewById(R.id.arrowFamilybtn);
        notifswitch = view.findViewById(R.id.notifswitchbtn);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        emailTextView = view.findViewById(R.id.email);
        nameTextView  = view.findViewById(R.id.nameofuser);
        profileImage = view.findViewById(R.id.profileImage);

        String name = currentUser.getDisplayName();
        nameTextView.setText(name);
        String email = currentUser.getEmail();
        emailTextView.setText(email);

        // Check if permission is granted to access the camera roll
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        }

        //on click listener for profileImage
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the image picker
                choosePicture();
            }
        });

        // Set click listeners
        arrowFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowFamilyView();
            }
        });

        //handles notification switch
        notifswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle nightModeSwitch state change
                if (isChecked) {
                    // Notification is enabled
                    Toast.makeText(getContext(), "Notifications on", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Notification is disabled
                    Toast.makeText(getContext(), "Notifications off", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        editProfileButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle editProfileButton click event
//                openEditProfileActivity();
//            }
//        });

        // Initialize night mode switch
        nightModeSwitch.setChecked(isNightModeEnabled);
        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle night mode switch state change
                if (isChecked) {
                    // Night mode is enabled
                    enableNightMode();
                } else {
                    // Night mode is disabled
                    disableNightMode();
                }
            }
        });

        return view;
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading image");
        pd.show();


        final String randomKey = UUID.randomUUID().toString();
        // Create a reference to images
        StorageReference reference = storageReference.child("images/" + randomKey);

        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "Image uploaded", Toast.LENGTH_SHORT).show();;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "Image upload fail", Toast.LENGTH_SHORT).show();;
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Uploading" + (int) progressPercent + "%");
                    }
                });
    }

    //method to enable night mode
    private void enableNightMode() {
        if (!isNightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            isNightModeEnabled = true;
        }
    }

    //method to disable night mode
    private void disableNightMode() {
        if (isNightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            isNightModeEnabled = false;
        }
    }

    private void ShowFamilyView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_family_view, null);
        builder.setView(dialogView);

        RecyclerView familyView = dialogView.findViewById(R.id.familyView);
        Button addFamilyMem = dialogView.findViewById(R.id.addfamilymem);

        addFamilyMem.setOnClickListener(v -> {

            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        dialog = builder.create();
        dialog.show();
    }
}
