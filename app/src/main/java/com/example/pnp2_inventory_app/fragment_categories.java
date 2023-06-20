package com.example.pnp2_inventory_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fragment_categories extends Fragment {
    private Button foodButton;
    private Button officeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        //Button is used to direct to food category page
        foodButton = (Button) rootView.findViewById(R.id.ButtonFoodCategory);
        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFoodFragment(v);
            }
        });

        officeButton = (Button) rootView.findViewById(R.id.ButtonOfficeCategory);
        officeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFoodFragment(v);
            }
        });

        // Return the inflated view
        return rootView;
    }

    public void navigateToFoodFragment(View view) {
        Fragment fragment = new fragment_food();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}


