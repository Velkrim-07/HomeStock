package com.example.pnp2_inventory_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class fragment_home extends Fragment {
    private Button buttonEditItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

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