package com.example.pnp2_inventory_app;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//Class for handling all of the button events
//If need be we can move certain buttons to other classes for ease of access
public class Button_Handler
{

    //This button will add another entry to the database with the information that the
    //user provides
    public static void MakeAddButton(View view, int Id, fragment_home fragment) {//takes in button, and the view of the object
        ImageButton button = view.findViewById(Id);
        button.setOnClickListener(v -> fragment.showDialogToAddItem());
    }

    public static void makeEditButton(View view, int Id) {//takes in button, and the view of the object
        Button button = view.findViewById(Id);
        button.setOnClickListener(v -> {
            //Nothing here yet
        });
    }

    public static void makeCameraAcceptButton(View view, int Id, cameraClass fragment) {//takes in button, and the view of the object
        Button button = view.findViewById(Id);
        button.setOnClickListener(v -> fragment.AcceptPictureHandler(view));
    }
    public static void AddCategory(View view, int Id, fragment_categories fragment) {//takes in button, and the view of the object
        ImageButton button = view.findViewById(Id);
        button.setOnClickListener(v -> fragment.AlertBox());
    }

    public static void AddFoodButton(View view, int Id, fragment_categories fragment) {//takes in button, and the view of the object
        Button button = view.findViewById(Id);
        button.setOnClickListener(v -> {
            Fragment foodFragment = new fragment_food();
            FragmentTransaction transaction = fragment.getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, foodFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    public static void AddOfficeButton(View view, int Id, fragment_categories fragment) {//takes in button, and the view of the object
        Button button = view.findViewById(Id);
        button.setOnClickListener(v -> {
            Fragment officeFragment = new fragment_office(); // Replace fragment_office with your office fragment class
            FragmentTransaction transaction = fragment.getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, officeFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }


    public static void AddManageHomeButton(View view, int Id) {//takes in button, and the view of the object
        Button button = view.findViewById(Id);
        button.setOnClickListener(v -> {
            Fragment fragment1 = new fragment_manage_home();
            FragmentTransaction transaction = fragment1.getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment1);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    public static  fragment_home AddRefreshButton(int Id, Activity fragment) {//takes in button, and the view of the object
        ImageButton button = fragment.findViewById(Id);
        fragment_home Fragment_home = new fragment_home(); //initialises home fragment for the refresh button
        button.setOnClickListener(v -> Fragment_home.GetItemsFromDatabase());
        return Fragment_home;
    }

    public static cameraClass AddCameraButton(int Id, Activity activity, FragmentManager fragmentchanger, Navigation navigation, fragment_camera cameraFragment) {//takes in button, and the view of the object
        cameraClass camera = new cameraClass(navigation, activity, fragmentchanger);
        ImageButton button = activity.findViewById(Id);
        button.setOnClickListener(v -> {
            camera.dispatchTakePictureIntent();
            fragmentchanger.beginTransaction().replace(R.id.fragment_container, cameraFragment).commit();
        });
        return camera;
    }
}
