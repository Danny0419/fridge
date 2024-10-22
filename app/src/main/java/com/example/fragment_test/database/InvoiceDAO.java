package com.example.fragment_test.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fragment_test.entity.Invoice;
import com.example.fragment_test.entity.InvoiceWithItems;

import java.util.List;

@Dao
public interface InvoiceDAO {
    // 插入发票数据，若有冲突则替换
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInvoice(Invoice invoice);

    // 根据发票ID查询发票
    @Query("SELECT * FROM Invoice WHERE id = :invoiceId LIMIT 1")
    Invoice getInvoiceById(String invoiceId);
}

