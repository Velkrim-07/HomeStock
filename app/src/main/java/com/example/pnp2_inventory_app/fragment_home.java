package com.example.pnp2_inventory_app;

//Fragment Usage needs

import android.app.Activity;
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
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import DbConfig.FirebaseConfig;
import DbConfig.HouseholdConfig;

public class fragment_home extends Fragment {
    private Context context;
    private View rootView;
    private LinearLayout InsideLinearLayout;
    private LinearLayout VerticalLinearView;
    private AlertDialog dialog;
    private FirebaseConfig db;
    private List<Item> ItemList;
    private OfflineUsage SaveAndReadFromFile;
    private File FileTOUse;
    Activity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //Gets the context for this fragment to be used through the program
        context = getContext();

        //A database connection is made
        db = new FirebaseConfig();
        db.ConnectDatabase();

        main = this.getActivity(); //used so we dont need to get the activity over and over

        SaveAndReadFromFile = new OfflineUsage(main);//initialises the OfflineUsage class
        FileTOUse = new File(this.getActivity().getFilesDir(), "Config.txt"); //creates a file to save data in(Should be using the file in teh program)

        //checks if we are connected to the internet
        if(SaveAndReadFromFile.isNetworkAvailable() == true) {
            GetItemsFromDatabase(); //if we are then we will get the information from the internet
            Toast.makeText(context, "Connected to database.", Toast.LENGTH_SHORT).show();//we give the user the message telling them the internet is connected
        }
        else{
            String stringItems = null;//if we are then we will get the information from the the text file
            try {
                stringItems = SaveAndReadFromFile.getStringFromFile(FileTOUse); //gets the file from the File
                ReadListOfItems(stringItems); //turns the string into a list of items
                Toast.makeText(context, "No connection to database.", Toast.LENGTH_SHORT).show();//we give the user the message telling them the internet is not connected
            } catch (Exception e) {throw new RuntimeException(e);}
        }

        //creates the buttons used in the fragment
        Button_Handler.MakeAddButton(rootView, R.id.ButtonAddItem, this);
        Button_Handler.makeEditButton(rootView, R.id.ButtonEditItem, db, this);
        Button_Handler.MakeDeleteButton(rootView, R.id.ButtonDelete, context, db, this);

