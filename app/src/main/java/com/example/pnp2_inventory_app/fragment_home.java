package com.example.pnp2_inventory_app;

//Fragment Usage needs
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

//Objects
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.app.AlertDialog;

// DbStuff for testing
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import DbConfig.FirebaseConfig;
import java.util.Locale;
import java.util.Objects;

import android.widget.RelativeLayout;
import android.widget.ImageView;

//Contains all the functions for the fragment home
//Function List
//AddToScrollView // Takes in an item// returns void// Adds an item object to the scroll view. The item object creation is handled by the CreateItemObject function
//CreateItemObject // Takes in an item and the context// returns the item object // creates an item object
//showDialogToAddItem// Takes in an List<Item> // return void// Creates a dialog button that gets an item's attributes from the user and uses the AddToScrollView function to display the data to the user
//GetItemsFromDatabase// Takes in noting// returns void// Gets information from the database and populates the Layout views. If the view is filled the function will update the view
//getFormattedDate// Takes in a Calendar // return a string // Takes a data and formats that into a string
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class fragment_home extends Fragment {
    private Context context;
    private View rootView;
    private LinearLayout InsideLinearLayout;
    private LinearLayout VerticalLinearView;
    private Button buttonEditItem;
    private AlertDialog dialog;
    private FirebaseConfig db;
    private List<Item> ItemList;
    private GestureDetector gestureDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
        db = new FirebaseConfig();
        db.ConnectDatabase();
        GetItemsFromDatabase();
        buttonEditItem = rootView.findViewById(R.id.ButtonEditItem);
        buttonEditItem.setVisibility(View.VISIBLE);
        ImageButton buttonAddItem = rootView.findViewById(R.id.ButtonAddItem);
        buttonAddItem.setOnClickListener(v -> showDialogToAddItem());
        rootView.setOnClickListener(v -> buttonEditItem.setVisibility(View.GONE));

        gestureDetector = new GestureDetector(context, new SwipeGestureListener());
        rootView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        return rootView;
    }

    private void AddToScrollView(Item newItem) {
        newItem.ConstructObject(context);
        VerticalLinearView = rootView.findViewById(R.id.LinearLayoutOutside);
        InsideLinearLayout = new LinearLayout(VerticalLinearView.getContext());
        InsideLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams AmountObjectParams = new ViewGroup.LayoutParams(100, 50);
        ViewGroup.LayoutParams NameObjectParams = new ViewGroup.LayoutParams(400, 50);
        ViewGroup.LayoutParams ExpireDateObjectParams = new ViewGroup.LayoutParams(400, 50);
        newItem.AmountObject.setLayoutParams(AmountObjectParams);
        newItem.NameObject.setLayoutParams(NameObjectParams);
        newItem.ExpireDateObject.setLayoutParams(ExpireDateObjectParams);
        newItem.ExpireDateObject.setPadding(200, 0, 0, 0);
        InsideLinearLayout.addView(newItem.AmountObject);
        InsideLinearLayout.addView(newItem.NameObject);
        InsideLinearLayout.addView(newItem.ExpireDateObject);
        VerticalLinearView.addView(InsideLinearLayout);
    }

    //Creates a AlertBox that prompts the user for an items information
    private void showDialogToAddItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Item");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_item, null);
        builder.setView(dialogView);

        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
        EditText editTextItemName = dialogView.findViewById(R.id.editTextItemName);
        CalendarView calendarView = dialogView.findViewById(R.id.calendarView);
        Button buttonAccept = dialogView.findViewById(R.id.buttonAccept);

        final Calendar selectedDate = Calendar.getInstance();

        selectedDate.setTimeInMillis(calendarView.getDate());

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
            AddToScrollView(NewItem); //adds the new Item to the Scroll View
            db.InsertDb(NewItem); //Insets the new item into the database

            // Dismiss the dialog after accepting the input
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        dialog = builder.create();
        dialog.show();
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

    public void testingThisShit(View view) {
        //scrollView testing
        //items = view.findViewById(R.id.textView2);

        Button addItem = view.findViewById(R.id.ButtonAddItem);

        // for testing, deprecated
        //items = view.findViewById(R.id.textView);
        addItem.setOnClickListener(v -> {
            //Item newItem = new Item("MilkTest", 1, "12/12/12", dbActions.GetDate(), dbActions.GetDate());
            //dbActions.testingItemAdd(newItem);

            // it takes a bit of time for the CloudStore to return the data its getting.
            // using a callback interface (which is configured and declared inside FirebaseConfig,
            // this will return to the function when the call returns something!
            // currently transforming to json
            // TODO: figure if we want json or just convert into item class
            db.GetAll("InventoryItems", new FirebaseConfig.FirestoreCallback() {
                @Override
                public void OnCallBack(QuerySnapshot querySnapshot) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String json = document.getData().toString();
                        List<String> test = new ArrayList<>();
                        test.add(json);
                    }
                }
            });
        });
    }

    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        // Right swipe (not used in this example)
                    } else {
                        // Perform left swipe action here
                        // For example, display the desired drawables and update the item's view
                        int childCount = VerticalLinearView.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            LinearLayout childLayout = (LinearLayout) VerticalLinearView.getChildAt(i);
                            TextView childView = (TextView) childLayout.getChildAt(0);
                            // Update the child view with the desired drawables
                            childView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24, 0, R.drawable.baseline_delete_24, 0);
                        }
                    }
                    result = true;
                }
            }

            return result;
        }}
}