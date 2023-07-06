package com.example.pnp2_inventory_app;

import android.app.appsearch.GetByDocumentIdRequest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import DbConfig.FirebaseConfig;
import DbConfig.Household;
import DbConfig.HouseholdConfig;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class fragment_manage_home extends Fragment {

    Household temp;
    String tempTwo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_home, container, false);

        Button btnAdd = view.findViewById(R.id.buttonAdd);
        TextView idk = view.findViewById(R.id.textViewHomeStock);

        FirebaseConfig db = new FirebaseConfig();
        db.ConnectDatabase();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Item sample = db.CreateSampleItem();
               db.InsertDb(sample);
            }
        });

        // Inflate the layout for this fragment
        return view;

    }

}

