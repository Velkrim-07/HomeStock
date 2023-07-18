package com.example.pnp2_inventory_app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import DbConfig.FirebaseConfig;

public class ScrollBarLayoutBox {
    public List<Item> LayoutItem;
    LinearLayout LayoutView;
    ScrollView scrollview;
    public Item EditedItem;
    dialog_Edit DialogEdit;
    public void MakeLayoutBox(Item NewItem, Context fragContext, fragment_home fragmentHome, dialog_Edit dialogEdit){
        LayoutItem = new ArrayList<>();
        LayoutView = new LinearLayout(fragContext);
        LayoutView.setOrientation(LinearLayout.HORIZONTAL);
        DialogEdit = dialogEdit;

        ViewGroup.LayoutParams AmountObjectParams = new ViewGroup.LayoutParams(100, 50);
        TextView Amount = new TextView(LayoutView.getContext());

        Amount.setText(String.valueOf(NewItem.getQuantity()));
        Amount.setLayoutParams(AmountObjectParams);

        ViewGroup.LayoutParams NameObjectParams = new ViewGroup.LayoutParams(600, 50);
        TextView NameCheckBox = new TextView(LayoutView.getContext());
        NameCheckBox.setText(NewItem.getName());
        NameCheckBox.setLayoutParams(NameObjectParams);

        //Adding the TextViews to the InsideLinearLayout view
        LayoutView.addView(Amount);
        LayoutView.addView(NameCheckBox);
    }

    public void AddToLayoutList(Item layoutToAdd){
        LayoutItem.add(layoutToAdd);
    }
}
