package com.example.pnp2_inventory_app;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import DbConfig.FirebaseConfig;

public class fragment_categories extends Fragment {
    private int ButtonCounter = 0; //keeps count of the amount of buttons being added
    private Context context; //allows for the context to be accessed throughout the class
    private View view;//allows for the view to be accessed throughout the class
    private Button[] NewButtonArray; //this holds the buttons used and sends it to navigation
    private final Navigation navigation; //this allow this class to access navigation
    private FirebaseConfig db;

    fragment_categories(Navigation navigation, Button[] categoryButtonArrays){
        this.navigation = navigation;
        NewButtonArray = categoryButtonArrays;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_categories, container, false);
        context = getContext();

        NewButtonArray = new Button[10];

        db = new FirebaseConfig();
        db.ConnectDatabase();

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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context); //create an alert-box object
        alertBuilder.create(); //creates the objects to be used
        alertBuilder.setTitle("Create Category"); //sets the title the user will see
        alertBuilder.setMessage("Type in the name of the category you would like to create"); //sets the message the user will see

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_make_category, null);
        alertBuilder.setView(dialogView);

        LinearLayout AlertBoxLinearLayout = new LinearLayout(alertBuilder.getContext());

        final EditText ETName = dialogView.findViewById(R.id.editTextItemName);

        final EditText ETDescription = dialogView.findViewById(R.id.editDescription);

        final ScrollView AlertBoxScrollBar = dialogView.findViewById(R.id.categoryCheckedItemScrollBar);

        AlertBoxScrollBar.addView(GetItemsFromDatabase());

        alertBuilder.show(); //shows the alert box
    }

    public LinearLayout GetItemsFromDatabase(){
        LinearLayout ScrollBarLayout = new LinearLayout(context);
        ScrollBarLayout.setOrientation(LinearLayout.VERTICAL);

        List<Item> ItemList = new ArrayList<>(); //creates a list to hold the items we want to get from the database

        db.GetAll("InventoryItems", querySnapshot -> {
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                String name = document.getString("name");
                int quantity = Objects.requireNonNull(document.getLong("quantity")).intValue();
                String expirationDate = document.getString("expirationDate");
                String documentId = document.getString("documentId");
                //String insertedDate = document.getString("insertedDate ");
                //String lastUpdated = document.getString("lastUpdated ");
                String insertedDate = db.GetDate();
                String lastUpdated = db.GetDate();

                //creates the item object
                Item item = new Item(name, quantity, expirationDate, documentId);
                item.insertedDate = insertedDate;
                item.lastUpdated = lastUpdated;

                if (item != null) {//placeholder foe new expression
                    //TODO : make sure there are no duplicates
                    ItemList.add(item); //adds the item to the list of items
                }
            }
            for(Item items: ItemList){
                ScrollBarLayout.addView(CreateCheckBoxList(items));
            }
        });

        return ScrollBarLayout;
    }

    public LinearLayout CreateCheckBoxList(Item NewItem){
        LinearLayout CheckBoxItemList = new LinearLayout(context);
        CheckBoxItemList.setOrientation(LinearLayout.HORIZONTAL);

        ViewGroup.LayoutParams AmountObjectParams = new ViewGroup.LayoutParams(100, 50);
        TextView Amount = new TextView(CheckBoxItemList.getContext());
        Amount.setText(String.valueOf(NewItem.getQuantity()));
        Amount.setLayoutParams(AmountObjectParams);

        ViewGroup.LayoutParams NameObjectParams = new ViewGroup.LayoutParams(600, 50);
        TextView NameCheckBox = new TextView(CheckBoxItemList.getContext());
        NameCheckBox.setText(NewItem.getName());
        NameCheckBox.setLayoutParams(NameObjectParams);

        ViewGroup.LayoutParams CheckBoxParams = new ViewGroup.LayoutParams(75, 50);
        CheckBox itemCheckbox = new CheckBox(CheckBoxItemList.getContext());
        itemCheckbox.setLayoutParams(CheckBoxParams);

        //Adding the TextViews to the InsideLinearLayout view
        CheckBoxItemList.addView(Amount);
        CheckBoxItemList.addView(NameCheckBox);
        CheckBoxItemList.addView(itemCheckbox);

        return CheckBoxItemList;
    }

}


