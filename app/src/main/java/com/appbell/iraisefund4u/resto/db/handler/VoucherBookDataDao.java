package com.appbell.iraisefund4u.resto.db.handler;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.appbell.iraisefund4u.resto.ui.VoucherBookData;

import java.util.List;

@Dao
public interface VoucherBookDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createVoucherBookData(VoucherBookData voucherBookData);

    @Query("SELECT * FROM VOUCHER_BOOK_MASTER where BOOK_ID =:voucherBookId")
    VoucherBookData getVoucherDataBy(int voucherBookId);

    @Query("SELECT LAST_SYNCTIME  FROM VOUCHER_BOOK_MASTER where BOOK_ID =:voucherBookId")
    long getlastSyncTime(int voucherBookId);

    @Query("SELECT TERMS_AND_CONDITION FROM VOUCHER_BOOK_MASTER where BOOK_ID =:voucherBookId")
    String getTermsAndCondition(int voucherBookId);

    @Query("UPDATE VOUCHER_BOOK_MASTER SET LAST_SYNCTIME=:lastSyncTime  where BOOK_ID =:voucherBookId")
    void updatelastSyncTime(long lastSyncTime, int voucherBookId);


    @Query("UPDATE VOUCHER_BOOK_MASTER SET LAST_DELETED_VOUCHERBOOK_TIME=:lastDeletedVoucherBookTime  where BOOK_ID =:voucherBookId")
    void updateVoucherDeletedTime(long lastDeletedVoucherBookTime, int voucherBookId);


    @Query("SELECT LAST_DELETED_VOUCHERBOOK_TIME  FROM VOUCHER_BOOK_MASTER where BOOK_ID =:voucherBookId")
    long getLastDeletedVoucherTime(int voucherBookId);

    @Query("delete from VOUCHER_BOOK_MASTER")
    void deleteAllVoucherBook();

    @Query("select * from VOUCHER_BOOK_MASTER")
    List<VoucherBookData> getVoucherBookData();

    @Update
    void updateVoucherBookData (VoucherBookData voucherBookData);

}
