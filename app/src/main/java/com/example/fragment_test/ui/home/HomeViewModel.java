package com.example.fragment_test.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fragment_test.database.FridgeDatabase;
import com.example.fragment_test.database.InvoiceDAO;
import com.example.fragment_test.database.InvoiceItemDAO;
import com.example.fragment_test.entity.InvoiceWithItems;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mText;
    //private final LiveData<List<InvoiceWithItems>> invoiceWithItemsList;

    public HomeViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

        // 获取数据库实例
        //FridgeDatabase db = FridgeDatabase.getInstance(application);
        //InvoiceDAO invoiceDAO = db.invoiceDAO();
        //InvoiceItemDAO invoiceItemDAO = db.invoiceItemDAO();

        // 查询所有发票及其品项
        //invoiceWithItemsList = invoiceDAO.getAllInvoicesWithItems();
    }

    public LiveData<String> getText() {
        return mText;
    }

    //public LiveData<List<InvoiceWithItems>> getInvoiceWithItemsList() {
    //    return invoiceWithItemsList;
    //}
}