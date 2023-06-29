package com.example.pnp2_inventory_app;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class fragment_home extends Fragment {

    private Button buttonEditItem;
    private AlertDialog dialog; // Declare the dialog as a member variable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        List<Item> itemList = new ArrayList<>();
        Item item = new Item("Corn", 5, "2023-06-30");
        itemList.add(item);

        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(getContext(), R.layout.list_item_layout, itemList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
                }

                Item item = getItem(position);

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

        ListView listViewItems = rootView.findViewById(R.id.listViewItems);
        listViewItems.setAdapter(adapter);

        buttonEditItem = rootView.findViewById(R.id.ButtonEditItem);
        buttonEditItem.setVisibility(View.VISIBLE); // Set the visibility to always be visible

        ImageButton buttonAddItem = rootView.findViewById(R.id.ButtonAddItem);
        buttonAddItem.setOnClickListener(v -> {
            showDialogToAddItem(adapter);
        });

        rootView.setOnClickListener(v -> buttonEditItem.setVisibility(View.GONE));

        return rootView;
    }

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



