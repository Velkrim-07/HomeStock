package DbConfig;

import com.example.pnp2_inventory_app.Item;
import com.google.firebase.firestore.PropertyName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// import util class
import DbConfig.Util;

public class Category {


    public String categoryName;
    public String categoryDescription;
    public String creationDate;
    public String lastUpdated;
    public String categoryId;
    public String inventoryId;

    // list of users inside the household
    // if we want to expand later on regarding permissions and such, this would have to be reworked
    // possible solution would be creating a list for admins, and a list for normal users
    public List<Item> itemList;

    public Category(String _name, String _creationDate, String _lastUpdated, String _inventoryId, List<Item> _itemList) {
        categoryName = _name;
        creationDate = _creationDate;
        lastUpdated = _lastUpdated;
        inventoryId = _inventoryId;
        itemList = _itemList;

        // calls helperFunction to create GUID
        categoryId = DbConfig.Util.CreateGuid();
    }
}