package com.example.pnp2_inventory_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //creates the instance of the program
        setContentView(R.layout.activity_main); //sets the current view to the activity

        //this is the start of the implementation of the navigation bar
        //the code for the navigation bar is in the Navigation java file
        //The constructor takes in the context from MainActivity(this)
        //The NavigationCreate takes in the mainActivity as the Activity(this)
        Navigation navigation = new Navigation(this);//initialises the Navigation object
        navigation.NavigationCreate(this); //if the context is not null then it will be used for the navigation bar
        if (savedInstanceState == null) {//if the saved Instance(basically the programs screen) is not active we set the home screen to the fragment being shown
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit(); //Sets the screen to home if nothing is displayed
            navigation.GetNavigationBar().setCheckedItem(R.id.nav_home); //sets the navigation bar to having home selected
        }//this is the end of the navigation bar implementation
    }
}