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
    private Map<String, Object> item;
    private static final String TAG = "DbConnection";

    public void ConnectDatabase(){
        db = FirebaseFirestore.getInstance();
        item = new HashMap<>();
    }

    // creates the item data structure to push into the Db.
    // some problems: what does an item need?
    // TODO: figure out what we need inside the Document in the Db
    // example: expirationDate, name, insertDate, lastUpdated, quantity
    // suggestion: since we are saving the variable globally in this class, don't

    // think we need to return anything. it can be void, will think about the
    // implications later!
    public Map<String, Object> CreateItem(String _key, String _value){
        item.put(_key, _value);

        return item;
    }

    public Item CreateSampleItem(){
        String date = GetDate();

        Item newItem = new Item();
        newItem.m_Name = "TestName";
        newItem.m_Quantity = 1;
        newItem.m_ExpirationDate = "11/22/33";
        newItem.insertedDate = date;
        newItem.lastUpdated= date;

        newItem.CreateGuid();

        return newItem;
    }

    // helper method to grab date!
    public String GetDate(){
        // Get the current date and time
        Calendar calendar = Calendar.getInstance();

        // Format the timestamp as a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        String formattedTimestamp = dateFormat.format(calendar.getTime());

        return formattedTimestamp;
    }


    // Inserts Item into the Db. Check Item to see the fields that are being pushed.
    public void InsertDb(Item _item){

        // creates random GUID for documentID, insertdate and lastUpdated from current timestamp
        _item.CreateGuid();
        _item.insertedDate = GetDate();
        _item.lastUpdated = GetDate();

        db.collection("InventoryItems").document(_item.documentId).set(_item)
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

        db.collection("InventoryItems").document(_documentId)
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
    public List<Map<String, Object>> GetAll(String collection, FirestoreCallback callback){

        CollectionReference reference = db.collection(collection);

        // for testing currently
        List<Map<String, Object>> resultList = new ArrayList<>();


                reference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        callback.OnCallBack(queryDocumentSnapshots);

                    }
                });

        return resultList;
    }

    // Not sure how much this will be used.
    // If you want to find something in the Db with an specific value, let's say
    // parameter = "name" and value = "Whole Milk", you can find all the data in the Db that has
    // these two on it.
    // PROBLEM: if there are duplicates of the same item in the Db, it will return all of them. Seems like
    // just the parameter name and its value.
    // POSSIBLE SOLUTION: add the documentId parameter inside the object.
    // TODO: in the future, figure out if there will need to be changes here, possibly when we are going to display
    // everything to the user.
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

    // I think we need the id always.
    // Updates whatever you want from the Db based on the documentId
    public void UpdateFromId(String _documentId){

    }

    public interface FirestoreCallback{
        void OnCallBack(QuerySnapshot querySnapshot);
    }
}

