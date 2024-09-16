package com.example.fragment_test.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class InvoiceWithItems {
    @Embedded
    public Invoice invoice;

    @Relation(
            parentColumn = "id",
            entityColumn = "invoice_id"
    )
    public List<InvoiceItem> items;
}
