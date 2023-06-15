package com.example.pnp2_inventory_app;


import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class cameraClass {
    private Navigation navigation;
    private Activity mainActivty;
    androidx.fragment.app.FragmentActivity fragmentActivity;
    private static final int REQUEST_IMAGE_CAPTURE = 1; //this allows us to use the camera

    cameraClass(Navigation navigation, Activity mainActivty, androidx.fragment.app.FragmentActivity fragmentActivity){
        this.navigation = navigation;
        this.mainActivty = mainActivty;
        this.fragmentActivity = fragmentActivity;
    }

    @SuppressWarnings("deprecation")
    public void dispatchTakePictureIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //this changes the screen to the camera
        mainActivty.startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE); //this start the camera activity
    }

    public void RetakePictureHandler(View fragmentView){
        Button RetakePicture = fragmentView.findViewById(R.id.RetakePicture);
        //this will start the camera activity again
        RetakePicture.setOnClickListener(v -> dispatchTakePictureIntent());
    }

    public void AcceptPictureHandler(View fragmentView){
        Button AcceptPicture = fragmentView.findViewById(R.id.AcceptPicture);
        AcceptPicture.setOnClickListener(v -> {
            //TODO
            //create barcode Scanner
            //for now this will go back to home
            fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit(); //Sets the screen to home if nothing is displayed
            navigation.GetNavigationBar().setCheckedItem(R.id.nav_home); //sets the navigation bar to having home selected
        });
    }

    public void OnActivityHelper(int requestCode, int resultCode, Intent data, Fragment fragment){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) { //if the picture is accepted then the data will be used to create the bitmap
            // Picture captured successfully
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            fragment = fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null) {
                //this allows us to use the buttons from the fragment in main
                View fragmentView = fragment.getView(); // Access the views in the fragment
                assert fragmentView != null;
                ImageView imageView = fragmentView.findViewById(R.id.ImageViewPicture); //this grabs the button using the view and the Id from the XML
                imageView.setImageBitmap(imageBitmap); //this sets the image to the new Bitmap
                RetakePictureHandler(fragmentView); //allows the user to retake the image
                AcceptPictureHandler(fragmentView); //for now returns to home
            }
        }//if cancelled can come later
    }
}
