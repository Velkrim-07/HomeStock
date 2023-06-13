package com.example.pnp2_inventory_app;

//This class will be used to create the Variables needed in order to name, group, and delete
//the items that will be going into the database.
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

}
