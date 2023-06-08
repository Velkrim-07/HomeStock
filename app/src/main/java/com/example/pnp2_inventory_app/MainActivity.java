package com.example.pnp2_inventory_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// map and hash for testing
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

//DbConnection package!
import DbConfig.FirebaseConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase!!
        //FirebaseApp.initializeApp(this);

        FirebaseConfig dbActions = new FirebaseConfig();

        // initialize stuff
        dbActions.ConnectDatabase();

        // testing create method
        // cannot be null, make catchers
        Map<String, Object> temp;
        temp = dbActions.CreateItem("Milk", "1l");

        // testing pushToDb method
        // TODO: works fine
        //dbActions.InsertDb(temp);

        // getAll testing
        // TODO: works sometimes for some reason?
        //List<Map<String, Object>> resultList = new ArrayList<>();
        //resultList = dbActions.GetAll();

        // GetByParameter testing
        // TODO: works sometimes for some reason?
        //resultList = new ArrayList<>();
        //resultList = dbActions.GetByParameterValue("Milk", "1l");

        // testing Delete
        // TODO: failure listeners not working
        //dbActions.DeleteFromId("ySWgoTCrn8vj5p1GG16Q");
    }
}