package com.appbell.iraisefund4u.resto.ui;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "VOUCHER_DATA_MASTER")
public class VoucherData implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    int appVoucherBookId;

    @ColumnInfo(name ="VOUCHER_ID")
    private int voucherId;

    @ColumnInfo(name = "RESTAURANT_ID")
    private int restaurantId;

    @ColumnInfo(name = "VOUCHER_CODE")
    private String voucherCode;

    @ColumnInfo(name = "SALE_VALUE")
    private float saleValue;

    @ColumnInfo(name = "VOUCHER_VALUE")
    private float voucherValue;

    @ColumnInfo(name = "EXPIRY_DATE")
    private long voucherEndTime;

    @ColumnInfo(name = "VALIDITY")
    private int validity;

    @ColumnInfo(name = "VOUCHER_USE")
    private String voucherUse;

    @ColumnInfo(name = "SOLD_BY")
    private String soldBy;

    @ColumnInfo(name = "VOUCHER_STATUS")
    private String voucherStatus;

    @ColumnInfo(name = "VOUCHER_DESC")
    private String voucherDesc;

    @ColumnInfo(name = "IMAGE_NAME")
    private String imageName;

    @ColumnInfo(name = "COMMISSION")
    private float commission;

    @ColumnInfo(name = "GROUP_ID")
    private String groupId;

    @ColumnInfo(name = "COMMENT")
    private String comment;

    @ColumnInfo(name = "CUSTOMER_NAME")
    private String customerName;

    @ColumnInfo(name = "CUSTOMER_EMAIL")
    private String customerEmail;

    @ColumnInfo(name = "CUSTOMER_PHONE")
    private String customerPhone;

    @ColumnInfo(name = "SERIAL_NUMBER")
    private int serialNumber;

    @ColumnInfo(name = "PURPOSE")
    private String purpose;

    @ColumnInfo(name = "ACTIVATION_DAYS")
    private int activationDays;

    @ColumnInfo(name = "START_DATE")
    private long voucherStartTime;

    @ColumnInfo(name = "BOOK_ID")
    private int bookId;

    @ColumnInfo(name = "MAX_ALLOWED_COUNT")
    private int maxAllowedCount;

    @ColumnInfo(name = "USED_COUNT")
    private int usedCount;

    @ColumnInfo(name = "NOTES")
    private String notes;

    @ColumnInfo(name = "PURCHES_DATE")
    private long purchesDate;

    @ColumnInfo(name = "START_REDEEM_DATE")
    private String startRedeemDate;

    @ColumnInfo(name = "FACILITY_ID")
    private int facilityId;

    @ColumnInfo(name = "COMPAING_ID")
    private int compaingId;

    @ColumnInfo(name = "VOUCHER_MSG")
    private String voucherMsg;

    @ColumnInfo(name = "VOUCHER_ALIES")
    private String voucherAlies;

    //Non DB Col
    @ColumnInfo(name = "RESTAURANT_NAME")
    private String restaurantName;

    @ColumnInfo(name = "RESTAURANT_ADDRESS")
    private String restaurantAddress;

    @ColumnInfo(name = "PHONE1")
    private String restaurantPhone;

    @ColumnInfo(name = "RESTAURANT_WEBSITE_URL")
    private String resaurantWeburl;

    @ColumnInfo(name = "DEMAND_OBJECT_TYPE")
    private String demandobjectType;

    @ColumnInfo(name = "DEMAND_OBJECT_ID")
    private int    demandobjId;



    @ColumnInfo(name = "GROUP_TOTAL_QTY")
    private int grpTotalQty;

    @ColumnInfo(name = "GROUP_SOLD_QTY")
    private int grpSoldQty;

    @ColumnInfo(name = "AVAL_IDS")
    private String avalIds;

    @ColumnInfo(name = "AVIABLR_VCIMAGENAME")
    private String availableVCImgNames;

    @ColumnInfo(name = "USAGECOUNTSTR")
    private String usageCountStr;

    @ColumnInfo(name = "COMAINGNAME")
    private String compaingName;

    @ColumnInfo(name = "RESTAURANTLONG")
    private double restaurantlong = 0.0;

    @ColumnInfo(name = "RESTAURANTLAT")
    private double restaurantlat = 0.0;

    @ColumnInfo(name = "VOUCHER_RESTODATA")
    private String voucherRestodata;

    @ColumnInfo(name = "VOUCHER_STATUS_ID")
    private int voucherStatusId;

    @ColumnInfo(name = "VOUCHER_REDEEM_DATE")
    private String voucherRedemmDates;

    @Ignore
    private boolean isheader;

    public VoucherData() {
    }

    public String getVoucherRestodata() {
        return voucherRestodata;
    }

    public void setVoucherRestodata(String voucherRestodata) {
        this.voucherRestodata = voucherRestodata;
    }



    protected VoucherData(Parcel in) {
        voucherId = in.readInt();
        restaurantId = in.readInt();
        voucherCode = in.readString();
        saleValue = in.readFloat();
        voucherValue = in.readFloat();
        validity = in.readInt();
        voucherUse = in.readString();
        soldBy = in.readString();
        voucherStatus = in.readString();
        voucherDesc = in.readString();
        imageName = in.readString();
        commission = in.readFloat();
        groupId = in.readString();
        comment = in.readString();
        customerName = in.readString();
        customerEmail = in.readString();
        customerPhone = in.readString();
        serialNumber = in.readInt();
        purpose = in.readString();
        activationDays = in.readInt();
        bookId = in.readInt();
        maxAllowedCount = in.readInt();
        usedCount = in.readInt();
        notes = in.readString();
        facilityId = in.readInt();
        compaingId = in.readInt();
        restaurantName = in.readString();
        restaurantAddress = in.readString();
        restaurantPhone = in.readString();
        resaurantWeburl = in.readString();
        demandobjectType = in.readString();
        demandobjId = in.readInt();
        grpTotalQty = in.readInt();
        grpSoldQty = in.readInt();
        avalIds = in.readString();
        availableVCImgNames = in.readString();
        usageCountStr = in.readString();
        compaingName = in.readString();

        //  voucherEndTime = new Date(in.readLong());
        //  purchesDate = new Date(in.readLong());

        voucherEndTime = in.readLong();
        purchesDate = in.readLong();

        //  startRedeemDate = new Date(in.readLong());
        startRedeemDate = in.readString();
        restaurantlong = in.readDouble();
        restaurantlat = in.readDouble();
        voucherMsg = in.readString();
        voucherAlies= in.readString();
        voucherStatusId = in.readInt();
        voucherRedemmDates =in.readString();

    }

    public static final Creator<VoucherData> CREATOR = new Creator<VoucherData>() {
        @Override
        public VoucherData createFromParcel(Parcel in) {
            return new VoucherData(in);
        }

        @Override
        public VoucherData[] newArray(int size) {
            return new VoucherData[size];
        }
    };


    public int getAppVoucherBookId() {
        return appVoucherBookId;
    }

    public void setAppVoucherBookId(int appVoucherBookId) {
        this.appVoucherBookId = appVoucherBookId;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public float getSaleValue() {
        return saleValue;
    }

    public void setSaleValue(float saleValue) {
        this.saleValue = saleValue;
    }

    public float getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(float voucherValue) {
        this.voucherValue = voucherValue;
    }

    public long getVoucherEndTime() {
        return voucherEndTime;
    }

    public void setVoucherEndTime(long voucherEndTime) {
        this.voucherEndTime = voucherEndTime;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public String getVoucherUse() {
        return voucherUse;
    }

    public void setVoucherUse(String voucherUse) {
        this.voucherUse = voucherUse;
    }

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
    }

    public String getVoucherStatus() {
        return voucherStatus;
    }

    public void setVoucherStatus(String voucherStatus) {
        this.voucherStatus = voucherStatus;
    }

    public String getVoucherDesc() {
        return voucherDesc;
    }

    public void setVoucherDesc(String voucherDesc) {
        this.voucherDesc = voucherDesc;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getActivationDays() {
        return activationDays;
    }

    public void setActivationDays(int activationDays) {
        this.activationDays = activationDays;
    }

    public long getVoucherStartTime() {
        return voucherStartTime;
    }

    public void setVoucherStartTime(long voucherStartTime) {
        this.voucherStartTime = voucherStartTime;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMaxAllowedCount() {
        return maxAllowedCount;
    }

    public void setMaxAllowedCount(int maxAllowedCount) {
        this.maxAllowedCount = maxAllowedCount;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getRestaurantPhone() {
        return restaurantPhone;
    }

    public void setRestaurantPhone(String restaurantPhone) {
        this.restaurantPhone = restaurantPhone;
    }

    public String getDemandobjectType() {
        return demandobjectType;
    }

    public void setDemandobjectType(String demandobjectType) {
        this.demandobjectType = demandobjectType;
    }

    public int getDemandobjId() {
        return demandobjId;
    }

    public void setDemandobjId(int demandobjId) {
        this.demandobjId = demandobjId;
    }


    public int getGrpTotalQty() {
        return grpTotalQty;
    }

    public void setGrpTotalQty(int grpTotalQty) {
        this.grpTotalQty = grpTotalQty;
    }

    public int getGrpSoldQty() {
        return grpSoldQty;
    }

    public void setGrpSoldQty(int grpSoldQty) {
        this.grpSoldQty = grpSoldQty;
    }

    public String getAvalIds() {
        return avalIds;
    }

    public void setAvalIds(String avalIds) {
        this.avalIds = avalIds;
    }

    public String getAvailableVCImgNames() {
        return availableVCImgNames;
    }

    public void setAvailableVCImgNames(String availableVCImgNames) {
        this.availableVCImgNames = availableVCImgNames;
    }

    public String getUsageCountStr() {
        return usageCountStr;
    }

    public void setUsageCountStr(String usageCountStr) {
        this.usageCountStr = usageCountStr;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getPurchesDate() {
        return purchesDate;
    }

    public void setPurchesDate(long purchesDate) {
        this.purchesDate = purchesDate;
    }

    public String getStartRedeemDate() {
        return startRedeemDate;
    }

    public void setStartRedeemDate(String startRedeemDate) {
        this.startRedeemDate = startRedeemDate;
    }

   /* public void setStartRedeemDate(Date startRedeemDate) {
        this.startRedeemDate = startRedeemDate;
    }*/


    public String getResaurantWeburl() {
        return resaurantWeburl;
    }

    public void setResaurantWeburl(String resaurantWeburl) {
        this.resaurantWeburl = resaurantWeburl;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public String getCompaingName() {
        return compaingName;
    }

    public void setCompaingName(String compaingName) {
        this.compaingName = compaingName;
    }

    public int getCompaingId() {
        return compaingId;
    }

    public void setCompaingId(int compaingId) {
        this.compaingId = compaingId;
    }


    public double getRestaurantlong() {
        return restaurantlong;
    }

    public void setRestaurantlong(double restaurantlong) {
        this.restaurantlong = restaurantlong;
    }

    public double getRestaurantlat() {
        return restaurantlat;

    }

    public void setRestaurantlat(double restaurantlat) {
        this.restaurantlat = restaurantlat;
    }

    public String getVoucherMsg() {
        return voucherMsg;
    }

    public void setVoucherMsg(String voucherMsg) {
        this.voucherMsg = voucherMsg;
    }


    public String getVoucherAlies() {
        return voucherAlies;
    }

    public void setVoucherAlies(String voucherAlies) {
        this.voucherAlies = voucherAlies;
    }

    public int getVoucherStatusId() {
        return voucherStatusId;
    }

    public void setVoucherStatusId(int voucherStatusId) {
        this.voucherStatusId = voucherStatusId;
    }


    public boolean isIsheader() {
        return isheader;
    }

    public void setIsheader(boolean isheader) {
        this.isheader = isheader;
    }

    public String getVoucherRedemmDates() {
        return voucherRedemmDates;
    }

    public void setVoucherRedemmDates(String voucherRedemmDate) {
        this.voucherRedemmDates = voucherRedemmDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(voucherId);
        dest.writeInt(restaurantId);
        dest.writeString(voucherCode);
        dest.writeFloat(saleValue);
        dest.writeFloat(voucherValue);
        dest.writeInt(validity);
        dest.writeString(voucherUse);
        dest.writeString(soldBy);
        dest.writeString(voucherStatus);
        dest.writeString(voucherDesc);
        dest.writeString(imageName);
        dest.writeFloat(commission);
        dest.writeString(groupId);
        dest.writeString(comment);
        dest.writeString(customerName);
        dest.writeString(customerEmail);
        dest.writeString(customerPhone);
        dest.writeInt(serialNumber);
        dest.writeString(purpose);
        dest.writeInt(activationDays);
        dest.writeInt(bookId);
        dest.writeInt(maxAllowedCount);
        dest.writeInt(usedCount);
        dest.writeString(notes);
        dest.writeInt(facilityId);
        dest.writeInt(compaingId);
        dest.writeString(restaurantName);
        dest.writeString(restaurantAddress);
        dest.writeString(restaurantPhone);
        dest.writeString(resaurantWeburl);
        dest.writeString(demandobjectType);
        dest.writeInt(demandobjId);
        dest.writeInt(grpTotalQty);
        dest.writeInt(grpSoldQty);
        dest.writeString(avalIds);
        dest.writeString(availableVCImgNames);
        dest.writeString(usageCountStr);
        dest.writeString(compaingName);
        dest.writeLong(voucherEndTime);
        dest.writeLong(purchesDate);

        if(startRedeemDate != null){
            //  dest.writeLong(startRedeemDate.getTime());
            dest.writeString(startRedeemDate);
        }else {
            dest.writeString("");
        }

        dest.writeDouble(restaurantlong);
        dest.writeDouble(restaurantlat);
        dest.writeString(voucherMsg);
        dest.writeString(voucherAlies);
        dest.writeInt(voucherStatusId);

        if(voucherRedemmDates != null){
            dest.writeString(voucherRedemmDates);
        }else {
            dest.writeString("");
        }

    }

}
