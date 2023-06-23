
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
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.Objects;

public class fragment_categories extends Fragment {
    private int ButtonCounter = 0; //keeps count of the amount of buttons being added
    private Context context; //allows for the context to be accessed throughout the class
    private View view;//allows for the view to be accessed throughout the class
    private Button[] NewButtonArray; //this holds the buttons used and sends it to navigation
    private final Navigation navigation; //this allow this class to access navigation
    private LinearLayout.LayoutParams params;

    fragment_categories(Navigation navigation, Button[] categoryButtonArrays){
        this.navigation = navigation;
        NewButtonArray = categoryButtonArrays;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_categories, container, false);
        context = getContext();
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        NewButtonArray = new Button[10];

        ImageButton AddCategory = view.findViewById(R.id.ButtonAddCategory);
        AddCategory.setOnClickListener(v -> {
            AlertBox();//opens the dialog box
        });

        //Button is used to direct to food category page
        Button foodButton = (Button) view.findViewById(R.id.ButtonFoodCategory);
        foodButton.setOnClickListener(this::navigateToFoodFragment);

        Button officeButton = (Button) view.findViewById(R.id.ButtonOfficeCategory);
        officeButton.setOnClickListener(v -> navigateToOfficeFragment());

        InitialiseButtons(); //initialise user buttons

        return view; // Return the inflated view
    }

    public void navigateToFoodFragment(View view) {
        Fragment fragment = new fragment_food();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void navigateToOfficeFragment() {
        Fragment fragment = new fragment_office(); // Replace fragment_office with your office fragment class
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //this creates the button object to be used
    public Button AddCategory(String CategoryName){
        Button CategoryBtn = new Button(this.context);
        CategoryBtn.setHeight(300);
        CategoryBtn.setWidth(600);
        CategoryBtn.setLayoutParams(params);
        CategoryBtn.setText(CategoryName);
        CategoryBtn.layout(300, 300, 300, 300);
        CategoryBtn.setBackgroundColor(R.style.Base_Theme_PNP2Inventoryapp);
        return CategoryBtn;
    }

    //this picks which layout to use when the button is picked
    public void ButtonLayoutAdder(Button ButtonAdded, int layoutSide){
        LinearLayout usedLinearLayout;
        if(!(layoutSide/2 < 1) || layoutSide == 0){
            usedLinearLayout = view.findViewById(R.id.LeftLayout);}
        else{
            usedLinearLayout = view.findViewById(R.id.RightLayout );}
        usedLinearLayout.addView(ButtonAdded);
    }

    //This runs through the buttons in the button array and adds them to the layout view
    public void InitialiseButtons(){
        NewButtonArray = navigation.GetButtonArray();
        for (int i = 0; i < NewButtonArray.length; i++ ){
            if(NewButtonArray[i] == null || Objects.equals((String) NewButtonArray[i].getText(), "")) {break;}
            else{ButtonLayoutAdder(AddCategory((String)NewButtonArray[i].getText()), i);}
        }
    }

    //this pulls up the Alertbox and adds a button the user fills out the data
    private void AlertBox(){
        final EditText edittext = new EditText(context); //creates a editable text box for the user to name their button
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context); //create an alert-box object
        alertBuilder.create(); //creates the objects to be used
        alertBuilder.setTitle("Create Category"); //sets the title the user will see
        alertBuilder.setMessage("Type in the name of the category you would like to create"); //sets the message the user will see
        alertBuilder.setView(edittext); //sets a view as the editable text we created before\

        //creates the Accept button in the alert box
        alertBuilder.setPositiveButton("Accept", (dialog, whichButton) -> {
            String YouEditTextValue = edittext.getText().toString(); //gets the name of the button from the user
            NewButtonArray[ButtonCounter] = AddCategory(YouEditTextValue); //creates the new button and adds it to the button array
            ButtonLayoutAdder(NewButtonArray[ButtonCounter], ButtonCounter); //adds the button to the layout
            ButtonCounter++; //we get ready for the next button
            navigation.SetButtonArray(NewButtonArray); //we give the array of button to the navigation
        });
        //creates the cancel button in the alert box
        alertBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {
            // what ever you want to do with No option.
        });
        alertBuilder.show(); //shows the alert box
    }
}


