package com.example.pnp2_inventory_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment;
    private Navigation navigation; //navigation object is created so we can access the navigation throughout the file
    private cameraClass cameraClass; //cameraClass object allows for the camera to be called from main

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //creates the instance of the program
        setContentView(R.layout.activity_main); //sets the current view to the activity

        cameraClass = new cameraClass(navigation,this,this);
        navigation = new Navigation(this);//initialises the Navigation object
        ImageButton ImgBtnCamera = findViewById(R.id.ImgBtnCam); //this is the camera button on the navigation bar

        ImgBtnCamera.setOnClickListener(v -> {
            cameraClass.dispatchTakePictureIntent(); //this changes the intent to a camera and gets the bitmap of the picture
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_camera()).commit(); //this changes the screen to the fragment
        });

        //this is the start of the implementation of the navigation bar. the code for the navigation bar is in the Navigation java file
        //The constructor takes in the context from MainActivity(this). The NavigationCreate takes in the mainActivity as the Activity(this)

        navigation.NavigationCreate(this); //if the context is not null then it will be used for the navigation bar
        if (savedInstanceState == null) {//if the saved Instance(basically the programs screen) is not active we set the home screen to the fragment being shown
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit(); //Sets the screen to home if nothing is displayed
            navigation.GetNavigationBar().setCheckedItem(R.id.nav_home); //sets the navigation bar to having home selected
        }//this is the end of the navigation bar implementation
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cameraClass.OnActivtyHelper(requestCode, resultCode, data, fragment);
    }
}