        return rootView;
    }

    public void AddToScrollView(Item newItem) {
        newItem.ConstructObject(context);
        VerticalLinearView = rootView.findViewById(R.id.LinearLayoutOutside);
        InsideLinearLayout = new LinearLayout(VerticalLinearView.getContext());
        //Makes the InsideLinearLayout horizontal so our item's attributes are in a line
        InsideLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        //Formatting for the sizes of each individual layout
        ViewGroup.LayoutParams AmountObjectParams = new ViewGroup.LayoutParams(200, 50);
        ViewGroup.LayoutParams NameObjectParams = new ViewGroup.LayoutParams(500, 50);
        ViewGroup.LayoutParams ExpireDateObjectParams = new ViewGroup.LayoutParams(400, 50);

        //Adds the formatting to the objects
        newItem.AmountObject.setLayoutParams(AmountObjectParams);
        newItem.NameObject.setLayoutParams(NameObjectParams);
        newItem.ExpireDateObject.setLayoutParams(ExpireDateObjectParams);
        newItem.ExpireDateObject.setPadding(1, 0, 0, 0);

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

        // Creates the AlertBox objects used to get the information from the user
        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
        EditText editTextItemName = dialogView.findViewById(R.id.editTextItemName);
        CalendarView calendarView = dialogView.findViewById(R.id.calendarView);
        Button buttonAccept = dialogView.findViewById(R.id.buttonAccept);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

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
            // Code to handle the Accept button click event
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

        buttonCancel.setOnClickListener(v -> {
            // Code to handle the Cancel button click event
            dialog.dismiss();
        });

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

        if(SaveAndReadFromFile.isNetworkAvailable()) {
            db.GetAll("InventoryItems", querySnapshot -> {
                Toast.makeText(context, "Connected to database.", Toast.LENGTH_SHORT).show();
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
                for (Item items : ItemList) {
                    AddToScrollView(items); //the items are added to the scrollview
                }
                SaveAndReadFromFile.writeToFile(ItemList, FileTOUse); //writes to the file
            });
        }
        else {
            Toast.makeText(context, "No connection to database.", Toast.LENGTH_SHORT).show();//we give the user the message telling them the internet is not connected
            try {ReadListOfItems(SaveAndReadFromFile.getStringFromFile(FileTOUse));}
            //gets the string from the file and parses the data to get each attribute of the items in the list
            //this will save to the Items list
            catch (Exception e) {throw new RuntimeException(e);}
            for (Item items: ItemList) {
                AddToScrollView(items);//adds each item to the scroll view
                SaveAndReadFromFile.writeToFile(ItemList, FileTOUse); //saves the data to the file
            }
        }
    }

    public void ReadListOfItems(String ReadItem){
        ItemList = new ArrayList<>(); //creates a list of items
        int CaseSetter = 0; //sets a counter for the amount of variables an item
        //creates the strings that hold each attribute of an item
        String Name = "", Id = "", Expiration = "", ItemObject = "", insertdate = "", LastUpdated = ""; int Amount = 0;
        for (char character : ReadItem.toCharArray()) {//loops through the string
            if (character == ',') { //checks for "," if a comman is found we send data to an attribute depending on the attribute we are on
                switch (CaseSetter) {
                    case 0:
                        Id = ItemObject;//sets the Id
                        ItemObject = ""; //resets the Item Object
                        break;
                    case 1:
                        Name = ItemObject;//sets the Name to the string
                        ItemObject = "";//resets the Item Object
                        break;
                    case 2:
                        Amount = Integer.parseInt(ItemObject);//sets the Amount to the string
                        ItemObject = "";//resets the Item Object
                        break;
                    case 3:
                        Expiration = ItemObject;//sets the Expiration date to the string
                        ItemObject = "";//resets the Item Object
                        break;
                    case 4:
                        insertdate = ItemObject; //sets the insert date to the string
                        ItemObject = "";//resets the Item Object
                        break;
                    case 5:
                        LastUpdated = ItemObject;//sets the last update to the string
                        ItemObject = "";//resets the Item Object
                        break;
                }
                CaseSetter++; //increase the CaseSetter so when the next comma is found we update a different attribute
            } else if (character == '\n') {//if a newline is found we are at the end of an object
                Item NewReadItem = new Item(Name, Amount, Expiration, Id); //creates the new item without the insertdate of lastupdate because they are not set in the intialiser
                NewReadItem.lastUpdated = LastUpdated; //last update is set here
                NewReadItem.insertedDate = insertdate;//insert date is set here
                ItemList.add(NewReadItem); //we add the item to the itemlist
                //we reset the attributes for the item
                Expiration = ""; Name = ""; Id = ""; insertdate = ""; LastUpdated = ""; Amount = 0; CaseSetter = 0;
            } else {ItemObject = ItemObject + character;} //if there are no commas or newlines we add the character to the string holding the attribute we are adding to
        }
    }

    public String getFormattedDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public DateCreator GetUnformatedDate(String date){
        String[] NewDate = new String[3];
        NewDate[0] =  ("");NewDate[1] =  ("");NewDate[2] =  ("");
        int[] dates = new int[3];

        for (int i = 0; i < 3; i++){
            for(char character : date.toCharArray()){
                if (character == '-'){ i++;}
                else{NewDate[i] += character;}
            }
        }

        for (int i = 0; i < 3; i++) {
            dates[i] = Integer.parseInt(NewDate[i]);
        }
        DateCreator Expecteddate = new DateCreator(dates[2], dates[1], dates[0]);
        return Expecteddate;
    }
}