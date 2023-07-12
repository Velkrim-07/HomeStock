package com.example.pnp2_inventory_app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class fragment_Settings extends Fragment {
    // Called when the fragment should create its view
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button_Handler.AddManageHomeButton(view, R.id.buttonManageHome, this);
        return view;
    }

}

