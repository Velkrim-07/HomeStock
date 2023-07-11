package com.example.pnp2_inventory_app;

//This class will be used to create the Variables needed in order to name, group, and delete
//the items that will be going into the database.
import android.os.Build;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Vector;

public class Item_Class
{
    //A private String variable that will be used to store the name of the product being entered
    //We shouldn't need to change this, which is why it is set as private
    private String m_Name;
    //A private int variable that will be used to store the amount of the product the user has in
    //the database. This will be set once at the beginning when the user purchases the item for the
    //first time and then will be updated every time after that.
    protected int m_Quantity;
    //A protected string variable that will be used to store the expiration date of the item purchased
    //This will be determined manually be the user for the moment being although it will be possible
    //to automatically set it at a later date when the functionality is implemented. A problem that I
    //could potentially see is if the user purchases multiple items at different times so that their
    //expiration dates are different, although i think this could be resolved easily by grouping them
    //via their expiration date when they are first listed.
    protected String m_ExpirationDate;
    //A private string variable that will be used to store the insertion date for the item(s) being added
    //Once again we shouldn't need ot change this variable, which is why it is set as private
    private String m_InsertDate;
    //A protected string variable that will be used to store the date of when the item was last updated
    protected String m_LastUpdated;
    //A protected int variable that will be used only internally in for the frequency algorithm
    //The user will not be able to access this variable and it will only be manipulated in the algorithm
    //to better help it understand what the user is buying
    protected int m_Frequency;
    //A Public String variable that will be used for the health bar for all of the products
    public String m_HealthBar;

    //This method will set the name of the string so the data is protected
    public void SetName(String name)
    {
        m_Name = name;
    }

    //This method will get the name that was set to m_Name so the data is protected
    public String GetName()
    {
        return m_Name;
    }

    //This method will set the date for m_InsertionDate so that the data is protected
    public void SetInsertDate(String InsertDate)
    {
        m_InsertDate = InsertDate;
    }

    //This method will get the Insertion date that was set to m_InsertionDate so the data is protected
    public String GetInsertionDate()
    {
        return m_InsertDate;
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
            insertDate = LocalDate.of(year = Integer.parseInt(m_InsertDate.substring(0,3)), month = Integer.parseInt(m_InsertDate.substring(5,7)), day = Integer.parseInt(m_InsertDate.substring(9,11)));
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
        long originalExpirationDays = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            originalExpirationDays = ChronoUnit.DAYS.between(insertDate, expirationDate);
        }
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

        //TODO
        //Create color index for different percentages EX: 80% and above is green and between 60% and 79% is lime green
        String ANSI_Color = "";

        if (colorIndex < 4)
        {
            ANSI_Color = "\u001B[32m";
        }
        else if (colorIndex >= 4 && colorIndex < 8)
        {
            ANSI_Color = "\u001B[33m";
        }
        else if (colorIndex >= 8)
        {
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
    public long daysTillExpiration ()
    {
        int year;
        int month;
        int day;
        LocalDate insertDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            insertDate = LocalDate.of(year = Integer.parseInt(m_InsertDate.substring(0,3)), month = Integer.parseInt(m_InsertDate.substring(5,7)), day = Integer.parseInt(m_InsertDate.substring(9,11)));
        }
        LocalDate expirationDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            expirationDate = LocalDate.of(year = Integer.parseInt(m_ExpirationDate.substring(0,3)), month = Integer.parseInt(m_ExpirationDate.substring(5,7)), day = Integer.parseInt(m_ExpirationDate.substring(9,11)));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return ChronoUnit.DAYS.between(insertDate, expirationDate);
        }
        else {
            return 8;
        }
    }
//year, month, date

}
