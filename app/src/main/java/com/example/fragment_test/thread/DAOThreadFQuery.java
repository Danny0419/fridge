package com.example.fragment_test.thread;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fragment_test.helper.FridgeHelper;

public class DAOThreadFQuery implements Runnable {

    private FridgeHelper fridgeHelper;
    private String tabName;
    private String[] columns;
    private String selection;
    private String[] selectionArgs;
    private String groupBy;
    private String having;
    private String orderBy;
    private String limit;
    public DAOThreadFQuery(FridgeHelper fridgeHelper, String tabName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        this.fridgeHelper = fridgeHelper;
        this.tabName = tabName;
        this.columns = columns;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
    }

    @Override
    public void run() {
        SQLiteDatabase database = fridgeHelper.getReadableDatabase();
        Cursor query = database.query(tabName, columns, selection, selectionArgs, groupBy, having, orderBy);

    }

}
