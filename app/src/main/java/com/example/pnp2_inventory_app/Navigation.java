package com.example.pnp2_inventory_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Context;

public class Navigation implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout; //this is need to create the navigation bar
    private NavigationView navigationView; //creates an object for Navigation view so it can be passed to main(GetNavigationBar)
    private Context context; //creates a Context object
    FirebaseAuth mAuth;

    public Navigation(Context context){//this is the constructor for the Navigation.
        this.context = context; //the context for the Navigation is set here
    }
    //NavigationCreate creates the  Navigation bar for the activity
    //it takes in the mainActivity as a parameter
    public void NavigationCreate(Activity mainActivity) {
            //This is the start of the tool bar code
            androidx.appcompat.widget.Toolbar toolbar = mainActivity.findViewById(R.id.toolbar); //sets up the toolbar for the navigation menu as a object in this files
            ((AppCompatActivity) mainActivity).setSupportActionBar(toolbar);//sets the toolbar created in the activity_main.xml as the Action bar fpr the program

            drawerLayout = mainActivity.findViewById(R.id.drawer_layout); //Initialises up the DrawerLayout for the navigation menu in this file

            navigationView = mainActivity.findViewById(R.id.nav_view); //set the navigation menu to what is set up in the menu/nav_view
            navigationView.setNavigationItemSelectedListener(this);//sets the listener to this

            //creates the action bar according to the all the other files created for the navigation screen
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(mainActivity, drawerLayout, toolbar, R.string.open_nav,R.string.close_nav);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { //should switch the screen between the different fragments
        int itemId = item.getItemId();//gets the id of the item/ fragment the user is currently seeing
        //would love to use a switch but it does not work
        if(itemId == R.id.nav_home) { // id of the home menu
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit();
        }
        else if(itemId == R.id.nav_settings) {//id of the settings menu
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_Settings()).commit();
        }
        else if(itemId == R.id.nav_category) {
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_categories()).commit();
        }
        else if (itemId == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent((AppCompatActivity)context, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                ((AppCompatActivity) context).finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() { //Opens and closes the nav bar(I think Im not too sure)
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            ((AppCompatActivity) context).onBackPressed();
        }
    }

    public NavigationView GetNavigationBar(){
        return navigationView; //sends the navigation bar to the MainActivity
    }
}
