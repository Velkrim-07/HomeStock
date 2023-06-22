package com.example.pnp2_inventory_app;

public class Item {
    private final String m_Name;
    protected int m_Quantity;
    protected String m_ExpirationDate;

    public Item(String name, int quantity, String expirationDate) {
        m_Name = name;
        m_Quantity = quantity;
        m_ExpirationDate = expirationDate;
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
}
