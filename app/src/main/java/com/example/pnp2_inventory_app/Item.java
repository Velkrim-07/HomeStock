package com.example.pnp2_inventory_app;
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
}
