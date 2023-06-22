package com.example.pnp2_inventory_app;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class fragment_categories extends Fragment {
    int ButtonCounter = 1; //keeps count of the amount of buttons being added
    Context context; //allows for the context to be accessed throughout the class
    View view;//allows for the view to be accessed throughout the class

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_categories, container, false);
        context = getContext();

        Button AddCategory = view.findViewById(R.id.ButtonAddCategory);
        AddCategory.setOnClickListener(v -> {
            AlertBox();//opens the dialog box
        });

        //Button is used to direct to food category page
        Button foodButton = (Button) view.findViewById(R.id.ButtonFoodCategory);
        foodButton.setOnClickListener(this::navigateToFoodFragment);

        Button officeButton = (Button) view.findViewById(R.id.ButtonOfficeCategory);
        officeButton.setOnClickListener(v -> navigateToOfficeFragment());

        // Return the inflated view
        return view;
    }

    public void navigateToFoodFragment(View view) {
        Fragment fragment = new fragment_food();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void AddCategory(String CategoryName){
        LinearLayout usedLinearLayout;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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

    public void navigateToOfficeFragment() {
        Fragment fragment = new fragment_office(); // Replace fragment_office with your office fragment class
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void AlertBox(){
        final EditText edittext = new EditText(context); //creates a editable text box for the user to name their button
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context); //create an alertbox object
        alertBuilder.create(); //creates the objects to be used
        alertBuilder.setTitle("Create Category"); //sets the title the user will see
        alertBuilder.setMessage("Type in the name of the category you would like to create"); //sets the message the user will see
        alertBuilder.setView(edittext); //sets a view as the editable text we created before
        //creates the Accept button in the alert box
        alertBuilder.setPositiveButton("Accept", (dialog, whichButton) -> {
            String YouEditTextValue = edittext.getText().toString();
            AddCategory(YouEditTextValue);
        });
        //creates the cancel button in the alert box
        alertBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {
            // what ever you want to do with No option.
        });

        alertBuilder.show(); //shows the alert box
    }
}


