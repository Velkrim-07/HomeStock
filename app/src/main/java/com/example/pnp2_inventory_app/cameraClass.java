package com.example.pnp2_inventory_app;


import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
//import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity; not used
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class cameraClass {
    private final Navigation navigation;
    private final Activity mainActivity;
    private static final int REQUEST_IMAGE_CAPTURE = 1; //this allows us to use the camera
    private Bitmap imageTTBitmap; //this will hold the data for the picture
    protected Intent cameraIntent; //this will change the screen to the camera
    private TextView ResultITT; //this is where the text will be shown
    private FragmentManager fragmentManager;

    cameraClass(Navigation navigation, Activity mainActivity, FragmentManager fragmentManager){
        this.navigation = navigation;
        this.mainActivity = mainActivity;
        this.fragmentManager = fragmentManager;
    }

    @SuppressWarnings("deprecation")
    public void dispatchTakePictureIntent() {
        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //this changes the screen to the camera
        mainActivity.startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE); //this start the camera activity
    }

    public void RetakePictureHandler(View fragmentView){
        Button RetakePicture = fragmentView.findViewById(R.id.RetakePicture);
        //this will start the camera activity again
        RetakePicture.setOnClickListener(v -> dispatchTakePictureIntent());
    }

    public void AcceptPictureHandler(View fragmentView){
        Button_Handler.makeCameraAcceptButton(fragmentView, R.id.AcceptPicture, this);
        DetectText();//scans the text
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new fragment_home()).commit(); //Sets the screen to home if nothing is displayed
        navigation.GetNavigationBar().setCheckedItem(R.id.nav_home); //sets the navigation bar to having home selected
    }

    public void OnActivityHelper(int requestCode, int resultCode, Intent data, fragment_camera camera){
        ResultITT = camera.getView().findViewById(R.id.ImageToText); //why is this being done here?
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) { //if the picture is accepted then the data will be used to create the bitmap
            // Picture captured successfully
            // Once the picture has been captured successfully then it will be sent to the detectText class to be converted
            imageTTBitmap = (Bitmap) data.getExtras().get("data");
            Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
            if (fragment != null) {
                //this allows us to use the buttons from the fragment in main
                View fragmentView = fragment.getView(); // Access the views in the fragment
                assert fragmentView != null;
                ImageView imageView = fragmentView.findViewById(R.id.ImageViewPicture); //this grabs the button using the view and the Id from the XML
                imageView.setImageBitmap(imageTTBitmap); //this sets the image to the new Bitmap
                RetakePictureHandler(fragmentView); //allows the user to retake the image
                AcceptPictureHandler(fragmentView); //for now returns to home
            }
        }//if cancelled can come later
    }

   //The method where the image will be passed to so text can be pulled from the image
    public void DetectText() {
        InputImage image = InputImage.fromBitmap(imageTTBitmap, 0); // This converts the data back to the image to be processed
        //The image will be sent to a TextRecognition object that will then process the image
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = recognizer.process(image).addOnSuccessListener(text -> {
            // If the Text is detected then it will be sent into this nested for loop to extract the characters
            StringBuilder result1 = new StringBuilder();
            for(Text.TextBlock block: text.getTextBlocks())
            {
                String blockText = block.getText();
                Point[] blockCornerPoint = block.getCornerPoints();
                Rect blockFrame = block.getBoundingBox();
                for(Text.Line line : block.getLines())
                {
                    String lineText = line.getText();
                    Point[] lineCornerPoint = line.getCornerPoints();
                    Rect lineFrame = line.getBoundingBox();
                    for(Text.Element element : line.getElements())
                    {
                        String elementText = element.getText();
                        // characters get appended here
                        result1.append(elementText);
                    }
                    // Sets the textview on screen to the text that has been detected
                    ResultITT.setText(result1);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // If no text is detected then the textview on screen is set to a default Failure message
                Toast.makeText(mainActivity, "Failed to scan text in image", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
