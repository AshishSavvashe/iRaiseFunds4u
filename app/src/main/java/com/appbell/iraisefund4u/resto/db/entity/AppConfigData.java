package com.appbell.iraisefund4u.resto.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.appbell.common.util.AppUtil;

@Entity(tableName = "APP_CONFIG_MASTER")
public class AppConfigData {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    int appConfigId;

    @ColumnInfo(name = "CURRENT_LOGIN_COMMON_USER_ID")
    int currentLoggedInCommonUserId;

    @ColumnInfo(name = "USER_LOGIN_ID")
    String userLoginId;

    @ColumnInfo(name = "USER_LOGIN_PASSWORD")
    String userLoginPassword;

    @ColumnInfo(name = "CUSTOMER_NAME")
    String customerName;

    @ColumnInfo(name = "CUSTOMER_EMAIL_ID")
    String customerEmail;

    @ColumnInfo(name = "CUSTOMER_PHONE")
    String customerPhone;

    @ColumnInfo(name = "CUSTOMER_VOUCHER_BOOK_IDS")
    String voucherBookIds;

    @ColumnInfo(name = "GCM_APP_REG_ID")
    String gcmAppRegId;


    // *********************** NON DB Column ****************************


    public int getAppConfigId() {
        return appConfigId;
    }

    public void setAppConfigId(int appConfigId) {
        this.appConfigId = appConfigId;
    }

    public int getCurrentLoggedInCommonUserId() {
        return currentLoggedInCommonUserId;
    }

    public void setCurrentLoggedInCommonUserId(int currentLoggedInCommonUserId) {
        this.currentLoggedInCommonUserId = currentLoggedInCommonUserId;
    }
    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getUserLoginPassword() {
        return userLoginPassword;
    }

    public void setUserLoginPassword(String userLoginPassword) {
        this.userLoginPassword = userLoginPassword;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getVoucherBookIds() {
        return voucherBookIds;
    }

    public void setVoucherBookIds(String voucherBookIds) {
        this.voucherBookIds = voucherBookIds;
    }

    public String getGcmAppRegId() {
        return gcmAppRegId;
    }

    public void setGcmAppRegId(String gcmAppRegId) {
        this.gcmAppRegId = gcmAppRegId;
    }

}
