package com.example.fragment_test.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = @ForeignKey(entity = Invoice.class,
                parentColumns = "id",
                childColumns = "invoiceId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("invoiceId")}  // 为外键列创建索引
)
public class InvoiceItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public int quantity;
    public double price;

    @ColumnInfo(name = "invoiceId")
    public String invoiceId;  // invoiceId 是 int 类型

    // 构造函数匹配字段类型
    public InvoiceItem(String invoiceId, String name, int quantity, double price) {
        this.invoiceId = invoiceId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    // 提供无参构造函数（Room 要求）
    public InvoiceItem() {
    }

    // Getters 和 Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
