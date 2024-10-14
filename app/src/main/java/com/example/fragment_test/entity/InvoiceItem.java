package com.example.fragment_test.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Invoice.class,
        parentColumns = "id",
        childColumns = "invoiceId",
        onDelete = CASCADE))
public class InvoiceItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int invoiceId;
    private String name;
    private String quantity;
    private String price;

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
