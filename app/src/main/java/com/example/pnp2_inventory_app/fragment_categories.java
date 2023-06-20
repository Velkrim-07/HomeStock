package com.example.pnp2_inventory_app;

import static java.lang.ref.Cleaner.create;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class fragment_categories extends Fragment {
    int ButtonCounter = 1;
    Context context;
    View view;
    static LinearLayout.LayoutParams params;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_categories, container, false);
        context = getContext();
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button AddCategory = view.findViewById(R.id.ButtonAddCategory);
        AddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertBox();
                //open dialog box here
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
    private void AlertBox(){
        final EditText edittext = new EditText(context);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.create();
        alertBuilder.setTitle("Create Category");
        alertBuilder.setMessage("Type in the name of the category you would like to create");
        alertBuilder.setView(edittext);
        alertBuilder.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextValue = edittext.getText().toString();
                AddCategory(YouEditTextValue);
            }
        });
        alertBuilder.setNegativeButton("No Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alertBuilder.show();
    }

    public void AddCategory(String CategoryName){
        LinearLayout usedLinearLayout;
        if(ButtonCounter/2 < 1){usedLinearLayout = view.findViewById(R.id.LeftLayout);}
        else{usedLinearLayout = view.findViewById(R.id.RightLayout);}
        Button CategoryBtn = new Button(this.context);
        CategoryBtn.setHeight(300);
        CategoryBtn.setWidth(600);
        CategoryBtn.setLayoutParams(params);
        CategoryBtn.setText(CategoryName);
        usedLinearLayout.addView(CategoryBtn);
        CategoryBtn.layout(300, 300, 300, 300);
        ButtonCounter++;
    }
}