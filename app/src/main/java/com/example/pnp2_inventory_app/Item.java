package com.example.pnp2_inventory_app;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.google.firebase.firestore.PropertyName;


// util class
import DbConfig.Util;


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
    public TextView NameObject;
    public TextView AmountObject;
    public TextView ExpireDateObject;


    // deleting this cuz fuck it
    /*public Item(String name, int quantity, String _expirationDate, String _insertedDate, String _lastUpdated) {
        this.m_Name = name;
        this.m_Quantity = quantity;
        this.m_ExpirationDate = _expirationDate;
        this.insertedDate = _insertedDate;
        this.lastUpdated = _lastUpdated;
    }*/

    //Constructor for new item
    public Item(String itemName, int amount, String expireDate){
        m_Name = itemName;
        m_Quantity = amount;
        m_ExpirationDate = expireDate;
        documentId = DbConfig.Util.CreateGuid(); // gets a random document id
    }
    //Constructor for item inside database
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

    //Creates the text view necessary to show items to the user
    public void ConstructObject(Context fragContext){
        NameObject = new TextView(fragContext);
        NameObject.setTextSize(15);
        NameObject.setTextColor(Color.parseColor("#000000"));
        NameObject.setText(m_Name);
        NameObject.setBackgroundColor(Color.parseColor("#e6f2a2"));
        AmountObject = new TextView(fragContext);
        AmountObject.setTextSize(15);
        AmountObject.setTextColor(Color.parseColor("#000000"));
        AmountObject.setText(String.valueOf(m_Quantity));
        AmountObject.setBackgroundColor(Color.parseColor("#e6f2a2"));
        ExpireDateObject = new TextView(fragContext);
        ExpireDateObject.setTextSize(15);
        ExpireDateObject.setTextColor(Color.parseColor("#000000"));
        ExpireDateObject.setText(m_ExpirationDate);
        ExpireDateObject.setBackgroundColor(Color.parseColor("#e6f2a2"));
    }
}
