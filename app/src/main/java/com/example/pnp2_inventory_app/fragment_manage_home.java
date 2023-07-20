package com.example.pnp2_inventory_app;

import android.app.appsearch.GetByDocumentIdRequest;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import DbConfig.FirebaseConfig;
import DbConfig.Household;
import DbConfig.HouseholdConfig;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class fragment_manage_home extends Fragment {

    Household temp;
    String tempTwo;
    FirebaseConfig db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_home, container, false);

        Button btnAdd = view.findViewById(R.id.ButtonAddItem);
        Button btnEdit = view.findViewById(R.id.ButtonEditItem);
        Button btnDelete = view.findViewById(R.id.ButtonDelete);

        db = new FirebaseConfig();
        db.ConnectDatabase();

       /* btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertBox(getContext());
            }
        });*/


        // Inflate the layout for this fragment
        return view;
    }

   /* public void AlertBox(Context fragContext){
        androidx.appcompat.app.AlertDialog.Builder alertBuilder = new AlertDialog.Builder(fragContext); //create an alert-box object
        alertBuilder.create(); //creates the objects to be used
        alertBuilder.setTitle("Delete Items"); //sets the title the user will see
        alertBuilder.setMessage("Select Which Items You Wish To Delete"); //sets the message the user will see
        View dialogView = LayoutInflater.from(fragContext).inflate(R.layout.dialog_home_manegment, null);
        alertBuilder.setView(dialogView);

        EditText NewuserEmail = dialogView.findViewById(R.id.AddNewMemberEmail);

        alertBuilder.setPositiveButton("Accept", (dialog, id) -> AddNewMember(NewuserEmail));
        alertBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        alertBuilder.show(); //shows the alert box
    }*/


    /*public void AddNewMember(EditText userEmail){

        String NewMemberEmail = userEmail.getText().toString();
        HouseholdConfig householdConfig = new HouseholdConfig();
        householdConfig.ConnectDatabase();
        String Id = householdConfig.GetDocumentIdFromHouseHold();
        Household household = householdConfig.GetHousehold(Id);
        household.userList.add(NewMemberEmail);
        householdConfig.UpdateHousehold(household);
    }*/
}

