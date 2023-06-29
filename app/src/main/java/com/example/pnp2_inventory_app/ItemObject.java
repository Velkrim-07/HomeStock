package com.example.pnp2_inventory_app;

import android.content.Context;
import android.widget.TextView;

public class ItemObject {
    TextView NameObject;
    TextView AmountObject;
    TextView ExpireDateObject;
    Context context;

    public ItemObject(Item ItemAdded, Context fragcontext){
        Item NewItem = ItemAdded;
        context = fragcontext;
        ConstructObject(NewItem);
    }

    public void ConstructObject(Item NewItem){
        NameObject = new TextView(context);
        NameObject.setTextSize(30);
        NameObject.setText(NewItem.getName());
        AmountObject = new TextView(context);
        AmountObject.setTextSize(30);
        AmountObject.setText(String.valueOf(NewItem.getQuantity()));
        ExpireDateObject = new TextView(context);
        ExpireDateObject.setTextSize(30);
        ExpireDateObject.setText(NewItem.getExpirationDate());
    }
}
