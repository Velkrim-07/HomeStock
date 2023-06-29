package com.example.pnp2_inventory_app;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.google.firebase.firestore.PropertyName;
public class Item {
    // was private changed for testing
    @PropertyName("name")
    public String m_Name;

    // was private changed for testing
    @PropertyName("quantity")
    public int m_Quantity;

    // was private changed for testing
    @PropertyName("expirationDate")
    public String m_ExpirationDate;

    // Rafael Testing
    public String insertedDate;
    public String lastUpdated;
    public String documentId; // incremental id

    // deleting this cuz fuck it
    /*public Item(String name, int quantity, String _expirationDate, String _insertedDate, String _lastUpdated) {
        this.m_Name = name;
        this.m_Quantity = quantity;
        this.m_ExpirationDate = _expirationDate;
        this.insertedDate = _insertedDate;
        this.lastUpdated = _lastUpdated;
    }*/


    public Item(String itemName, int amount, String expireDate){
        m_Name = itemName;
        m_Quantity = amount;
        m_ExpirationDate = expireDate;
        CreateGuid();
    }

    public Item(String itemName, int amount, String expireDate, String _documentId){
        m_Name = itemName;
        m_Quantity = amount;
        m_ExpirationDate = expireDate;
        documentId = _documentId;
    }
    public String getName() {
        return m_Name;
    }

    public int getQuantity() {
        return m_Quantity;
    }

    public String getExpirationDate() {
        return m_ExpirationDate;
    }
    public void CreateGuid(){
        documentId =  UUID.randomUUID().toString();
    }
    // Implement the toMap() method
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", m_Name);
        map.put("quantity", m_Quantity);
        map.put("expirationDate", m_ExpirationDate);
        map.put("insertedDate", insertedDate);
        map.put("lastUpdated", lastUpdated);
        map.put("documentId", documentId);

        return map;
    }
}
