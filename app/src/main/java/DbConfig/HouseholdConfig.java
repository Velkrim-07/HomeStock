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
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

// map and hash for testing
import java.sql.Array;
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

    public void ConnectDatabase() {
        db = FirebaseFirestore.getInstance();
    }

    public Household CreateSampleHousehold() {
        List<String> sampleTemp = new ArrayList<>();
        sampleTemp.add("rafatest@gmail.com");
        sampleTemp.add("judahtest@gmail.com");
        sampleTemp.add("brandontest@gmail.com");
        sampleTemp.add("kelltest@gmail.com");

        Household newHousehold = new Household("sampleHouse", DbConfig.Util.GetDate(), DbConfig.Util.GetDate(),
                DbConfig.Util.CreateGuid(), sampleTemp, "");

        return newHousehold;
    }

    public void InsertHousehold(Household _household) {
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

    public void UpdateHousehold(Household _household) {
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

    public void DeleteHouseholdFromId(String _householdId) {
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
    public void GetAll(String collection, HouseholdConfig.FirestoreHouseholdCallback callback) {

        CollectionReference reference = db.collection(collection);

        reference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        callback.OnCallBack(queryDocumentSnapshots);

                    }
                });
    }

    // gets household if you have an Id
    public void GetHouseholdUsers(HouseholdConfig.FirestoreHouseholdCallback callback, String householdId) {

        db.collection("Household")
                .whereEqualTo("householdId", householdId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        callback.OnCallBack(querySnapshot);
                    }
                });
    }

    // Gets Household after you already know what is the householdId. returns as household
    // WORKING! TODO: maybe some error handling in else
    public Household GetHousehold(String _householdId) {
        Household household = CreateSampleHousehold();

        db.collection("Households").document(_householdId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        household.name = documentSnapshot.getString("name");
                        household.householdDescription = documentSnapshot.getString("householdDescription");
                        household.creationDate = documentSnapshot.getString("creationDate");
                        household.lastUpdated = documentSnapshot.getString("lastUpdated");
                        household.houseHoldId = documentSnapshot.getString("houseHoldId");
                        household.inventoryId = documentSnapshot.getString("inventoryId");
                    } else {

                    }

                }).addOnFailureListener(e -> {
                    // Error occurred while retrieving the document
                    // Handle the failure case
                });

        return household;
    }


    // this methods returns the documentId of the household querying the email that is currently logged in.
    // returns a list of households the user is part of, if more than one.
    public void GetDocumentIdFromHouseHold(HouseholdConfig.FirestoreHouseholdCallback callback) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String email = currentUser.getEmail();

        List<String> userList = new ArrayList<>();

        db.collection(collectionPath)
                .whereArrayContains("userList", email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        callback.OnCallBack(queryDocumentSnapshots);
                    }
                });
    }

    public interface FirestoreHouseholdCallback{
        void OnCallBack(QuerySnapshot querySnapshot);
    }
}
