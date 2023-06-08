package com.example.pnp2_inventory_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;

import android.view.MenuItem;


import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout; //this is need to create the navigation bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This is the start of the tool bar code
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar); //sets up the toolbar for the navigation menu as a object in this files
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout); //sets up the drawerlayout for the navigation menu in this file
        NavigationView navigationView = findViewById(R.id.nav_view); //set the navigation menu to what is set up in the menu/nav_view
        navigationView.setNavigationItemSelectedListener(this);//sets the listener to this

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav,R.string.close_nav); //creates the action bar according to the all the other files created for the naigation screen
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { //should switch the screen between the different fragments
        int itemId = item.getItemId();//gets the id of the item/ fragment the user is currently seeing
        //would love to use a switch but it does not work
        if(itemId == R.id.nav_home) { // id of the home menu
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit();
        }
        else if(itemId == R.id.nav_settings) {//id of the settings menu
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragement_settings()).commit();
        }
        else if(itemId == R.id.nav_category) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_categories()).commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() { //Opens and closes the nav bar
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //This is the End of the tool bar code
}