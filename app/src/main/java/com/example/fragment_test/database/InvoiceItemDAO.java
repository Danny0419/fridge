package com.example.fragment_test.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fragment_test.entity.InvoiceItem;

import java.util.List;

@Dao
public interface InvoiceItemDAO {

    @Insert
    void insertInvoiceItems(List<InvoiceItem> items);

    @Query("SELECT * FROM invoiceitem WHERE invoiceid = :invoiceId")
    LiveData<List<InvoiceItem>> getItemsForInvoice(int invoiceId);

}
