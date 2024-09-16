package com.example.fragment_test.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "invoice_item",
        foreignKeys = @ForeignKey(entity = Invoice.class,
                parentColumns = "id",
                childColumns = "invoice_id",
                onDelete = ForeignKey.CASCADE))
public class InvoiceItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "invoice_id")
    private int invoiceId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "quantity")
    private String quantity;

    @ColumnInfo(name = "price")
    private String price;

    // Constructor
    public InvoiceItem(int invoiceId, String name, String quantity, String price) {
        this.invoiceId = invoiceId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
}
