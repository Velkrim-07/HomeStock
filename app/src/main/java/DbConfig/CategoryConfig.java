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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

// household and util
import org.checkerframework.checker.units.qual.A;
import DbConfig.Household;
import DbConfig.Category;
import DbConfig.Util;

public class CategoryConfig {

    private FirebaseFirestore db;
    private static final String TAG = "DbConnection";
    private final String collectionPath = "Category";
    public String categoryId;

    List<String> tempList;

    // create collectionRef here
    String collectionName = "Households"; // Replace with the actual collection name
    String subcollectionName = "Categories"; // Replace with the actual subcollection name
    CollectionReference subcollectionRef;

    public void ConnectDatabase() {
        db = FirebaseFirestore.getInstance();

        // new household connection
        HouseholdConfig temp = new HouseholdConfig();

        /*temp.GetDocumentIdFromHouseHold(querySnapshot ->
                tempList = TestDB(querySnapshot)
        );

        if (tempList.isEmpty()) {
            tempList.add("a2553d61-c3ef-465e-96c0-8f9b75159393");
        }*/

        DocumentReference documentRef = db.collection(collectionName).document("a2553d61-c3ef-465e-96c0-8f9b75159393");
        subcollectionRef = documentRef.collection(subcollectionName);
    }

    public List<String> TestDB(QuerySnapshot temp) {

        List<String> userList = new ArrayList<>();

        for (DocumentSnapshot document : temp) {
            categoryId = document.getId();
            // maybe remove break?
            // break;
            userList.add(categoryId);
        }

        return userList;
    }

    // Creates Sample Category from Category Class
    public Category CreateSampleCategory() {
        List<Item> sampleList = new ArrayList<>();
        FirebaseConfig item = new FirebaseConfig();

        // creates samples 3 times
        sampleList.add(item.CreateSampleItem());
        sampleList.add(item.CreateSampleItem());
        sampleList.add(item.CreateSampleItem());

        Category sampleCategory = new Category("sampleCategory", DbConfig.Util.GetDate(), DbConfig.Util.GetDate(),
                DbConfig.Util.CreateGuid(), sampleList);

        return sampleCategory;
    }

    // To insert a Category into a Household, you first need to know which household we are talking about.
    // After that, you navigate inside that specific household and add a new collection called Category
    public void InsertDb(Category _category) {

        // creates random GUID for documentID, insertDate and lastUpdated from current timestamp
        _category.categoryId = DbConfig.Util.CreateGuid();
        _category.creationDate = Util.GetDate();
        _category.lastUpdated = Util.GetDate();

        subcollectionRef.document(_category.categoryId).set(_category)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "DocumentSnapshot successfully INSERTED! ID: " + _category.categoryId);
                        } else {
                            // Error occurred
                            Exception exception = task.getException();
                        }
                    }
                });
    }
}