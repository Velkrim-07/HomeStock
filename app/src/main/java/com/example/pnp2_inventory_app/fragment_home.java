package com.example.pnp2_inventory_app;

//Fragment Usage needs

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import DbConfig.FirebaseConfig;

public class fragment_home extends Fragment {
    private Context context;
    private View rootView;
    private LinearLayout InsideLinearLayout;
    private LinearLayout VerticalLinearView;
    private AlertDialog dialog;
    private FirebaseConfig db;
    private List<Item> ItemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //Gets the context for this fragment to be used through the program
        context = getContext();

        //A database connection is made
        db = new FirebaseConfig();
        db.ConnectDatabase();

        //We Initially pull from the database and populate the scrollview
        GetItemsFromDatabase();

        Button_Handler.MakeAddButton(rootView, R.id.ButtonAddItem, this);
        Button_Handler.makeEditButton(rootView, R.id.ButtonEditItem);
        Button_Handler.MakeDeleteButton(rootView, R.id.ButtonDelete, context, db, this);

        return rootView;
    }

    private void AddToScrollView(Item newItem) {
        newItem.ConstructObject(context);
        VerticalLinearView = rootView.findViewById(R.id.LinearLayoutOutside);
        InsideLinearLayout = new LinearLayout(VerticalLinearView.getContext());
        //Makes the InsideLinearLayout horizontal so our item's attributes are in a line
        InsideLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        //Formatting for the sizes of each individual layout
        ViewGroup.LayoutParams AmountObjectParams = new ViewGroup.LayoutParams(100, 50);
        ViewGroup.LayoutParams NameObjectParams = new ViewGroup.LayoutParams(400, 50);
        ViewGroup.LayoutParams ExpireDateObjectParams = new ViewGroup.LayoutParams(400, 50);

        //Adds the formatting to the objects
        newItem.AmountObject.setLayoutParams(AmountObjectParams);
        newItem.NameObject.setLayoutParams(NameObjectParams);
        newItem.ExpireDateObject.setLayoutParams(ExpireDateObjectParams);
        newItem.ExpireDateObject.setPadding(140, 0, 0, 0);

        //Adding the TextViews to the InsideLinearLayout view
        InsideLinearLayout.addView(newItem.AmountObject);
        InsideLinearLayout.addView(newItem.NameObject);
        InsideLinearLayout.addView(newItem.ExpireDateObject);

        // Add padding between each item
        int paddingBetweenItems = 0;

        InsideLinearLayout.setPadding(0, paddingBetweenItems, 0, paddingBetweenItems);
        VerticalLinearView.addView(InsideLinearLayout);
    }

    //Creates a AlertBox that prompts the user for an items information
    public void showDialogToAddItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Item");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_item, null);
        builder.setView(dialogView);

        //Creates the AlertBox objects used to get the information from the user
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

            Item NewItem = new Item(itemName, quantity, expirationDate); //creates the object
            db.InsertDb(NewItem); //Insets the new item into the database
            ItemList.add(NewItem);
            AddToScrollView(NewItem); //adds the new Item to the Scroll View

            // Dismiss the dialog after accepting the input
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        dialog = builder.create();
        dialog.show();
    }

    public List<Item> getItemList(){
        return ItemList;
    }

    //Gets the item from the database and adds them to the Scroll view
    public void GetItemsFromDatabase(){
        ItemList = new ArrayList<>(); //creates a list to hold the items we want to get from the database

        //if there is already information displayed when we refresh that information is deleted
        if(InsideLinearLayout != null){
            InsideLinearLayout.removeAllViews(); //the information is removed from the inside view
            VerticalLinearView.removeAllViews(); //the information is removed form the Vertical view
        }

        db.GetAll("InventoryItems", querySnapshot -> {
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                String name = document.getString("name");
                int quantity = Objects.requireNonNull(document.getLong("quantity")).intValue();
                String expirationDate = document.getString("expirationDate");
                String documentId = document.getString("documentId");
                //String insertedDate = document.getString("insertedDate ");
                //String lastUpdated = document.getString("lastUpdated ");
                String insertedDate = db.GetDate();
                String lastUpdated = db.GetDate();

                //creates the item object
                Item item = new Item(name, quantity, expirationDate, documentId);
                item.insertedDate = insertedDate;
                item.lastUpdated = lastUpdated;

                if (item != null) {//placeholder foe new expression
                 //TODO : make sure there are no duplicates
                    ItemList.add(item); //adds the item to the list of items
                }
            }
            for(Item items: ItemList){
                AddToScrollView(items); //the items are added to the scrollview
            }
        });
    }

    private String getFormattedDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}