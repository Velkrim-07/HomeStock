package com.example.pnp2_inventory_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// map and hash for testing
import java.util.Map;
import java.util.HashMap;

//DbConnection package!
import DbConfig.FirebaseConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseConfig dbActions = new FirebaseConfig();

        // initialize stuff
        dbActions.ConnectDatabase();

        // testing create method
        // cannot be null, make catchers
        Map<String, Object> temp;
        temp = dbActions.CreateItem("Fucking Milk", "Expired as fuck broski");

        // testing pushToDb method
        dbActions.InsertDb(temp);
    }
}