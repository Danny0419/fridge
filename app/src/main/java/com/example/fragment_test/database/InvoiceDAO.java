package com.example.fragment_test.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fragment_test.entity.Invoice;
import com.example.fragment_test.entity.InvoiceWithItems;

import java.util.List;

@Dao
public interface InvoiceDAO {

    @Insert
    long insertInvoice(Invoice invoice);

    @Transaction
    @Query("SELECT * FROM invoice")
    LiveData<List<InvoiceWithItems>> getAllInvoicesWithItems();
}

