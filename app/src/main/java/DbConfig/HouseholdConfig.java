package DbConfig;

import android.util.Log;

import androidx.annotation.NonNull;

// imports for the Get method
import com.example.pnp2_inventory_app.Item;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

// map and hash for testing
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// calendar
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Locale;

// firebase queries
import com.google.firebase.firestore.DocumentReference;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

// household and util
import org.checkerframework.checker.units.qual.A;
import DbConfig.Household;
import DbConfig.Util;

public class HouseholdConfig {

    private FirebaseFirestore db;
    private static final String TAG = "DbConnection";
    private final String collectionPath = "Households";
    public String householdId;

    public void ConnectDatabase(){
        db = FirebaseFirestore.getInstance();
    }

    public Household CreateSampleHousehold(){
        List<String> sampleTemp = new ArrayList<>();
        sampleTemp.add("test@gmail.com");
        sampleTemp.add("test2@gmail.com");
        sampleTemp.add("test3@gmail.com");
        sampleTemp.add("test123@gmail.com");

        Household newHousehold = new Household("sampleHouse", DbConfig.Util.GetDate(), DbConfig.Util.GetDate(),
                DbConfig.Util.CreateGuid(), sampleTemp);

        return newHousehold;
    }

    public void InsertHousehold(Household _household){
        db.collection("Households").document(_household.houseHoldId).set(_household)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "DocumentSnapshot successfully INSERTED! _householdId ID: " + _household.houseHoldId);
                        } else {
                            // Error occurred
                            Exception exception = task.getException();
                        }
                    }
                });
    }

    public void UpdateHousehold(Household _household){
        // change lastUpdated field from item
        _household.lastUpdated = DbConfig.Util.GetDate();

        db.collection("Households").document(_household.houseHoldId).update(_household.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "DocumentSnapshot successfully UPDATED! HOUSEHOLDID: " + _household.houseHoldId);
                        } else {
                            // Error occurred
                            Exception exception = task.getException();
                        }
                    }
                });
    }

    public void DeleteHouseholdFromId(String _householdId){
        db.collection("Households").document(_householdId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted! HOUSEHOLD ID: " + _householdId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document, HOUSEHOLD ID: " + _householdId, e);
                    }
                });
    }

    // Gets everything inside the collection
    // PROBLEM: it will be necessary some kind of pagination maybe depending on the amount of items in the collection
    // TODO: implement error handling
    public void GetAll(String collection, HouseholdConfig.FirestoreHouseholdCallback callback){

        CollectionReference reference = db.collection(collection);

        reference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        callback.OnCallBack(queryDocumentSnapshots);

                    }
                });
    }

    // this methods returns the documentId of the household querying the email that is currently logged in.
    public String GetDocumentIdFromHouseHold() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String email = currentUser.getEmail();

        db.collection(collectionPath)
                .whereArrayContains("userList", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        householdId = document.getId();
                        break;
                        // Process the document ID as needed
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("MainActivity", "Error querying documents: " + e.getMessage());
                });

        return householdId;
    }

    public interface FirestoreHouseholdCallback{
        void OnCallBack(QuerySnapshot querySnapshot);
    }
}
