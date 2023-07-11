package com.example.pnp2_inventory_app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import DbConfig.FirebaseConfig;

public class fragment_delete extends Fragment {

    private Context context;
    private View view;
    private FirebaseConfig db;
    private LinearLayout scrollBarLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.delete_dialog, container, false);
        context = getContext();

        db = new FirebaseConfig();
        db.ConnectDatabase();

        scrollBarLayout = view.findViewById(R.id.deleteCheckedItemScrollBar);
        Button deleteButton = view.findViewById(R.id.buttonAccept);
        Button cancelButton = view.findViewById(R.id.buttonCancel);

        cancelButton.setOnClickListener(v -> getActivity().onBackPressed());



        return view;
    }

    private void loadItemsFromDatabase() {
        List<Item> ItemList = new ArrayList<>(); // Creates a list to hold the items we want to get from the database

        db.GetAll("InventoryItems", querySnapshot -> {
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                String name = document.getString("name");
                int quantity = Objects.requireNonNull(document.getLong("quantity")).intValue();
                String expirationDate = document.getString("expirationDate");
                String documentId = document.getString("documentId");
                String insertedDate = db.GetDate();
                String lastUpdated = db.GetDate();

                Item item = new Item(name, quantity, expirationDate, documentId);
                item.insertedDate = insertedDate;
                item.lastUpdated = lastUpdated;

                if (item != null) {
                    ItemList.add(item);
                }
            }
            for (Item items : ItemList) {
                scrollBarLayout.addView(CreateCheckBoxList(items));
            }
        });
    }

    //this pulls up the Alertbox and adds a button the user fills out the data
    public void AlertBox(Context fragcontext){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(fragcontext); //create an alert-box object
        alertBuilder.create(); //creates the objects to be used
        alertBuilder.setTitle("Delete Items"); //sets the title the user will see
        alertBuilder.setMessage("Select Which Items You Wish To Delete"); //sets the message the user will see
        View dialogView = LayoutInflater.from(fragcontext).inflate(R.layout.delete_dialog, null);
        alertBuilder.setView(dialogView);

        Button buttonAccept = dialogView.findViewById(R.id.buttonAccept);

        LinearLayout AlertBoxLinearLayout = new LinearLayout(alertBuilder.getContext());
        loadItemsFromDatabase();

        buttonAccept.setOnClickListener(v -> {
            deleteSelectedItems();
        });

        alertBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        alertBuilder.show(); //shows the alert box
    }

    public LinearLayout CreateCheckBoxList(Item NewItem){
        LinearLayout CheckBoxItemList = new LinearLayout(context);
        CheckBoxItemList.setOrientation(LinearLayout.HORIZONTAL);

        ViewGroup.LayoutParams AmountObjectParams = new ViewGroup.LayoutParams(100, 50);
        TextView Amount = new TextView(CheckBoxItemList.getContext());
        Amount.setText(String.valueOf(NewItem.getQuantity()));
        Amount.setLayoutParams(AmountObjectParams);

        ViewGroup.LayoutParams NameObjectParams = new ViewGroup.LayoutParams(600, 50);
        TextView NameCheckBox = new TextView(CheckBoxItemList.getContext());
        NameCheckBox.setText(NewItem.getName());
        NameCheckBox.setLayoutParams(NameObjectParams);

        ViewGroup.LayoutParams CheckBoxParams = new ViewGroup.LayoutParams(75, 50);
        CheckBox itemCheckbox = new CheckBox(CheckBoxItemList.getContext());
        itemCheckbox.setLayoutParams(CheckBoxParams);

        //Adding the TextViews to the InsideLinearLayout view
        CheckBoxItemList.addView(Amount);
        CheckBoxItemList.addView(NameCheckBox);
        CheckBoxItemList.addView(itemCheckbox);

        return CheckBoxItemList;
    }

    private void deleteSelectedItems() {
        int childCount = scrollBarLayout.getChildCount();
        List<Item> itemsToDelete = new ArrayList<>();

        for (int i = 0; i < childCount; i++) {
            LinearLayout checkBoxList = (LinearLayout) scrollBarLayout.getChildAt(i);
            CheckBox itemCheckbox = (CheckBox) checkBoxList.getChildAt(2);

            if (itemCheckbox.isChecked()) {
                TextView amount = (TextView) checkBoxList.getChildAt(0);
                TextView name = (TextView) checkBoxList.getChildAt(1);

                int quantity = Integer.parseInt(amount.getText().toString());
                String itemName = name.getText().toString();

                Item item = new Item(itemName, quantity, null, null);
                itemsToDelete.add(item);
            }
        }

        // Perform the deletion of items here using the itemsToDelete list

        // For demonstration purposes, let's just print the names of selected items
        for (Item item : itemsToDelete) {
            System.out.println("Deleting item: " + item.getName());
        }
    }
}