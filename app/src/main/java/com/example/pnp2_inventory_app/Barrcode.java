package com.example.pnp2_inventory_app;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

public class Barrcode extends AppCompatActivity
{
    Button btn_scan;
    setContentView(R.layout.activity_main);
    btn_scan = findViewById(R.id.btn_scan);
    //assigning the button and having it call ScanCode when clicked
        btn_scan.setOnClickListener(v->
    {
        ScanCode();
    });

    //this method will be how the qr code/barcode gets scanned
    private void ScanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    //barLauncher will process the image that is sent in
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->
    {
        //if the content is scanned correctly then it will display in its own text box
        if(result.getContents() != null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            }).show();
        }
    });
}
