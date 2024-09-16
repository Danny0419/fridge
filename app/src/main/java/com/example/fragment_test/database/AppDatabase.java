package com.example.fragment_test.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fragment_test.entity.Invoice;
import com.example.fragment_test.entity.InvoiceItem;

@Database(entities = {Invoice.class, InvoiceItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract InvoiceDAO invoiceDAO();
    public abstract InvoiceItemDAO invoiceItemDAO();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
