package com.example.pnp2_inventory_app;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    //This method will be used to show the user the "health" of their food with a food bar
    public String DetermineFoodHealth()
    {
        //These ints will be used to create the Local
        int year;
        int month;
        int day;
        //the current date will always be whatever time it was called
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }
        //insertion date will be based of of the substring for the year, month, and day
        LocalDate insertDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            insertDate = LocalDate.of(year = Integer.parseInt(insertedDate.substring(0,3)), month = Integer.parseInt(insertedDate.substring(5,7)), day = Integer.parseInt(insertedDate.substring(9,11)));
        }
        //The same will be applied for the expiration date
        LocalDate expirationDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            expirationDate = LocalDate.of(year = Integer.parseInt(m_ExpirationDate.substring(0,3)), month = Integer.parseInt(m_ExpirationDate.substring(5,7)), day = Integer.parseInt(m_ExpirationDate.substring(9,11)));
        }

        //In order to calculate how far along the food is we will need to find the percentage between the days till expiration and the original days till expiration
        long daysTillExpiration = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            daysTillExpiration = ChronoUnit.DAYS.between(currentDate, expirationDate);
        }
        long originalExpirationDays = daysTillExpiration();
        //once the days have been calculated we will divide the days till expiration by the original days till expiration to get a double that will look something like this EX: 0.123
        double percentExpired = daysTillExpiration / originalExpirationDays;

        //array that will hold the health bar characters to be changed
        Character[] healthBar = {'[', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', ']'};
        //index to note which character to change
        int index = 10;
        int colorIndex = 0;

        //while loop that changes the '#' symbols to '-' symbols depending on what percentage the food is expired
        while (percentExpired >= .1)
        {
            healthBar[index] = '-';
            percentExpired -= .1;
            index--;
            colorIndex++;

            //if the index gets to zero and the percentExpired is still above .1 it doesn't matter and we will break out of the loop because the bar is ready to be displayed
            if (index < 1) break;
        }

        //Create color index for different percentages EX: 80% and above is green and between 60% and 79% is lime green
        String ANSI_Color = "";

        if (colorIndex < 4)
        {
            ANSI_Color = "\u001B[32m";
        }
        else if (colorIndex < 8)
        {
            ANSI_Color = "\u001B[33m";
        }
        else {
            ANSI_Color = "\u001B[31m";
        }
        //String to be printed
        String print = "";

        //append all of the characters in the health bar to the print String
        for (char element : healthBar)
        {
            print = print + element;
        }

        //Print out the healthBar
        return ANSI_Color + print;
    }

    //does the same calculation as the above method but just returns the amount of days till expiration instead
    public int daysTillExpiration ()
    {
        int iYear = Integer.parseInt(insertedDate.substring(0,3)) * 365;
        int iMonth = Integer.parseInt(insertedDate.substring(5,7)) * 31;
        int iDay = Integer.parseInt(insertedDate.substring(9,11));
        //totals days from 0/0/0. will be used to calculated days between the insertion date and expiration date
        int insertDateDays = iYear + iMonth + iDay;

        int eYear = Integer.parseInt(m_ExpirationDate.substring(0,3)) * 365;
        int eMonth = Integer.parseInt(m_ExpirationDate.substring(5,7)) * 31;
        int eDay = Integer.parseInt(m_ExpirationDate.substring(9,11));
        //total days from 0/0/0. will be used to calculate days between the insertion date and expiration date
        int expirationDateDays = eYear + eMonth + eDay;

        return expirationDateDays - insertDateDays;
    }
    //year, month, date
    public void FrozenFood ()
    {
        //Local variables used to turn the m_ExpirationDate into a LocalDate object
        int year = Integer.parseInt(insertedDate.substring(0,3));
        int month = Integer.parseInt(insertedDate.substring(5,7));
        int day = Integer.parseInt(insertedDate.substring(9,11));
        String newExpirationDate = "";

        if (month + 2 > 12)
        {
            month -= 10;
            year++;
        }
        else month += 2;

        newExpirationDate += year;
        newExpirationDate += '-';
        newExpirationDate += month;
        newExpirationDate += '-';
        newExpirationDate += day;

        m_ExpirationDate = newExpirationDate;

    }
}
