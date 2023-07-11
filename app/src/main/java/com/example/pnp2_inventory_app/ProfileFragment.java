package com.example.pnp2_inventory_app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.annotation.SuppressLint;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_IMAGE_PICKER = 2;

    private Switch nightModeSwitch;
    private ImageView arrowFamily;
    private Switch notifswitch;
    private Button editProfileButton;
    private TextView emailTextView;
    private ImageView profileImage;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        // Initialize views
//        nightModeSwitch = view.findViewById(R.id.nightmodebtn);
//        arrowFamily = view.findViewById(R.id.arrowFamilybtn);
//        notifswitch = view.findViewById(R.id.notifswitchbtn);
//        editProfileButton = view.findViewById(R.id.editProfileButton);
        emailTextView = view.findViewById(R.id.email);
        profileImage = view.findViewById(R.id.profileImage);

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

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the image picker
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICKER);
            }
        });
//
//        // Set click listeners
//        arrowFamily.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle arrowFamily click event
//            }
//        });
//
//        notifswitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle arrowNotifications click event
//            }
//        });

//        editProfileButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle editProfileButton click event
//                openEditProfileActivity();
//            }
//        });

//        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // Handle nightModeSwitch state change
//                if (isChecked) {
//                    // Night mode is enabled
//                } else {
//                    // Night mode is disabled
//                }
//            }
//        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK && data != null) {
            // Get the URI of the selected image
            Uri selectedImageUri = data.getData();

            // Use the selected image URI as needed (e.g., display the image in an ImageView)
            profileImage.setImageURI(selectedImageUri);
        }
    }
}
