package com.example.pnp2_inventory_app;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import DbConfig.FirebaseConfig;

public class dialog_Edit extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_edit, container, false);
    }

    //this pulls up the AlertBox and adds a button the user fills out the data
    public void AlertBox(FirebaseConfig db, Context fragContext, fragment_home fragmentHome) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(fragContext);
        alertBuilder.setTitle("Edit Items");
        alertBuilder.setMessage("Select the item you wish to edit");

        View dialogView = LayoutInflater.from(fragContext).inflate(R.layout.dialog_edit, null);
        alertBuilder.setView(dialogView);

        ScrollView deleteViewScrollBar = dialogView.findViewById(R.id.EditCheckedItemScrollBar);
        deleteViewScrollBar.addView(loadItemsFromDatabase(fragContext, fragmentHome));

        AlertDialog dialogAlert = alertBuilder.create();

        Button buttonAcceptEdit = dialogView.findViewById(R.id.buttonAcceptEdit);
        buttonAcceptEdit.setOnClickListener(v -> {
            EditSelectedItems(db, (LinearLayout) deleteViewScrollBar.getChildAt(0), fragmentHome, fragContext);
            dialogAlert.dismiss();
        });

        Button buttonCancelEdit = dialogView.findViewById(R.id.buttonCancelEdit);
        buttonCancelEdit.setOnClickListener(v -> dialogAlert.dismiss());

        dialogAlert.show();
    }


    private LinearLayout loadItemsFromDatabase(Context fragContext, fragment_home fragmentHome) {
        LinearLayout scrollBarLayout = new LinearLayout(fragContext);
        scrollBarLayout.setOrientation(LinearLayout.VERTICAL);

        for (Item items : fragmentHome.getItemList()) {
            scrollBarLayout.addView(CreateCheckBoxList(items, fragContext));
        }

        return scrollBarLayout;
    }

    public LinearLayout CreateCheckBoxList(Item NewItem, Context context){
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

    private void EditSelectedItems(FirebaseConfig db, LinearLayout scrollBarLayout, fragment_home fragmentHome, Context fragContext) {
        int childCount = scrollBarLayout.getChildCount();

        class VerificationItem { //used to store the name and amount of item to check if it was selected to be deleted
            public String CheckedAmount;
            public String CheckedName;
        }

        List<VerificationItem> itemsToDelete = new ArrayList<>(); //items to delete

        for (int i = 0; i < childCount; i++) {
            LinearLayout checkBoxList = (LinearLayout) scrollBarLayout.getChildAt(i);
            CheckBox itemCheckbox = (CheckBox) checkBoxList.getChildAt(2);
            if (itemCheckbox.isChecked()) {
                VerificationItem VerifiedItem = new VerificationItem(); // creates a new VerificationItem to store the name and amount of items selected
                TextView textAmount = (TextView) checkBoxList.getChildAt(0); //takes the child from the Linearlayout and makes it into a textView
                TextView textName = (TextView) checkBoxList.getChildAt(1);
                VerifiedItem.CheckedAmount = textAmount.getText().toString();//Takes the textView and retrieves the Amount and name
                VerifiedItem.CheckedName = textName.getText().toString();
                itemsToDelete.add(VerifiedItem); //adds that item to a list to be deleted
            }
        }

        for (VerificationItem VerificationItems : itemsToDelete) {
            for (Item items : fragmentHome.getItemList()) {
                //if the name of the object and the amount of the object is the same we delete the object
                if (VerificationItems.CheckedAmount.equals(String.valueOf(items.m_Quantity)) && VerificationItems.CheckedName.equals(items.m_Name)) {
                    showDialogToAddItem(items, db, fragmentHome, fragContext);
                    db.DeleteFromId(items.documentId); //deletes item or items from database
                    int itemsDeleted = 0; itemsDeleted++; // an int is used to keep check of the amount of items being deleted
                    if(itemsDeleted == itemsToDelete.size()){//if the items deleted is equal to the amount of items in the delete list we break
                        break;
                    }
                }
            }
        }
        fragmentHome.GetItemsFromDatabase(); // refreshes home
    }

    public void showDialogToAddItem(Item EditItem, FirebaseConfig db, fragment_home fragmentHome, Context fragContext) {;
        List<Item> ItemList = fragmentHome.getItemList();
        AlertDialog.Builder builder = new AlertDialog.Builder(fragContext);
        builder.setTitle("Add Item");

        View dialogView = LayoutInflater.from(fragContext).inflate(R.layout.dialog_add_item, null);
        builder.setView(dialogView);
        AlertDialog dialogAlert = builder.create();

        //Creates the AlertBox objects used to get the information from the user
        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
        editTextQuantity.setText(String.valueOf(EditItem.getQuantity()));
        EditText editTextItemName = dialogView.findViewById(R.id.editTextItemName);
        editTextItemName.setText(EditItem.getName());
        CalendarView calendarView = dialogView.findViewById(R.id.calendarView);
        //calendarView.setDate(Long.getLong(fragmentHome.GetUnformatedDate(EditItem.getExpirationDate())));
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

        Button AcceptEdit = dialogView.findViewById(R.id.buttonAccept);

        AcceptEdit.setOnClickListener(v -> {
            int quantity = Integer.parseInt(editTextQuantity.getText().toString());
            String itemName = editTextItemName.getText().toString();
            String expirationDate = fragmentHome.getFormattedDate(selectedDate);

            Item NewItem = new Item(itemName, quantity, expirationDate); //creates the object
            db.InsertDb(NewItem); //Insets the new item into the database
            ItemList.add(NewItem);
            fragmentHome.AddToScrollView(NewItem); //adds the new Item to the Scroll View
            dialogAlert.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        dialogAlert.show();
    }
}
