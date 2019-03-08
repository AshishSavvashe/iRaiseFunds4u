package com.appbell.iraisefund4u.resto.ui;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "VOUCHER_BOOK_MASTER")
public class VoucherBookData {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    int appVoucherBookId;

    @ColumnInfo(name = "BOOK_ID")
    private int bookId;

    @ColumnInfo(name = "GROUP_ID")
    private String groupId;

    @ColumnInfo(name = "CUSTOMER_EMAIL")
    private String customerEmail;

    @ColumnInfo(name = "CUSTOMER_PHONE")
    private String customerPhone;

    @ColumnInfo(name = "CUSTOMER_NAME")
    private String customerName;

    @ColumnInfo(name = "BOOK_STATUS")
    private String bookStatus;

    @ColumnInfo(name = "BOOK_CODE")
    private String bookCode;

    @ColumnInfo(name = "CAMPAING_ID")
    private int campaignId;

    @ColumnInfo(name = "CAMPAING_TAG_LINE")
    private String campaignTagLine;

    @ColumnInfo(name = "FILE_NAME")
    private String fileName;

    @ColumnInfo(name = "VOUCHER_START_TIME")
    private long voucherStartTime;

    @ColumnInfo(name = "VOUCHER_END_TIME")
    private long voucherEndTime;

    @ColumnInfo(name = "LAST_SYNCTIME")
    private long lastsyncTime;

    @ColumnInfo(name = "TERMS_AND_CONDITION")
    private String campaingTermsAndCondition;

    @ColumnInfo(name = "TIME_ZONE_PROP")
    private String timeZoneProp;


    @ColumnInfo(name = "LAST_DELETED_VOUCHERBOOK_TIME")
    private long lastDeletedVoucherBookTime;




    public int getAppVoucherBookId() {
        return appVoucherBookId;
    }

    public void setAppVoucherBookId(int appVoucherBookId) {
        this.appVoucherBookId = appVoucherBookId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignTagLine() {
        return campaignTagLine;
    }

    public void setCampaignTagLine(String campaignTagLine) {
        this.campaignTagLine = campaignTagLine;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getVoucherStartTime() {
        return voucherStartTime;
    }

    public void setVoucherStartTime(long voucherStartTime) {
        this.voucherStartTime = voucherStartTime;
    }

    public long getVoucherEndTime() {
        return voucherEndTime;
    }

    public void setVoucherEndTime(long voucherEndTime) {
        this.voucherEndTime = voucherEndTime;
    }

    public long getLastsyncTime() {
        return lastsyncTime;
    }

    public void setLastsyncTime(long lastsyncTime) {
        this.lastsyncTime = lastsyncTime;
    }

    public String getCampaingTermsAndCondition() {
        return campaingTermsAndCondition;
    }

    public void setCampaingTermsAndCondition(String campaingTermsAndCondition) {
        this.campaingTermsAndCondition = campaingTermsAndCondition;
    }

    public String getTimeZoneProp() {
        return timeZoneProp;
    }

    public void setTimeZoneProp(String timeZoneProp) {
        this.timeZoneProp = timeZoneProp;
    }

    public long getLastDeletedVoucherBookTime() {
        return lastDeletedVoucherBookTime;
    }

    public void setLastDeletedVoucherBookTime(long lastDeletedVoucherBookTime) {
        this.lastDeletedVoucherBookTime = lastDeletedVoucherBookTime;
    }
}
