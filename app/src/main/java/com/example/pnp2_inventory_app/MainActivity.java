package com.example.pnp2_inventory_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // navigation object is created so we can access the navigation throughout the file
    private Navigation navigation;
    // cameraClass object allows for the camera to be called from main
    private cameraClass CameraClass;
    private fragment_home Fragment_Home;
    private fragment_camera Fragment_Camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //creates the instance of the program
        setContentView(R.layout.activity_main); //sets the current view to the activity

        Fragment_Camera = new fragment_camera();
        Fragment_Home = Button_Handler.AddRefreshButton(R.id.ImgBtnRefresh, this);
        MakeNavigation(savedInstanceState);
        CameraClass = Button_Handler.AddCameraButton(R.id.ImgBtnCam, this, getSupportFragmentManager(), navigation, Fragment_Camera);
    }

    private void MakeNavigation(Bundle savedInstanceState){
        navigation = new Navigation(this); //initialises the Navigation object
        //this is the start of the implementation of the navigation bar. the code for the navigation bar is in the Navigation java file
        //The constructor takes in the context from MainActivity(this). The NavigationCreate takes in the mainActivity as the Activity(this)
        navigation.NavigationCreate(this); //if the context is not null then it will be used for the navigation bar
        if (savedInstanceState == null) {//if the saved Instance(basically the programs screen) is not active we set the home screen to the fragment being shown
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_Home).commit(); //Sets the screen to home if nothing is displayed
            navigation.GetNavigationBar().setCheckedItem(R.id.nav_home); //sets the navigation bar to having home selected
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CameraClass.OnActivityHelper(requestCode, resultCode, data, Fragment_Camera);
        CameraClass.DetectText();
    }

    public Navigation getNavigation(){
        return navigation;
    }

    public void test(){

        // Initialize Firebase!!
        // FirebaseApp.initializeApp(this);
        // FirebaseConfig dbActions = new FirebaseConfig();

        // initialize stuff
        // dbActions.ConnectDatabase();

        // testing create method
        // Creates sample Item for testing.
        // Item item = dbActions.CreateSampleItem();

        // testing pushToDb method, pushes Item objects
        // TODO: error or null inserts handling, check the "insides" of the object before sending it
        //dbActions.InsertDb(temp);

        // getAll testing
        // TODO: gets all and it is handled in callback.
        // dbActions.GetAll("InventoryItems", new FirebaseConfig.FirestoreCallback() {
            // @Override
            // public void OnCallBack(QuerySnapshot querySnapshot) {
                // for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    // String json = document.getData().toString();
                    // List<String> test = new ArrayList<>();
                    // test.add(json);
               // }
           // }
       // });


        // GetByParameter testing
        // TODO: needs rework
        // resultList = new ArrayList<>();
        // resultList = dbActions.GetByParameterValue("Milk", "1l");

        // testing Delete
        //dbActions.DeleteFromId("ySWgoTCrn8vj5p1GG16Q");
    }
}