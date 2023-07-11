package DbConfig;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class Util {

    // creates guid for inventoryId and householdId
    public static String CreateGuid(){
        String temp;
        temp =  UUID.randomUUID().toString();
        return temp;
    }

    // Helper method to grab date and format it to date/time
    // yyyy-MM-dd HH:mm:ss
    public static String GetDate(){
        // Get the current date and time
        Calendar calendar = Calendar.getInstance();

        // Format the timestamp as a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        String formattedTimestamp = dateFormat.format(calendar.getTime());

        return formattedTimestamp;

    }
}
