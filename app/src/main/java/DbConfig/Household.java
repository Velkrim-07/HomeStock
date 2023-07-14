package DbConfig;

import com.google.firebase.firestore.PropertyName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// import util class
import DbConfig.Util;

public class Household {

    public String name;
    public String creationDate;
    public String lastUpdated;

    public String houseHoldId;
    public String inventoryId;

    public String householdDescription;

    // list of users inside the household
    // if we want to expand later on regarding permissions and such, this would have to be reworked
    // possible solution would be creating a list for admins, and a list for normal users
    public List<String> userList;

    public Household(String _name, String _creationDate, String _lastUpdated, String _inventoryId, List<String> _userList, String _householdDescription){
            name = _name;
            creationDate = _creationDate;
            lastUpdated = _lastUpdated;
            inventoryId = _inventoryId;
            householdDescription = _householdDescription;
            userList = _userList;

            // calls helperFunction to create GUID
            houseHoldId = DbConfig.Util.CreateGuid();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("creationDate", creationDate);
        map.put("lastUpdated", lastUpdated);
        map.put("inventoryId", inventoryId);
        map.put("householdId", houseHoldId);
        map.put("householdDescription", householdDescription);

        map.put("userList", userList);

        return map;
    }


}
