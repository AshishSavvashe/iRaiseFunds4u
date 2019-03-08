package com.appbell.iraisefund4u.resto.db.handler;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.appbell.iraisefund4u.resto.ui.VoucherData;

import java.util.List;

@Dao
public interface VoucherDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createVoucherData(VoucherData voucherData);

    @Query("SELECT * FROM VOUCHER_DATA_MASTER where VOUCHER_ID =:voucherId")
    VoucherData getVoucherData(int voucherId);


    @Query("SELECT * from VOUCHER_DATA_MASTER where BOOK_ID =:book_id")
    List<VoucherData> getAllVoucherList(int book_id);

    @Query("SELECT COUNT(*) from VOUCHER_DATA_MASTER where BOOK_ID =:book_id")
    int getAllVoucherCount(int book_id);

    @Query("SELECT COUNT(*) from VOUCHER_DATA_MASTER where VOUCHER_STATUS =:voucherStatus")
    int getRedemVoucherCount(String voucherStatus);

    @Query("SELECT *  FROM VOUCHER_DATA_MASTER where BOOK_ID =:book_id")
    List<VoucherData>getRedeemVoucherList(int book_id);

    @Query("SELECT * from VOUCHER_DATA_MASTER where BOOK_ID =:bookId AND EXPIRY_DATE <:currentDate ORDER BY START_DATE ASC,RESTAURANT_NAME ASC")
    List<VoucherData>getExpiredVoucherList(int bookId, long currentDate);

    @Query("SELECT * from VOUCHER_DATA_MASTER where BOOK_ID =:book_id AND EXPIRY_DATE >:currentDate ORDER BY START_DATE ASC ,VOUCHER_STATUS_ID ASC,RESTAURANT_NAME ASC")
    List<VoucherData> getActiveVoucherList(int book_id, long currentDate);

    @Query("SELECT RESTAURANT_NAME  FROM VOUCHER_DATA_MASTER  where BOOK_ID =:book_id  GROUP BY RESTAURANT_NAME")
    List<String> getRestaurantname (int book_id);

    @Query("SELECT VOUCHER_REDEEM_DATE FROM VOUCHER_DATA_MASTER where BOOK_ID =:book_id ")
    String getVoucherRedeemDates(int book_id );


    @Query("SELECT RESTAURANT_NAME  FROM VOUCHER_DATA_MASTER where BOOK_ID =:book_id AND VOUCHER_REDEEM_DATE=:voucherRedemmDate")
    String getRedeemVoucherRestoname(int book_id, String voucherRedemmDate);

    @Query("SELECT VOUCHER_DESC  FROM VOUCHER_DATA_MASTER where BOOK_ID =:book_id AND RESTAURANT_NAME=:restaurantName")
    String getRedeemVoucherDesc(int book_id, String restaurantName);

    @Update
    void updateVoucherData(VoucherData voucherData);

    @Query("UPDATE VOUCHER_DATA_MASTER SET VOUCHER_REDEEM_DATE=:voucherRedemmDate  where VOUCHER_ID =:voucherId")
    void updateVoucherRedeemDate(String voucherRedemmDate, int voucherId);

    @Query("delete from VOUCHER_DATA_MASTER")
    void deleteAllVoucher();

    @Query("delete from VOUCHER_DATA_MASTER where BOOK_ID =:voucherBookId")
    void deleteVoucher(int voucherBookId);

}
