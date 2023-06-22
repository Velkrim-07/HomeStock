package com.example.pnp2_inventory_app;

import android.os.Bundle;

//Button
import android.widget.Button;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

// DbStuff for testing
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DbConfig.FirebaseConfig;

public class fragment_home extends Fragment {

    // for Rafael's testing
    public Button addItem;
    public TextView items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        testingThisShit(view);

        // Inflate the layout for this fragment
        return view;
    }

    // Testing the usage of getAll with the new callback function
    // will leave this here for future reference so we can take a look when we start to implement functions
    public void testingThisShit(View view){
        FirebaseConfig dbActions;
        dbActions = new FirebaseConfig();
        dbActions.ConnectDatabase();

        //scrollView testing
        //items = view.findViewById(R.id.textView2);

        addItem = view.findViewById(R.id.ButtonAddItem);
        items = view.findViewById(R.id.textView);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String, Object>> temp;


                // it takes a bit of time for the Cloudstore to return the data its getting.
                // using a callback interface (which is configured and declared inside FirebaseConfig,
                // this will return to the function when the call returns something!
                // currently trasnforming to json
                // TODO: figure if we want json or just convert into item class
                temp = dbActions.GetAll("InventoryItems", new FirebaseConfig.FirestoreCallback() {
                    @Override
                    public void OnCallBack(QuerySnapshot querySnapshot) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()){
                            String json = document.getData().toString();
                            List<String> test = new ArrayList<>();
                            test.add(json);
                            items.setText(json);
                        }
                    }
                });
                //items.setText(temp.toString());


            }
        });
    }
}