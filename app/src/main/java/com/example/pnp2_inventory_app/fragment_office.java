package com.example.pnp2_inventory_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class fragment_office extends Fragment {
    private Button buttonEditItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.office_category_layout, container, false);

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
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the visibility of the "Edit" button
                if (buttonEditItem.getVisibility() == View.VISIBLE) {
                    buttonEditItem.setVisibility(View.GONE);
                } else {
                    buttonEditItem.setVisibility(View.VISIBLE);
                }
            }
        });

        // Add OnClickListener to hide the "Edit" button when the user clicks anywhere on the screen
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEditItem.setVisibility(View.GONE);
            }
        });

        return rootView;
    }
}

