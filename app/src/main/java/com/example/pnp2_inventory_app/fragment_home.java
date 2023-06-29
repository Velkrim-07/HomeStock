package com.example.pnp2_inventory_app;

import android.app.AlertDialog;
import android.graphics.Color;
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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.Context;

//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;

// DbStuff for testing
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

//import org.w3c.dom.Text;

import org.w3c.dom.Text;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import DbConfig.FirebaseConfig;

//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Map;

//import java.util.ArrayList;
//import java.util.List;
import java.util.Locale;

public class fragment_home extends Fragment {

    private Button buttonEditItem;
    private AlertDialog dialog; // Declare the dialog as a member variable
    // Rafael Testing, ignore this
    private Button addItem;
    private TextView items;
    private Context context;
    private  View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();


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

                textViewQuantity.setTextColor(Color.BLACK);
                textViewName.setTextColor(Color.BLACK);
                textViewExpirationDate.setTextColor(Color.BLACK);

                return convertView;
            }
        };

        /*
        // Set the adapter to the ListView
        ListView listViewItems = rootView.findViewById(R.id.listViewItems);
        listViewItems.setAdapter(adapter);
*/
        // Find the "Edit" button and set its initial visibility

        buttonEditItem = rootView.findViewById(R.id.ButtonEditItem);
        buttonEditItem.setVisibility(View.VISIBLE); // Set the visibility to always be visible

        ImageButton buttonAddItem = rootView.findViewById(R.id.ButtonAddItem);
        buttonAddItem.setOnClickListener(v -> {
            showDialogToAddItem(adapter);
            AddToScrollView();//needs to go in alertView
        });

        rootView.setOnClickListener(v -> buttonEditItem.setVisibility(View.GONE));

        return rootView;
    }


//Creates the new item and adds it to the database/ item array
    private Item Makeitem(String itemName, int amount, int ExpireDay,int ExpireMonth, int ExpireYear){
        String ExpireDate = ExpireYear + "-" + ExpireMonth+ "-"+ ExpireDay;
        Item NewItem = new Item(itemName, amount, ExpireDate);
        return NewItem;
    }

    private void AddToScrollView(){
        ItemObject NewItemObject = CreateItemObject();
        LinearLayout VerticalLinearView = rootView.findViewById(R.id.LinearLayoutOutside);
        LinearLayout InsideLinearLayout = new LinearLayout(VerticalLinearView.getContext());
        InsideLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        NewItemObject.AmountObject.setPadding(0,0,20,0);
        InsideLinearLayout.addView(NewItemObject.AmountObject);
        InsideLinearLayout.addView(NewItemObject.NameObject);
        NewItemObject.ExpireDateObject.setPadding(600,0,0,0);
        InsideLinearLayout.addView(NewItemObject.ExpireDateObject);

        VerticalLinearView.addView(InsideLinearLayout); //adds the objects to the scrollView
    }

    private ItemObject CreateItemObject(){

        //AlertBox Added Here

        Item NewItem = Makeitem("a", 2, 2, 2, 2); //to be done
        ItemObject NewItemObject = new ItemObject(NewItem, context);
        return NewItemObject;
    }

    // Testing the usage of getAll with the new callback function
    // will leave this here for future reference so we can take a look when we start to implement functions
    public void testingThisShit(View view) {
        FirebaseConfig dbActions;
        dbActions = new FirebaseConfig();
        dbActions.ConnectDatabase();
        
        
    private void showDialogToAddItem(ArrayAdapter<Item> adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Item");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_item, null);
        builder.setView(dialogView);

        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
        EditText editTextItemName = dialogView.findViewById(R.id.editTextItemName);
        CalendarView calendarView = dialogView.findViewById(R.id.calendarView);
        Button buttonAccept = dialogView.findViewById(R.id.buttonAccept);

        // Variable to store the selected date
        final Calendar selectedDate = Calendar.getInstance();

        // Set the initial selected date
        selectedDate.setTimeInMillis(calendarView.getDate());

        // Set the OnDateChangeListener to update the selected date
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, month);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        });

        buttonAccept.setOnClickListener(v -> {
            int quantity = Integer.parseInt(editTextQuantity.getText().toString());
            String itemName = editTextItemName.getText().toString();
            String expirationDate = getFormattedDate(selectedDate);


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

            Item newItem = new Item(itemName, quantity, expirationDate);
            adapter.add(newItem);

            // Dismiss the dialog after accepting the input
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });

        dialog = builder.create();
        dialog.show();
    }

    private String getFormattedDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}