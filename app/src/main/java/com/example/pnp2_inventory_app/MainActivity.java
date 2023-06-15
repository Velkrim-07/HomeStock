package com.example.pnp2_inventory_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1; //this allows us to use the camera
    private Navigation navigation; //navigation object is created so we can access the navigation throughout the file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //creates the instance of the program
        setContentView(R.layout.activity_main); //sets the current view to the activity

        ImageButton ImgBtnCamera = findViewById(R.id.ImgBtnCam); //this is the camera button on the navigation bar
        ImgBtnCamera.setOnClickListener(v -> {
            dispatchTakePictureIntent(); //this changes the intent to a camera and gets the bitmap of the pictue
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_camera()).commit(); //this changes the screen to the fragment
        });

        //this is the start of the implementation of the navigation bar
        //the code for the navigation bar is in the Navigation java file
        //The constructor takes in the context from MainActivity(this)
        //The NavigationCreate takes in the mainActivity as the Activity(this)
        navigation = new Navigation(this);//initialises the Navigation object
        navigation.NavigationCreate(this); //if the context is not null then it will be used for the navigation bar
        if (savedInstanceState == null) {//if the saved Instance(basically the programs screen) is not active we set the home screen to the fragment being shown
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit(); //Sets the screen to home if nothing is displayed
            navigation.GetNavigationBar().setCheckedItem(R.id.nav_home); //sets the navigation bar to having home selected
        }//this is the end of the navigation bar implementation
    }

    @SuppressWarnings("deprecation")
    public void dispatchTakePictureIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //this changes the screen to the camera
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE); //this start the camera activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) { //if the picture is accepted then the data will be used to create the bitmpa
            // Picture captured successfully
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
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

    private void RetakePictureHandler(View fragmentView){
        Button RetakePicture = fragmentView.findViewById(R.id.RetakePicture);
        //this will start the camera activity again
        RetakePicture.setOnClickListener(v -> dispatchTakePictureIntent());
    }

    private void AcceptPictureHandler(View fragmentView){
        Button AcceptPicture = fragmentView.findViewById(R.id.AcceptPicture);
        AcceptPicture.setOnClickListener(v -> {
            //TODO
            //create barcode Scanner
            //for now this will go back to home
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit(); //Sets the screen to home if nothing is displayed
            navigation.GetNavigationBar().setCheckedItem(R.id.nav_home); //sets the navigation bar to having home selected
        });
    }
}