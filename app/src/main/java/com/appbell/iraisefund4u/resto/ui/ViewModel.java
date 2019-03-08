package com.appbell.iraisefund4u.resto.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class ViewModel extends AndroidViewModel {


     private  ArrayList<VoucherData> voucherList;


    public ViewModel(@NonNull Application application) {
        super(application);
    }


    public ArrayList<VoucherData> getVoucherList(int voucherBookID) {

      if(voucherList == null ||voucherList.size() == 0 ){

            voucherList = new VoucherBookService(getApplication()).getAllMontVoucherList(voucherBookID);
            ArrayList<VoucherData> expiredVoucherList = new VoucherBookService(getApplication()).getLastMontVoucherList(voucherBookID);
            voucherList.addAll(expiredVoucherList);
        }
        return voucherList;
    }

    public void resetVoucherList(int voucherBookID) {

        voucherList = new VoucherBookService(getApplication()).getAllMontVoucherList(voucherBookID);
        ArrayList<VoucherData> expiredVoucherList = new VoucherBookService(getApplication()).getLastMontVoucherList(voucherBookID);
        voucherList.addAll(expiredVoucherList);
    }
}
