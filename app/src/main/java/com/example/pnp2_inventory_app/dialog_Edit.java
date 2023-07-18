package com.example.pnp2_inventory_app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DbConfig.FirebaseConfig;

public class dialog_Edit extends Fragment {
    public Context fragContext;
    public fragment_home fragmentHome;
    public FirebaseConfig db;
    public ScrollBarLayoutBox ScrollHandler;
    List<Item> itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_edit, container, false);
    }

    //this pulls up the AlertBox and adds a button the user fills out the data
    public void AlertBox(FirebaseConfig Db, fragment_home fragmenthome){
        fragContext = fragmenthome.getContext();
        fragmentHome = fragmenthome;
        db = Db;
        ScrollHandler = new ScrollBarLayoutBox();
        itemList = fragmenthome.getItemList();

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(fragContext); //create an alert-box object
        alertBuilder.create(); //creates the objects to be used
        alertBuilder.setTitle("Edit Items"); //sets the title the user will see
        alertBuilder.setMessage("Select the item You Wish To Edit"); //sets the message the user will see

        View dialogView = LayoutInflater.from(fragContext).inflate(R.layout.dialog_edit, null);
        alertBuilder.setView(dialogView);

        ScrollHandler.scrollview = dialogView.findViewById(R.id.EditCheckedItemScrollBar); //connect the scrollbar to the xml
        ScrollHandler.scrollview.addView(loadItemsFromDatabase()); //load up the items for the database inside the scrollbar

        AlertDialog dialogAlert = alertBuilder.create();

        Button buttonAcceptEdit = dialogView.findViewById(R.id.buttonAcceptEdit);
        buttonAcceptEdit.setOnClickListener(v -> {
            dialogAlert.dismiss();
        });

        Button buttonCancelEdit = dialogView.findViewById(R.id.buttonCancelEdit);
        buttonCancelEdit.setOnClickListener(v -> dialogAlert.dismiss());

        dialogAlert.show();
    }


    private LinearLayout loadItemsFromDatabase() {
        LinearLayout scrollBarLayout = new LinearLayout(fragContext);
        scrollBarLayout.setOrientation(LinearLayout.VERTICAL);

        for (Item items : itemList) {
            scrollBarLayout.addView(CreateCheckBoxList(items));
        }
        return scrollBarLayout;
    }

    public LinearLayout CreateCheckBoxList(Item NewItem){
        ScrollHandler.MakeLayoutBox(NewItem, fragContext, fragmentHome, this);
        ScrollHandler.LayoutView.setOnClickListener(v -> {
            showDialogToEditItem(NewItem);
            ScrollHandler.AddToLayoutList(NewItem);
            fragmentHome.GetItemsFromDatabase(); // refreshes home
        });
        return ScrollHandler.LayoutView;
    }

    public void showDialogToEditItem(Item EditItem) {
        List<Item> ItemList = fragmentHome.getItemList();
        AlertDialog.Builder builder = new AlertDialog.Builder(fragContext);
        builder.setTitle("Edit Item");

        View dialogView = LayoutInflater.from(fragContext).inflate(R.layout.dialog_add_item, null);
        builder.setView(dialogView);
        AlertDialog dialogAlert = builder.create();

        //Creates the AlertBox objects used to get the information from the user
        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
        editTextQuantity.setText(String.valueOf(EditItem.getQuantity()));
        EditText editTextItemName = dialogView.findViewById(R.id.editTextItemName);
        editTextItemName.setText(EditItem.getName());
        CalendarView calendarView = dialogView.findViewById(R.id.calendarView);
        fragment_home.DateCreator Date = fragmentHome.GetUnformatedDate(EditItem.getExpirationDate());
        Calendar CalInstance = Calendar.getInstance();
        CalInstance.set(Date.m_year, Date.m_month, Date.m_day);

        // Variable to store the selected date
        final Calendar selectedDate = Calendar.getInstance();
        // Set the OnDateChangeListener to update the selected date
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, month);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        });

        Button AcceptEdit = dialogView.findViewById(R.id.buttonAccept);
        AcceptEdit.setOnClickListener(v -> {
            EditItem.m_Quantity = Integer.parseInt(editTextQuantity.getText().toString());
            EditItem.m_Name = editTextItemName.getText().toString();
            EditItem.m_ExpirationDate = fragmentHome.getFormattedDate(selectedDate);
            for (Item items: ItemList) {
                if (items.documentId == EditItem.documentId){
                    items = EditItem;
                    db.UpdateFromId(EditItem.documentId ,EditItem); //Insets the new item into the database
                    fragmentHome.GetItemsFromDatabase();
                    ResetScrollBar(items);
                }
            }
            dialogAlert.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        dialogAlert.show();
    }

    private void ResetScrollBar(Item EditedItem){
        for (Item items: ScrollHandler.LayoutItem) {
            if (items.documentId.equals(EditedItem.documentId)){
                CreateCheckBoxList(EditedItem);
                ScrollHandler.scrollview.removeAllViews();
                ScrollHandler.scrollview.addView(loadItemsFromDatabase());
            }
        }
    }
}
