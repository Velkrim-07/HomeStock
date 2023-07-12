package DbConfig;

import android.util.Log;

import androidx.annotation.NonNull;

// imports for the Get method
import com.example.pnp2_inventory_app.Item;
import com.google.android.gms.tasks.Task;
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


// TODO: CREATE CLASS THAT HAS ALL VARIABLES WE NEED INSIDE AN ITEM. ONE FOR EACH THING!

public class FirebaseConfig {

    private FirebaseFirestore db;
    private static final String TAG = "DbConnection";

    // create collectionRef here
    String collectionName = "Households"; // Replace with the actual collection name
    String subcollectionName = "InventoryItems"; // Replace with the actual subcollection name
    CollectionReference subcollectionRef;

    public void ConnectDatabase(){
        db = FirebaseFirestore.getInstance();

        HouseholdConfig householdConnect = new HouseholdConfig();
        householdConnect.ConnectDatabase();

        List<String> tempTest;
        String tempTwo;
        tempTest = householdConnect.GetDocumentIdFromHouseHold();
        tempTwo = "a2553d61-c3ef-465e-96c0-8f9b75159393"; // for now

        DocumentReference documentRef = db.collection(collectionName).document(tempTwo);
        subcollectionRef = documentRef.collection(subcollectionName);
    }

    // Creates sample item used only for testing
    public Item CreateSampleItem(){
        String date = GetDate();

        Item newItem = new Item("TestName", 1,"11/22/33");
        newItem.insertedDate = date;
        newItem.lastUpdated= date;
        return newItem;
    }

    // Helper method to grab date and format it to date/time
    // yyyy-MM-dd HH:mm:ss
    public String GetDate(){
        // Get the current date and time
        Calendar calendar = Calendar.getInstance();

        // Format the timestamp as a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        String formattedTimestamp = dateFormat.format(calendar.getTime());

        return formattedTimestamp;
    }


    // Inserts Item into the Db.
    // TODO: Check Item to see the fields that are being pushed. We need something similar to DDD contracts
    // TODO: It has to have a documentId, name, quantity and expirationDate. The insertedDate&lastUpdated will be handled inside the insertion Method
    public void InsertDb(Item _item){

        // creates random GUID for documentID, insertDate and lastUpdated from current timestamp
        _item.documentId = DbConfig.Util.CreateGuid();
        _item.insertedDate = GetDate();
        _item.lastUpdated = GetDate();

        subcollectionRef.document(_item.documentId).set(_item)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "DocumentSnapshot successfully INSERTED! ID: " + _item.documentId);
                        } else {
                            // Error occurred
                            Exception exception = task.getException();
                        }
                    }
                });
    }

    // Deletes from the Db based on the documentId.
    // Still figuring out if that is sufficient.
    // TODO: only deletes from InventoryItems collection, maybe add another parameter for the collection
    public void DeleteFromId(String _documentId){

        subcollectionRef.document(_documentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted! ID: " + _documentId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document, ID: " + _documentId, e);
                    }
                });
    }

    // Gets everything inside the collection
    // PROBLEM: it will be necessary some kind of pagination maybe depending on the amount of items in the collection
    // TODO: implement error handling
    public void GetAll(String collection, FirestoreCallback callback){

                // collection1 Household -> collection2 inside household is InventoryItems
                    subcollectionRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        callback.OnCallBack(queryDocumentSnapshots);

                    }
                });
    }

    // Deprecated. When we implement or start using this I will rewrite it.
    public List<Map<String, Object>> GetByParameterValue(String _parameter, String _value){

        // for testing currently
        List<Map<String, Object>> resultList = new ArrayList<>();

        // gets all the data that has the same parameter inside, with the same value.
        db.collection("InventoryItems").whereEqualTo(_parameter, _value)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                        // for each document inside the collection that matches the filter,
                        // adds to the resultList
                        for (DocumentSnapshot document : documents) {
                            Map<String, Object> data = document.getData();
                            resultList.add(data);

                            Log.d(TAG, "DOCUMENTID FROM GETBYPARAMETER:" + document.getId());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // error retrieving documents
                    }
                });

        return resultList;
    }

    // Updates specific Item in the Db, requires the documentId.
    // It changes the Item into a Map since the update method requires a Map. - toMap();
    public void UpdateFromId(String _documentId, Item _item){

        // change lastUpdated field from item
        _item.lastUpdated = GetDate();

        subcollectionRef.document(_documentId).update(_item.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "DocumentSnapshot successfully UPDATED! ID: " + _item.documentId);
                        } else {
                            // Error occurred
                            Exception exception = task.getException();
                        }
                    }
                });
    }

    public interface FirestoreCallback{
        void OnCallBack(QuerySnapshot querySnapshot);
    }
}

