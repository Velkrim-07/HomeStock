package com.example.pnp2_inventory_app;

//import android.content.ClipData;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;

//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;

// DbStuff for testing
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

//import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DbConfig.FirebaseConfig;

//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Map;

//import java.util.ArrayList;
//import java.util.List;

public class fragment_home extends Fragment {
    private Button buttonEditItem;

    // Rafael Testing, ignore this
    private Button addItem;
    private TextView items;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Create a sample list of items
        List<Item> itemList = new ArrayList<>();
        Item item = new Item("Corn", 5, "2023-06-30");
        itemList.add(item);

        // Create a custom adapter for the ListView
        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(getContext(), R.layout.list_item_layout, itemList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Check if an existing view is being reused, otherwise inflate the view
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
                }

                // Get the item at the specified position
                Item item = getItem(position);

                // Set the quantity, name, and expiration date in the item layout
                TextView textViewQuantity = convertView.findViewById(R.id.textViewQuantity);
                TextView textViewName = convertView.findViewById(R.id.textViewName);
                TextView textViewExpirationDate = convertView.findViewById(R.id.textViewExpirationDate);

                textViewQuantity.setText(String.valueOf(item.getQuantity()));
                textViewName.setText(item.getName());
                textViewExpirationDate.setText(item.getExpirationDate());

                return convertView;
            }
        };

        // Set the adapter to the ListView
        ListView listViewItems = rootView.findViewById(R.id.listViewItems);
        listViewItems.setAdapter(adapter);

        // Find the "Edit" button and set its initial visibility
        buttonEditItem = rootView.findViewById(R.id.ButtonEditItem);

        // Find the "Add" button and set its click listener
        ImageButton buttonAddItem = rootView.findViewById(R.id.ButtonAddItem);
        buttonAddItem.setOnClickListener(v -> {
            // Toggle the visibility of the "Edit" button
            if (buttonEditItem.getVisibility() == View.VISIBLE) {
                buttonEditItem.setVisibility(View.GONE);
            } else {
                buttonEditItem.setVisibility(View.VISIBLE);
            }
        });

        // Add OnClickListener to hide the "Edit" button when the user clicks anywhere on the screen
        rootView.setOnClickListener(v -> buttonEditItem.setVisibility(View.GONE));

        return rootView;
    }

    // Testing the usage of getAll with the new callback function
    // will leave this here for future reference so we can take a look when we start to implement functions
    public void testingThisShit(View view) {
        FirebaseConfig dbActions;
        dbActions = new FirebaseConfig();
        dbActions.ConnectDatabase();

        //scrollView testing
        //items = view.findViewById(R.id.textView2);

        addItem = view.findViewById(R.id.ButtonAddItem);

        // for testing, deprecated
        //items = view.findViewById(R.id.textView);

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
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String json = document.getData().toString();
                            List<String> test = new ArrayList<>();
                            test.add(json);

                            // testing but deprecated
                            //items.setText(json);
                        }
                    }
                });
                //items.setText(temp.toString());
                // test.

            }
        });
    }
}