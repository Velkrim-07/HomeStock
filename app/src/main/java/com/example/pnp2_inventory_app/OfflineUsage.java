package com.example.pnp2_inventory_app;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

public class OfflineUsage {
    private Activity main;

    OfflineUsage(Activity mainActivity){
        main = mainActivity;
    }

    //checks if there is an internet connection
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) main.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //takes the list of items and creates a string with all of the items listed
    //Delimiter is a ,(comma) and the end of object is \n (newline)
    private String ItemsToList(List<Item> Items){
        String writeString = ""; //string to hold list of items
        for (Item item: Items) {
            //saves the items to a string
            writeString = writeString + item.documentId + "," + item.m_Name + "," + item.m_Quantity + "," + item.m_ExpirationDate + "," + item.insertedDate + "," + item.lastUpdated + ",\n";
        }
        return writeString;
    }

    public void writeToFile(List<Item> Items, File file) {
        try {
            String data = ItemsToList(Items); //takes the list of items and transfers it into a string
            FileOutputStream fos = new FileOutputStream(file); //opens up a file to write in
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter (fos); //opens up the writer to write in the text file
            outputStreamWriter.write(data); //writes the string in the text file
            outputStreamWriter.flush();//pushes the string into the document
            outputStreamWriter.close(); //closes the outputWriter
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString()); //just in case there is no file
        }
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is)); //creates a buffer to read in the string
        StringBuilder sb = new StringBuilder(); //creates a string builder to create the string we need to parse through
        String line = null; //initialises the string
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");//writes the string while the document is not null
        }
        reader.close();//closes the reader
        return sb.toString();//returns the string
    }

    public static String getStringFromFile (File file) throws Exception {
        FileInputStream fin = new FileInputStream(file);//creates a FileInput
        String ret = convertStreamToString(fin);//converts the String into a string
        fin.close();//closes the File Input
        return ret;//returns the String
    }
}
