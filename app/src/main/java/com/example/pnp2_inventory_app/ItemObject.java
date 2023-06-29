package com.example.pnp2_inventory_app;

import android.content.Context;
import android.widget.TextView;

public class ItemObject {
    Item ItemReference;
    TextView NameObject;
    TextView AmountObject;
    TextView ExpireDateObject;
    Context context;

    public ItemObject(Item ItemAdded, Context fragcontext){
        ItemReference = ItemAdded;
        context = fragcontext;
        ConstructObject(ItemReference);
    }

    public void ConstructObject(Item NewItem){
        NameObject = new TextView(context);
        NameObject.setTextSize(15);
        NameObject.setText(NewItem.getName());
        AmountObject = new TextView(context);
        AmountObject.setTextSize(15);
        AmountObject.setText(String.valueOf(NewItem.getQuantity()));
        ExpireDateObject = new TextView(context);
        ExpireDateObject.setTextSize(15);
        ExpireDateObject.setText(NewItem.getExpirationDate());
    }
}
