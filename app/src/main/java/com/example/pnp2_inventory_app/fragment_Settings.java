package com.example.pnp2_inventory_app;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fragment_Settings extends Fragment {
    // Called when the fragment should create its view
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Context context = getContext();

        // Get the reference to the "Manage Home" button
        Button manageHome = (Button) view.findViewById(R.id.buttonManageHome);

        // Set a click listener for the button
        manageHome.setOnClickListener(v -> navigateToManageHome());

        // Return the inflated view
        return view;
    }

    // Method to navigate to the "Manage Home" fragment
    public void navigateToManageHome() {
        // Create an instance of the "Manage Home" fragment
        Fragment fragment = new fragment_manage_home();

        // Start a new fragment transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Replace the current fragment with the "Manage Home" fragment
        transaction.replace(R.id.fragment_container, fragment);

        // Add the transaction to the back stack, allowing user to navigate back
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}

